package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.dto.*;
import fr.univrouen.rss25SB.model.xml.Item;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.*;
import jakarta.xml.bind.*;
import lombok.AllArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Contrôleur REST permettant d’exposer plusieurs endpoints pour obtenir une
 * version résumée ou complète des articles RSS au format XML ou HTML.
 * <p>
 * Les articles retournés peuvent être :
 * <ul>
 *   <li>Résumés (id, date, guid) au format XML ou HTML</li>
 *   <li>Complet (type {@link Item}) au format XML</li>
 * </ul>
 * </p>
 *
 * <p>URL de base : <code>/rss25SB/resume</code></p>
 *
 * @author Matisse SENECHAL
 * @version 2.0
 */
@AllArgsConstructor
@RestController
@RequestMapping("/rss25SB/resume")
public class ResumeController {

    /** Service métier permettant d'accéder aux articles résumés stockés en base. */
    private final ItemService itemService;

    /** Moteur de rendu HTML pour générer dynamiquement les vues (via Thymeleaf). */
    private final HtmlRenderer htmlRenderer;

    /**
     * Endpoint GET exposant un flux XML contenant la liste synthétique des articles RSS.
     * <p>
     * Chaque article retourné contient uniquement trois champs : id, date, guid.
     * </p>
     *
     * @return {@link ResponseEntity} contenant la représentation XML de la liste résumée
     * @throws JAXBException en cas d’erreur de sérialisation JAXB
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/xml</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> application/xml</p>
     */
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getItemsAsXML() throws JAXBException {
        // Récupère la liste résumée des articles
        ItemSummaryListDTO summaryList = new ItemSummaryListDTO(
            itemService.getAllItemSummaries()
        );

        // Sérialisation XML avec JAXB via utilitaire
        String xml = XmlUtil.marshal(summaryList);

        return ResponseEntity.ok(xml);
    }

    /**
     * Endpoint GET exposant la liste synthétique des articles RSS au format HTML.
     * <p>
     * Génère dynamiquement une page HTML (via un template "items") contenant tous les articles résumés.
     * </p>
     *
     * @return {@link ResponseEntity} contenant le HTML rendu par le template {@code items.html}
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/html</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> text/html</p>
     */
    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemsAsHTML() {
        // Récupération des articles résumés
        List<ItemSummaryDTO> items = itemService.getAllItemSummaries();

        // Rendu du HTML avec insertion des données dans le template "items"
        String html = htmlRenderer.render(
            "items",
            Map.of("items", items)
        );

        return ResponseEntity.ok(html);
    }

    /**
     * Endpoint GET permettant d’obtenir un article complet au format XML selon son identifiant.
     * <p>
     * Si l’article est trouvé, un objet {@link Item} est retourné (sérialisé en XML).
     * Sinon, une réponse XML de type {@link XmlErrorResponseDTO} est générée avec l’ID et un statut "ERROR".
     * </p>
     *
     * @param id identifiant de l’article à rechercher
     * @return {@link ResponseEntity} contenant l’article au format XML ou une erreur
     * @throws JAXBException en cas d’erreur lors de la sérialisation XML
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/xml/{id}</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> application/xml</p>
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getItemByIdAsXML(@PathVariable Long id) throws JAXBException {
        // Recherche de l’article par ID
        Optional<Item> itemOptional = itemService.getItemAsXmlById(id);

        Object toMarshal;
        if (itemOptional.isPresent()) {     // Si trouvé, on sérialise l’article  
            toMarshal = itemOptional.get();
        } else {                            // sinon, une erreur XML
            toMarshal = new XmlErrorResponseDTO(id);
        }

        String xml = XmlUtil.marshal(toMarshal);
        return ResponseEntity.ok(xml);
    }
}