package fr.univrouen.rss25SB.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import fr.univrouen.rss25SB.dto.XmlErrorResponseDTO;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.*;
import fr.univrouen.rss25SB.utils.constants.XsltFilePath;

import jakarta.xml.bind.JAXBException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Contrôleur REST exposant un endpoint permettant d’afficher le détail complet
 * d’un article RSS au format HTML, selon son identifiant.
 *
 * <p>Les données sont transformées dynamiquement via des feuilles XSLT selon qu’il s’agisse
 * d’un article valide ou d’un message d’erreur.</p>
 *
 * <p><strong>URL de base :</strong> <code>/rss25SB/html/{id}</code></p>
 * <p><strong>Produit :</strong> <code>text/html</code></p>
 *
 * @author Matisse SENECHAL
 * @version 1.1
 */
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/rss25SB")
public class ItemController {

    /** Service métier permettant d'accéder aux articles stockés en base. */
    private final ItemService itemService;

    /**
     * Endpoint GET permettant d’obtenir un article au format HTML via transformation XSLT.
     * <p>
     * Si l’article est trouvé, une transformation avec {@link XsltFilePath#ITEM} est appliquée.
     * Sinon, une page d’erreur est générée avec {@link XsltFilePath#ERROR}.
     * </p>
     *
     * @param id identifiant de l’article à rechercher
     * @return {@link ResponseEntity} contenant la page HTML correspondante
     * @throws JAXBException en cas de problème de sérialisation JAXB
     */
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemByIdAsHTML(@PathVariable Long id) throws JAXBException {
        log.debug("GET /rss25SB/html/{} appelé", id);

        try {
            // Recherche l'article : si introuvable, on prépare un DTO d'erreur
            Object objectToTransform = itemService.getItemAsXmlById(id)
                .<Object>map(item -> item)
                .orElseGet(() -> new XmlErrorResponseDTO(id, "Article " + id + " introuvable. Status = ERROR"));

            // Sélection dynamique de la feuille XSLT à utiliser
            String xsltPath = (objectToTransform instanceof XmlErrorResponseDTO)
                ? "/xslt/rss25.error.xslt"
                : "/xslt/rss25.item.xslt";

            // Transformation en HTML via XSLT
            String html = XsltTransformer.marshalAndTransform(objectToTransform, xsltPath);

            return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(html);
        } catch (Exception e) {
            log.error("Erreur XSLT pour l’article {} : {}", id, e.getMessage(), e);

            // En cas d’exception, retour d’un flux d’erreur XML brut (non transformé)
            String messageErreur = "Erreur lors de la récupération d’un flux rss25SB :\n" +
                                   "L'article avec l'identifiant: " + id + " n'existe pas.";
            XmlErrorResponseDTO error = new XmlErrorResponseDTO(id, messageErreur);

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(XmlUtil.marshal(error));
        }
    }
}