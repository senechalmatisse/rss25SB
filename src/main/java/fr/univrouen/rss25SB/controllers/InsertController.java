package fr.univrouen.rss25SB.controllers;

import fr.univrouen.rss25SB.converter.FluxSourceSelector;
import fr.univrouen.rss25SB.dto.InsertResponseDTO;
import fr.univrouen.rss25SB.model.db.ItemEntity;
import fr.univrouen.rss25SB.model.xml.*;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.*;
import fr.univrouen.rss25SB.utils.constants.*;
import jakarta.xml.bind.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Contrôleur REST responsable de la gestion de l’insertion de flux RSS au format XML ou HTML.
 * <p>
 * Ce contrôleur expose les fonctionnalités suivantes :
 * </p>
 * <ul>
 *     <li><b>Insertion XML</b> : endpoint <code>POST /rss25SB/insert</code> pour recevoir un flux XML brut et l’insérer après validation XSD</li>
 *     <li><b>Affichage formulaire</b> : endpoint <code>GET /rss25SB/insert</code> pour afficher la page HTML permettant d’uploader un fichier XML</li>
 *     <li><b>Insertion via formulaire</b> : endpoint <code>POST /rss25SB/insert/html</code> qui traite un fichier XML envoyé via formulaire et affiche le résultat en HTML</li>
 * </ul>
 *
 * <p>
 * Ce contrôleur s’appuie sur :
 * </p>
 * <ul>
 *     <li>{@link ItemService} pour effectuer l’insertion en base</li>
 *     <li>{@link HtmlRenderer} pour générer des vues HTML dynamiques avec Thymeleaf</li>
 *     <li>{@link FluxSourceSelector} pour convertir les flux XML externes non conformes</li>
 *     <li>{@link XsltTransformer} pour transformer les réponses XML (type {@link InsertResponseDTO}) en pages HTML</li>
 * </ul>
 *
 * <p>
 * En cas de flux invalide ou de doublons, des messages d’erreur détaillés sont retournés,
 * et les conversions ou insertions sont loguées.
 * </p>
 *
 * <p><b>Base d’URL : </b><code>/rss25SB</code>
 *
 * @author Matisse SENECHAL
 * @version 3.0
 * @see ItemService
 * @see HtmlRenderer
 * @see FluxSourceSelector
 * @see InsertResponseDTO
 * @see XsltTransformer
 */
@RestController
@RequestMapping("/rss25SB")
@RequiredArgsConstructor
@Slf4j
public class InsertController {

    /** Service métier permettant d'accéder aux articles stockés en base. */
    private final ItemService itemService;

    /** Moteur de rendu HTML (basé sur Thymeleaf) pour afficher les vues HTML. */
    private final HtmlRenderer htmlRenderer;

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
        log.debug("POST /rss25SB/insert appelé, payload length={}", xmlContent.length());
        StringBuilder messageErreur = new StringBuilder("Erreur lors de la soumission d’un flux XML :\n");
        Feed feed = tryDeserializeOrConvert(xmlContent, messageErreur);

        if (feed == null) {
            log.warn("Échec de désérialisation/conversion : {}", messageErreur);
            return ResponseEntity.badRequest().body(InsertResponseDTO.error(messageErreur.toString()));
        }

        return tryInsertItems(feed, messageErreur);
    }

    /**
     * Tente de désérialiser un flux XML en objet {@link Feed} après validation via XSD.
     * Si la validation échoue, essaie d'appliquer un convertisseur de source externe
     * (via {@link FluxSourceSelector}).
     *
     * @param xmlContent Le contenu XML brut à valider et désérialiser
     * @param errorMsg Accumulateur de messages d’erreur à enrichir
     * @return Une instance de {@link Feed} si valide ou convertie, sinon {@code null}
     */
    private Feed tryDeserializeOrConvert(String xmlContent, StringBuilder errorMsg) {
        try {
            log.debug("Tentative de désérialisation avec validation XSD depuis {}", Constants.XSD_PATH);
            return XmlUtil.unmarshal(xmlContent, Feed.class, Constants.XSD_PATH);
        } catch (JAXBException | SAXException e) {
            log.warn("Le flux n'est pas conforme au XSD rss25SB : {}", e.getMessage());
            errorMsg.append("- Flux non valide au format rss25SB. Tentative de conversion automatique...\n");

            try {
                Feed converted = fluxSourceSelector.convert(xmlContent);
                log.info("Conversion réussie depuis une source externe vers rss25SB");
                return converted;
            } catch (UnsupportedOperationException ex) {
                log.warn("Aucune stratégie de conversion disponible : {}", ex.getMessage());
                errorMsg.append("- Flux non reconnu : ").append(ex.getMessage());
            } catch (Exception ex) {
                log.error("Erreur critique pendant la conversion : {}", ex.getMessage());
                errorMsg.append("- Échec de conversion : ").append(XmlUtil.extractFirstErrorMessage(ex));
            }
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la désérialisation : {}", e.getMessage(), e);
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
            List<Item> itemsToInsert = feed.getItem().stream()
                .filter(item -> !itemService.itemExists(item.getGuid()))
                .toList();

            if (itemsToInsert.isEmpty()) {
                errorMsg.append("- Aucun article inséré : tous déjà présents.");
                log.info("Aucun nouvel article inséré");
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                                     .body(InsertResponseDTO.error(errorMsg.toString()));
            }

            // Phase de validation : on vérifie que tous les items peuvent être convertis en entité
            List<ItemEntity> entities = new ArrayList<>();
            for (Item item : itemsToInsert) {
                try {
                    entities.add(ItemMapper.toEntity(item));
                } catch (Exception e) {
                    log.warn("Article invalide détecté avant insertion : {}", e.getMessage());
                    errorMsg.append("- Un ou plusieurs articles sont invalides. Aucune insertion effectuée.\n");
                    errorMsg.append("Article GUID=").append(item.getGuid()).append(" : ").append(e.getMessage());
                    return ResponseEntity.badRequest().body(InsertResponseDTO.error(errorMsg.toString()));
                }
            }

            // Si tout est valide : insertion effective
            List<Long> insertedIds = itemService.saveAllItems(entities);
            return ResponseEntity.status(HttpStatus.CREATED)
                                 .body(InsertResponseDTO.success(insertedIds));
        } catch (Exception e) {
            log.error("Erreur lors de l'insertion en base : {}", e.getMessage(), e);
            errorMsg.append("- Erreur lors de la sauvegarde : ")
                    .append(XmlUtil.extractFirstErrorMessage(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(InsertResponseDTO.error(errorMsg.toString()));
        }
    }

    /**
     * Endpoint GET exposant la page HTML de formulaire pour téléverser un fichier XML.
     * <p>
     * Cette page permet à l'utilisateur de sélectionner un fichier XML local et
     * de le soumettre à l’endpoint {@code /rss25SB/insert/html}.
     * Le rendu HTML est généré dynamiquement à l’aide de Thymeleaf via {@link HtmlRenderer}.
     * </p>
     *
     * @return une réponse HTTP contenant la page HTML du formulaire
     */
    @GetMapping(value = "/insert", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> showUploadForm() {
        log.debug("Affichage de la page de formulaire d'insertion (GET /rss25SB/insert)");

        // Préparation du modèle pour le template Thymeleaf
        Map<String, Object> model = new HashMap<>();
        model.put("title", "Upload XML");

        String html = htmlRenderer.render("upload", model);
        log.debug("Formulaire HTML généré ({} caractères)", html.length());

        return ResponseEntity.ok(html);
    }

    /**
     * Endpoint POST permettant de téléverser un fichier XML via un formulaire HTML
     * et d’afficher le résultat de l’insertion au format HTML (via transformation XSLT).
     *
     * <p>
     * Le traitement inclut :
     * <ul>
     *   <li>La lecture du fichier envoyé en `multipart/form-data`</li>
     *   <li>La validation du flux XML selon le XSD `rss25.xsd`</li>
     *   <li>L’invocation du endpoint `/insert` pour traitement métier</li>
     *   <li>La transformation du {@link InsertResponseDTO} en page HTML via XSLT</li>
     * </ul>
     * </p>
     *
     * @param file le fichier XML envoyé depuis le formulaire
     * @return page HTML générée indiquant le résultat (succès ou erreur) de l’insertion
     */
    @PostMapping(
        value = "/insert/html",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.TEXT_HTML_VALUE
    )
    public ResponseEntity<String> insertRssAsHtml(@RequestParam("file") MultipartFile file) {
        try {
            log.debug("Fichier reçu via formulaire : nom={}, taille={} octets",
                  file.getOriginalFilename(), file.getSize());

            // Lecture du fichier XML
            String xmlContent = new String(file.getBytes(), StandardCharsets.UTF_8);
            log.debug("Lecture du contenu XML réussie ({} caractères)", xmlContent.length());

            // Appel à l'endpoint technique XML (insertRssFeed)
            ResponseEntity<InsertResponseDTO> response = insertRssFeed(xmlContent);
            InsertResponseDTO dto = response.getBody();

            log.debug("Réponse obtenue après insertion XML : statut={}", dto.getStatus());

            // Transformation XSLT -> HTML
            String html = XsltTransformer.marshalAndTransform(dto, XsltFilePath.INSERT.getPath());
            log.debug("Transformation XSLT réussie ({} caractères HTML)", html.length());

            return ResponseEntity.ok(html);
        } catch (Exception e) {
            log.error("Erreur lecture ou transformation fichier XML : {}", e.getMessage(), e);

            // Génération d'un DTO d’erreur et tentative de transformation
            InsertResponseDTO dto = InsertResponseDTO.error("Erreur lors du traitement du fichier : " + e.getMessage());
            try {
                String html = XsltTransformer.marshalAndTransform(dto, XsltFilePath.INSERT.getPath());
                return ResponseEntity.badRequest().body(html);
            } catch (Exception ex) {
                log.error("Erreur critique lors de la transformation XSLT : {}", ex.getMessage(), ex);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("<html><body><p>Erreur critique XSLT : " + ex.getMessage() + "</p></body></html>");
            }
        }
    }
}