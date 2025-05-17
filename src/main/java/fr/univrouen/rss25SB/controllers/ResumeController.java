package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.dto.ItemSummaryDTO;
import fr.univrouen.rss25SB.dto.ItemSummaryListDTO;
import fr.univrouen.rss25SB.service.ItemService;

import jakarta.xml.bind.*;
import lombok.AllArgsConstructor;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.StringWriter;
import java.util.List;

/**
 * Controller REST permettant d’exposer un endpoint pour obtenir
 * une version résumée de la liste des articles RSS au format XML.
 * <p>
 * Chaque article retourné dans le flux ne contient que :
 * <ul>
 *     <li><b>id</b> : identifiant unique de l’article (généré automatiquement)</li>
 *     <li><b>date</b> : date de publication ou mise à jour</li>
 *     <li><b>guid</b> : identifiant unique global (URL de l’article)</li>
 * </ul>
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 * @see fr.univrouen.rss25SB.dto.ItemSummaryDTO
 * @see fr.univrouen.rss25SB.service.ItemService
 */
@AllArgsConstructor
@RestController
@RequestMapping("/rss25SB/resume")
public class ResumeController {

    /** Service métier permettant d'accéder aux articles résumés stockés en base. */
    private final ItemService itemService;

    private final TemplateEngine templateEngine;

    /**
     * Endpoint GET exposant un flux XML contenant la liste synthétique des articles RSS.
     * Chaque élément de la liste ne contient que l'identifiant, la date et le GUID.
     *
     * @return {@link ResponseEntity} contenant la représentation XML de la liste résumée
     * @throws Exception en cas d’erreur lors de la sérialisation XML
     *
     * <p><b>Exemple d’URL :</b> <code>/rss25SB/resume/xml</code></p>
     * <p><b>Méthode HTTP :</b> GET</p>
     * <p><b>MediaType :</b> application/xml</p>
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
     * Affiche la liste synthétique des articles au format HTML.
     * 
     * @param model modèle Spring pour la vue
     * @return nom de la vue Thymeleaf
     */
    @GetMapping(value = "/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemsAsHTML() {
        List<ItemSummaryDTO> items = itemService.getAllItemSummaries();

        Context context = new Context();
        context.setVariable("items", items);

        String htmlContent = templateEngine.process("items", context);
        return ResponseEntity.ok(htmlContent);
    }
}