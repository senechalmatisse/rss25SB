package fr.univrouen.rss25SB.dto;

import fr.univrouen.rss25SB.utils.constants.ResponseStatusConstants;
import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * DTO représentant une réponse d’erreur au format XML pour un article introuvable.
 * <p>
 * Ce type de réponse est retourné lorsqu’un identifiant d’article donné ne correspond
 * à aucun article en base. La réponse prend la forme suivante :
 * </p>
 *
 * <pre>{@code
 * <error>
 *     <id>123</id>
 *     <status>ERROR</status>
 * </error>
 * }</pre>
 *
 * <p>Ce DTO est sérialisé avec JAXB et utilisé dans les endpoints XML.</p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter @Setter
@NoArgsConstructor
@XmlRootElement(name = "error")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlErrorResponseDTO {

    /** Identifiant fourni dans la requête qui a causé l’erreur.*/
    @XmlElement(required = true)
    private Long id;

    /** Statut de la réponse d’erreur (par défaut : "ERROR"). */
    @XmlElement(required = true)
    private String status = ResponseStatusConstants.ERROR;

    /**
     * Constructeur avec identifiant.
     *
     * @param id identifiant de l’article non trouvé
     */
    public XmlErrorResponseDTO(Long id) {
        this.id = id;
    }
}