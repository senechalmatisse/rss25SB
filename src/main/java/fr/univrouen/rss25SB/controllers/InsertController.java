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
        try {
            // Désérialisation XML vers objet Java Feed + validation via XSD
            Feed feed = XmlUtil.unmarshal(xmlContent, Feed.class, Constants.XSD_PATH);

            // Insertion uniquement des articles n’existant pas déjà en base
            List<Long> insertedIds = feed.getItem().stream()
                .filter(item -> !itemService.itemExists(item.getTitle(), item.getPublished()))
                .map(itemService::saveItemFromXml)
                .toList();

            // Retour de la réponse avec la liste des IDs
            // ou une erreur s’il n’y a aucun nouvel article
            return ResponseEntity.ok(
                insertedIds.isEmpty()
                        ? InsertResponseDTO.error()              // aucun article inséré
                        : InsertResponseDTO.success(insertedIds) // articles insérés avec succès
            );
        } catch (JAXBException | SAXException e) {
            return ResponseEntity.ok(InsertResponseDTO.error());
        }
    }
}