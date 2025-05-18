package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.dto.*;
import fr.univrouen.rss25SB.model.xml.Item;
import fr.univrouen.rss25SB.service.ItemService;

import jakarta.xml.bind.*;
import lombok.AllArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;
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

    /** Moteur de template Thymeleaf utilisé pour générer la vue HTML des articles. */
    private final TemplateEngine templateEngine;

    /**
     * Endpoint GET exposant un flux XML contenant la liste synthétique des articles RSS.
     * Chaque élément de la liste ne contient que l'identifiant, la date et le GUID.
     *
     * @return {@link ResponseEntity} contenant la représentation XML de la liste résumée
     * @throws Exception en cas d’erreur lors de la sérialisation JAXB
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/xml</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> application/xml</p>
     */
    @GetMapping(value = "/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getItemsAsXML() throws Exception {
        ItemSummaryListDTO summaryList = new ItemSummaryListDTO(itemService.getAllItemSummaries());

        JAXBContext jaxbContext = JAXBContext.newInstance(ItemSummaryListDTO.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(summaryList, writer);

        return ResponseEntity.ok(writer.toString());
    }

    /**
     * Endpoint GET exposant la liste synthétique des articles RSS au format HTML.
     * <p>Génère une vue Thymeleaf affichant les articles sous forme tabulaire ou autre mise en page HTML.</p>
     *
     * @return {@link ResponseEntity} contenant le HTML rendu par le template {@code items.html}
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/html</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> text/html</p>
     */
    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemsAsHTML() {
        List<ItemSummaryDTO> items = itemService.getAllItemSummaries();

        Context context = new Context();
        context.setVariable("items", items);

        String htmlContent = templateEngine.process("items", context);
        return ResponseEntity.ok(htmlContent);
    }

    /**
     * Endpoint GET permettant d’obtenir un article complet au format XML selon son identifiant.
     * <p>
     * Si l’article est trouvé, un objet {@link Item} est retourné.
     * Sinon, une réponse XML de type {@link XmlErrorResponseDTO} est générée.
     * </p>
     *
     * @param id identifiant de l’article à rechercher
     * @return {@link ResponseEntity} contenant l’article au format XML ou un message d’erreur XML
     * @throws Exception en cas d’erreur de sérialisation JAXB
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/xml/{id}</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> application/xml</p>
     */
    @GetMapping(value = "/xml/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> getItemByIdAsXML(@PathVariable Long id) throws Exception {
        Optional<Item> itemOptional = itemService.getItemAsXmlById(id);
    
        JAXBContext context;
        Object toMarshal;
    
        if (itemOptional.isPresent()) {
            context = JAXBContext.newInstance(Item.class);
            toMarshal = itemOptional.get();
        } else {
            context = JAXBContext.newInstance(XmlErrorResponseDTO.class);
            toMarshal = new XmlErrorResponseDTO(id);
        }
    
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(toMarshal, writer);
    
        return ResponseEntity.ok(writer.toString());
    }    
}