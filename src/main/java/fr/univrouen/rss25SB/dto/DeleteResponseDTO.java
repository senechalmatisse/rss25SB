package fr.univrouen.rss25SB.dto;

import fr.univrouen.rss25SB.utils.constants.ResponseStatusConstants;
import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * DTO représentant une réponse XML après suppression d’un article.
 * <p>Structure attendue :</p>
 * <pre>{@code
 * <deleted>
 *     <id>42</id>
 *     <status>DELETED</status>
 * </deleted>
 * }</pre>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter @Setter
@NoArgsConstructor
@XmlRootElement(name = "deleted")
@XmlAccessorType(XmlAccessType.FIELD)
public class DeleteResponseDTO {

    /** Identifiant de l’article supprimé. */
    @XmlElement(required = true)
    private Long id;

    /** Statut de suppression (fixe : "DELETED"). */
    @XmlElement(required = true)
    private String status = ResponseStatusConstants.DELETED;

    /** Description de l'erreur rencontrée */
    @XmlElement(name = "description")
    private String description;

    /**
     * Constructeur avec ID.
     *
     * @param id identifiant de l’article supprimé
     */
    public DeleteResponseDTO(Long id) {
        this.id = id;
    }
}