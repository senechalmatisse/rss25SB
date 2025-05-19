package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.dto.*;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.XmlUtil;
import jakarta.xml.bind.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST permettant de supprimer un article RSS à partir de son identifiant.
 * <p>
 * Ce contrôleur expose un endpoint HTTP {@code DELETE} accessible via :
 * <pre>{@code
 * /rss25SB/delete/{id}
 * }</pre>
 * Il retourne une réponse au format XML indiquant si l'opération de suppression a réussi ou non.
 * </p>
 *
 * <p>Structure XML en cas de succès :</p>
 * <pre>{@code
 * <deleted>
 *   <id>123</id>
 *   <status>DELETED</status>
 * </deleted>
 * }</pre>
 *
 * <p>Structure XML en cas d'échec :</p>
 * <pre>{@code
 * <error>
 *   <id>123</id>
 *   <status>ERROR</status>
 * </error>
 * }</pre>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 * @see DeleteResponseDTO
 * @see XmlErrorResponseDTO
 * @see ItemService
 * @see XmlUtil
 */
@RestController
@RequestMapping("/rss25SB")
@RequiredArgsConstructor
public class DeleteController {

    /** Service métier responsable de la sauvegarde et de la vérification des articles. */
    private final ItemService itemService;

    /**
     * Endpoint HTTP DELETE permettant de supprimer un article à partir de son identifiant.
     *
     * @param id identifiant de l’article à supprimer
     * @return réponse HTTP contenant un flux XML :
     *         <ul>
     *             <li>{@code <deleted>} si la suppression a réussi</li>
     *             <li>{@code <error>} si aucun article correspondant n’a été trouvé</li>
     *         </ul>
     * @throws JAXBException si une erreur survient lors de la sérialisation XML
     *
     * <p><b>Exemple d’appel :</b> {@code DELETE /rss25SB/delete/12}</p>
     * <p><b>Type de retour :</b> {@code application/xml}
     */
    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<String> deleteItem(@PathVariable Long id) throws JAXBException {
        try {
            boolean deleted = itemService.deleteItemById(id);

            Object response = deleted
                    ? new DeleteResponseDTO(id)
                    : new XmlErrorResponseDTO(id);

            String xml = XmlUtil.marshal(response);

            return deleted
                    ? ResponseEntity.ok(xml)
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body(xml);
        } catch (JAXBException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<error><id>" + id + "</id><status>ERROR</status></error>");
        }
    }
}