package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.dto.ItemSummaryListDTO;
import fr.univrouen.rss25SB.service.ItemService;

import jakarta.xml.bind.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;

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
@RestController
@RequestMapping("/rss25SB/resume")
public class ResumeController {

    /** Service métier permettant d'accéder aux articles résumés stockés en base. */
    private final ItemService itemService;

    /**
     * Constructeur injectant le service {@link ItemService}.
     *
     * @param itemService service métier pour accéder aux résumés d’articles
     */
    public ResumeController(ItemService itemService) {
        this.itemService = itemService;
    }

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
    public ResponseEntity<String> getItemsAsXml() throws Exception {
        ItemSummaryListDTO summaryList = new ItemSummaryListDTO(itemService.getAllItemSummaries());

        JAXBContext jaxbContext = JAXBContext.newInstance(ItemSummaryListDTO.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        StringWriter writer = new StringWriter();
        marshaller.marshal(summaryList, writer);

        return ResponseEntity.ok(writer.toString());
    }
}