package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.dto.*;
import fr.univrouen.rss25SB.model.xml.Item;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.*;
import fr.univrouen.rss25SB.utils.constants.XsltFilePath;
import jakarta.xml.bind.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Contrôleur REST permettant d’exposer plusieurs endpoints pour obtenir une
 * version résumée ou complète des articles RSS au format XML ou HTML.
 *
 * <p>
 * Ce contrôleur utilise le service {@link ItemService} pour récupérer les données,
 * le composant {@link XmlUtil} pour la sérialisation JAXB en XML,
 * et {@link XsltTransformer} pour générer dynamiquement du HTML à partir d’un XSLT.
 * </p>
 *
 * <p>Les articles retournés peuvent être :</p>
 * <ul>
 *   <li>Résumés (id, date, guid) au format XML ou HTML</li>
 *   <li>Complet (type {@link Item}) au format XML</li>
 * </ul>
 *
 * <p><b>URL de base :</b> <code>/rss25SB/resume</code></p>
 *
 * @author Matisse SENECHAL
 * @version 2.0
 */
@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/rss25SB/resume")
public class ResumeController {

    /** Service métier permettant d'accéder aux articles résumés stockés en base. */
    private final ItemService itemService;

    /**
     * Endpoint GET exposant un flux XML contenant la liste synthétique des articles RSS.
     *
     * @return {@link ResponseEntity} contenant une chaîne XML de la liste résumée
     * @throws JAXBException en cas d’erreur de sérialisation JAXB
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/xml</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> application/xml</p>
     */
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getItemsAsXML() throws JAXBException {
        log.debug("GET /rss25SB/resume/xml appelé");

        // Sérialisation en XML des résumés d’articles
        String xml = XmlUtil.marshal(
            new ItemSummaryListDTO(itemService.getAllItemSummaries())
        );

        return ResponseEntity.ok(xml);
    }

    /**
     * Endpoint GET exposant la liste synthétique des articles RSS au format HTML.
     * <p>
     * Ce flux est généré dynamiquement à partir de la version XML et transformé via un XSLT.
     * En cas d’erreur, une page HTML d’erreur est produite à partir d’un {@link XmlErrorResponseDTO}.
     * </p>
     *
     * @return {@link ResponseEntity} contenant le HTML rendu ou une erreur transformée
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/html</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> text/html</p>
     */
    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemsAsHTML() throws JAXBException {
        log.debug("GET /rss25SB/resume/html appelé");

        try {
            // Récupère la liste synthétique des articles
            ItemSummaryListDTO dto = new ItemSummaryListDTO(itemService.getAllItemSummaries());

            // Transforme la liste en HTML via XSLT
            String html = XsltTransformer.marshalAndTransform(dto, XsltFilePath.LIST.getPath());
            return ResponseEntity.ok(html);
        } catch (Exception e) {
            log.error("Erreur XSLT liste résumée : {}", e.getMessage());

            // Prépare une réponse d’erreur en XML, puis transforme en HTML
            String messageErreur = "Erreur lors de la récupération de la liste résumée";
            XmlErrorResponseDTO error = new XmlErrorResponseDTO(null, messageErreur);

            try {
                String html = XsltTransformer.marshalAndTransform(error, XsltFilePath.ERROR.getPath());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(html);
            } catch (Exception ex) {
                // Erreur critique secondaire (par exemple : problème avec le XSLT d'erreur lui-même)
                log.error("Erreur critique lors de la transformation XSLT d’erreur : {}", ex.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("<html><body><p>Erreur interne : " + ex.getMessage() + "</p></body></html>");
            }
        }
    }

    /**
     * Endpoint GET permettant d’obtenir un article complet au format XML selon son identifiant.
     * <p>
     * Si l’article est trouvé, il est retourné sous forme XML.
     * Sinon, un {@link XmlErrorResponseDTO} est retourné avec un statut "ERROR".
     * </p>
     *
     * @param id identifiant de l’article à rechercher
     * @return {@link ResponseEntity} contenant l’article ou une erreur au format XML
     * @throws JAXBException en cas de problème lors de la sérialisation
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/xml/{id}</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> application/xml</p>
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getItemByIdAsXML(@PathVariable Long id) throws JAXBException {
        log.debug("GET /rss25SB/resume/xml/{} appelé", id);

        // Recherche l’article par son identifiant
        Optional<Item> itemOptional = itemService.getItemAsXmlById(id);

        if (itemOptional.isPresent()) {
            // Article trouvé : sérialisation en XML
            String xml = XmlUtil.marshal(itemOptional.get());
            return ResponseEntity.ok(xml);
        } else {
            // Article introuvable : retourne un message d’erreur XML
            String messageErreur = "Erreur lors de la récupération d’un flux rss25SB :\n" +
                                   "L'article avec l'identifiant: " + id + " n'existe pas.";
            log.warn(messageErreur);

            XmlErrorResponseDTO error = new XmlErrorResponseDTO(id, messageErreur);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                 .body(XmlUtil.marshal(error));
        }
    }
}