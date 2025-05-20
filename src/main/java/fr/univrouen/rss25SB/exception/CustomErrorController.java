package fr.univrouen.rss25SB.exception;

import fr.univrouen.rss25SB.dto.XmlErrorResponseDTO;
import fr.univrouen.rss25SB.utils.XmlUtil;
import fr.univrouen.rss25SB.utils.constants.ResponseStatusConstants;
import jakarta.xml.bind.JAXBException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur personnalisé de gestion des erreurs globales dans l'application.
 * <p>
 * Cette classe remplace le comportement par défaut de Spring Boot pour les erreurs HTTP
 * (ex : 400, 404, 500) en fournissant une réponse XML structurée au format rss25SB.
 * </p>
 *
 * <p>Les erreurs gérées incluent :</p>
 * <ul>
 *   <li>404 : Ressource non trouvée</li>
 *   <li>400 : Paramètre invalide</li>
 *   <li>500 : Erreur interne du serveur</li>
 * </ul>
 *
 * <p>La réponse est encapsulée dans un objet {@link XmlErrorResponseDTO} et sérialisée en XML
 * grâce à {@link XmlUtil}.</p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Controller
@Slf4j
public class CustomErrorController implements ErrorController {

    /** Réponse XML par défaut en cas d’échec de la sérialisation XML. */
    private static final String DEFAULT_XML_ERROR =
        "<error><status>ERROR</status><description>Erreur interne</description></error>";

    /**
     * Point d’entrée principal pour la gestion des erreurs.
     * <p>
     * Ce handler est déclenché automatiquement par Spring pour toute erreur non gérée
     * ou toute tentative d'accès à une ressource inexistante.
     * </p>
     *
     * @param request l’objet {@link HttpServletRequest} contenant les détails de l’erreur
     * @return une {@link ResponseEntity} contenant une réponse XML avec le code et message d’erreur
     */
    @RequestMapping(value = "/error", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        int status = getStatusCode(request);
        String uri = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        String message = String.valueOf(request.getAttribute(RequestDispatcher.ERROR_MESSAGE));

        // Création de l’objet de réponse XML personnalisé
        XmlErrorResponseDTO error = new XmlErrorResponseDTO();
        error.setStatus(ResponseStatusConstants.ERROR);
        error.setDescription(String.format(
                "Erreur %d : ressource non trouvée ou invalide [%s]. Message : %s",
                status, uri, message));

        try {
            return ResponseEntity.status(status).body(XmlUtil.marshal(error));
        } catch (JAXBException e) {
            log.error("Erreur de sérialisation XML dans /error : {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(DEFAULT_XML_ERROR);
        }
    }

    /**
     * Extrait le code de statut HTTP à partir de la requête.
     * <p>
     * Utilise l'attribut {@code javax.servlet.error.status_code} pour détecter le code HTTP original.
     *
     * @param request l’objet {@link HttpServletRequest}
     * @return le code d’erreur HTTP ou 500 si inconnu
     */
    private int getStatusCode(HttpServletRequest request) {
        Object statusCode = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        return (statusCode instanceof Integer)
            ? (Integer) statusCode
            : HttpStatus.INTERNAL_SERVER_ERROR.value(); // Valeur par défaut si erreur inconnue
    }
}