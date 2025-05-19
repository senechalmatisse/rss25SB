package fr.univrouen.rss25SB.controllers;

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
 * Cet endpoint permet de recevoir un flux RSS conforme au schéma XSD {@code rss25.xsd},
 * de le valider, de le désérialiser en objet {@link Feed}, et d’enregistrer les
 * articles {@link Item} dans la base de données s’ils n’existent pas déjà.
 * </p>
 *
 * <p><b>URL :</b> {@code POST /rss25SB/insert}</p>
 * <p><b>Consommé :</b> {@code application/xml}</p>
 * <p><b>Produit :</b> {@code application/xml}</p>
 *
 * @author Matisse SENECHAL
 * @version 1.1
 * @see ItemService
 * @see Feed
 * @see InsertResponseDTO
 */
@RestController
@RequestMapping("/rss25SB")
@RequiredArgsConstructor
public class InsertController {

    /** Service métier responsable de la sauvegarde et de la vérification des articles. */
    private final ItemService itemService;

    /**
     * Endpoint POST permettant d’insérer un flux RSS au format XML.
     * <p>
     * Le processus suit les étapes suivantes :
     * <ol>
     *   <li>Validation du XML reçu par rapport au XSD (via {@link XmlUtil})</li>
     *   <li>Désérialisation vers un objet {@link Feed}</li>
     *   <li>Filtrage des articles déjà existants (basé sur {@code title + published})</li>
     *   <li>Insertion des nouveaux articles dans la base</li>
     *   <li>Retour d’une réponse XML indiquant succès ou erreur</li>
     * </ol>
     * </p>
     *
     * @param xmlContent le contenu XML brut du flux RSS envoyé dans la requête
     * @return une réponse XML contenant la liste des IDs insérés ou un message d’erreur
     */
    @PostMapping(
        value = "/insert",
        consumes = MediaType.APPLICATION_XML_VALUE,
        produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<InsertResponseDTO> insertRssFeed(@RequestBody String xmlContent) {
        String messageErreur = "Erreur lors de la soumission d’un flux rss25SB:\n";

        try {
            // Désérialisation et validation XSD
            Feed feed = XmlUtil.unmarshal(xmlContent, Feed.class, Constants.XSD_PATH);

            // Filtrage et insertion des nouveaux items
            List<Long> insertedIds = feed.getItem().stream()
                .filter(item -> !itemService.itemExists(item.getGuid()))
                .map(itemService::saveItemFromXml)
                .toList();

            // Construction de la réponse avec code HTTP adapté
            if (insertedIds.isEmpty()) {
                messageErreur += "aucun article(s) inséré(s) parce qu'il(s) existe(nt) déjà.";
                // Aucun nouvel article inséré : 204 No Content
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(InsertResponseDTO.error(messageErreur));
            }

            // Articles insérés : 201 Created
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(InsertResponseDTO.success(insertedIds));
        } catch (JAXBException | SAXException e) {
            messageErreur += XmlUtil.extractFirstErrorMessage(e);
            // Flux XML invalide : 400 Bad Request
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(InsertResponseDTO.error(messageErreur));
        } catch (Exception e) {
            messageErreur += XmlUtil.extractFirstErrorMessage(e);
            // Erreur inattendue : 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(InsertResponseDTO.error(messageErreur));
        }
    }
}