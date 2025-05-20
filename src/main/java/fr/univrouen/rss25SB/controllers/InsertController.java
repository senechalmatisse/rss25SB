package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.converter.FluxSourceSelector;
import fr.univrouen.rss25SB.dto.InsertResponseDTO;
import fr.univrouen.rss25SB.model.xml.*;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.XmlUtil;
import fr.univrouen.rss25SB.utils.constants.Constants;

import jakarta.xml.bind.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import java.util.*;

/**
 * Contrôleur REST exposant l’endpoint permettant l’insertion d’un flux RSS au format XML.
 * <p>
 * Cet endpoint permet de :
 * <ul>
 *     <li>valider un flux XML reçu selon le schéma XSD {@code rss25.xsd},</li>
 *     <li>désérialiser ce flux en un objet {@link Feed},</li>
 *     <li>insérer en base les articles {@link Item} qui ne sont pas encore présents,</li>
 *     <li>ou effectuer une conversion automatique si le format n’est pas natif rss25SB.</li>
 * </ul>
 * </p>
 *
 * <p><b>URL :</b> {@code POST /rss25SB/insert}</p>
 * <p><b>Consommé :</b> {@code application/xml}</p>
 * <p><b>Produit :</b> {@code application/xml}</p>
 *
 * @author Matisse SENECHAL
 * @version 2.2
 * @see ItemService
 * @see Feed
 * @see InsertResponseDTO
 */
@RestController
@RequestMapping("/rss25SB")
@RequiredArgsConstructor
public class InsertController {

    /** Service métier permettant d'accéder aux articles stockés en base. */
    private final ItemService itemService;

    /**
     * Sélecteur de stratégie de conversion automatique de flux RSS externes vers le format {@code rss25SB}.
     * <p>
     * Permet d'identifier dynamiquement la source d’un flux XML non conforme (ex: Le Monde, AFP, etc.)
     * et d’en effectuer la conversion au format attendu par le service via des convertisseurs spécifiques.
     */
    private final FluxSourceSelector fluxSourceSelector;

    /**
     * Endpoint POST permettant d’insérer un flux RSS au format XML.
     * <p>
     * Si le flux est conforme au schéma rss25SB, il est directement traité.
     * Sinon, une tentative de conversion est effectuée via {@link FluxSourceSelector}.
     * Le contrôleur retourne un statut HTTP approprié selon le résultat :
     * <ul>
     *     <li>201 Created : articles insérés avec succès</li>
     *     <li>204 No Content : aucun article inséré (doublons)</li>
     *     <li>400 Bad Request : flux invalide ou non reconnu</li>
     *     <li>500 Internal Server Error : erreur inattendue lors de la sauvegarde</li>
     * </ul>
     *
     * @param xmlContent le contenu XML du flux RSS soumis
     * @return {@link ResponseEntity} contenant un objet {@link InsertResponseDTO}
     */
    @PostMapping(
        value = "/insert",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<InsertResponseDTO> insertRssFeed(@RequestBody String xmlContent) {
        StringBuilder messageErreur = new StringBuilder("Erreur lors de la soumission d’un flux XML :\n");
        Feed feed = tryDeserializeOrConvert(xmlContent, messageErreur);

        if (feed == null) {
            return ResponseEntity.badRequest().body(InsertResponseDTO.error(messageErreur.toString()));
        }

        return tryInsertItems(feed, messageErreur);
    }

    /**
     * Tente de désérialiser le flux XML au format natif rss25SB, puis,
     * en cas d’échec, utilise le convertisseur {@link FluxSourceSelector}
     * pour identifier une autre source (ex: Le Monde) et adapter le format.
     *
     * @param xmlContent le flux XML brut soumis à l’API
     * @param errorMsg   chaîne utilisée pour construire le message d’erreur
     * @return un objet {@link Feed} si la conversion réussit, {@code null} sinon
     */
    private Feed tryDeserializeOrConvert(String xmlContent, StringBuilder errorMsg) {
        try {
            return XmlUtil.unmarshal(xmlContent, Feed.class, Constants.XSD_PATH);
        } catch (JAXBException | SAXException e) {
            errorMsg.append("- Flux non valide au format rss25SB. Tentative de conversion automatique...\n");
            try {
                return fluxSourceSelector.convert(xmlContent);
            } catch (UnsupportedOperationException ex) {
                errorMsg.append("- Flux non reconnu : ").append(ex.getMessage());
            } catch (Exception ex) {
                errorMsg.append("- Échec de conversion : ").append(XmlUtil.extractFirstErrorMessage(ex));
            }
        } catch (Exception e) {
            errorMsg.append("- Erreur inattendue : ").append(XmlUtil.extractFirstErrorMessage(e));
        }
        return null;
    }

    /**
     * Insère les articles contenus dans le {@link Feed} passé en paramètre.
     * <p>
     * Seuls les articles n’ayant pas encore été enregistrés (vérification sur le GUID)
     * sont insérés. Une réponse est construite selon l’état d’insertion :
     * <ul>
     *     <li>201 Created si des articles ont été insérés,</li>
     *     <li>204 No Content si tous les articles existaient déjà,</li>
     *     <li>500 Internal Server Error en cas d’échec inattendu.</li>
     * </ul>
     *
     * @param feed      le flux {@link Feed} à insérer
     * @param errorMsg  message d’erreur à enrichir en cas d’exception
     * @return {@link ResponseEntity} contenant une réponse {@link InsertResponseDTO}
     */
    private ResponseEntity<InsertResponseDTO> tryInsertItems(Feed feed, StringBuilder errorMsg) {
        try {
            List<Long> insertedIds = feed.getItem().stream()
                .filter(item -> !itemService.itemExists(item.getGuid()))
                .map(itemService::saveItemFromXml)
                .toList();

            if (insertedIds.isEmpty()) {
                errorMsg.append("- Aucun article inséré : tous déjà présents.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(InsertResponseDTO.error(errorMsg.toString()));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(InsertResponseDTO.success(insertedIds));
        } catch (Exception e) {
            errorMsg.append("- Erreur lors de la sauvegarde : ")
                    .append(XmlUtil.extractFirstErrorMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InsertResponseDTO.error(errorMsg.toString()));
        }
    }
}