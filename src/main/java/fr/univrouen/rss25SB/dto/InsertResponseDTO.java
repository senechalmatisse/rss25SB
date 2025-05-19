package fr.univrouen.rss25SB.dto;

import java.util.List;

import fr.univrouen.rss25SB.utils.constants.ResponseStatusConstants;
import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * DTO utilisé pour représenter la réponse à une insertion de flux RSS.
 * <p>
 * Cette réponse est retournée au format XML après appel à l’endpoint
 * <code>/rss25SB/insert</code>. Elle contient :
 * </p>
 * <ul>
 *     <li>Une liste optionnelle d’identifiants d’articles insérés</li>
 *     <li>Un statut indiquant le succès ou l’échec de l’insertion</li>
 * </ul>
 *
 * <p>Structure XML attendue :</p>
 * <pre>{@code
 * <inserted>
 *     <ids>
 *         <id>1</id>
 *         <id>2</id>
 *     </ids>
 *     <status>INSERTED</status>
 * </inserted>
 * }</pre>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@AllArgsConstructor
@Getter @Setter @NoArgsConstructor
@XmlRootElement(name = "inserted")
@XmlAccessorType(XmlAccessType.FIELD)
public class InsertResponseDTO {

    /**
     * Liste des identifiants des articles insérés.
     * Enveloppée dans un élément XML {@code <ids>}.
     */
    @XmlElementWrapper(name = "ids")
    @XmlElement(name = "id")
    private List<Long> id;

    /**
     * Statut de l’opération d’insertion : "inserted" ou "error".
     * Toujours présent dans la réponse.
     */
    @XmlElement(name = "status", required = true)
    private String status;

    /** Description de l'erreur rencontrée */
    @XmlElement(name = "description")
    private String description;

    /**
     * Fabrique une réponse de succès contenant la liste des identifiants insérés.
     *
     * @param ids liste des identifiants des articles insérés
     * @return un objet {@link InsertResponseDTO} avec statut {@code "inserted"}
     */
    public static InsertResponseDTO success(List<Long> ids) {
        InsertResponseDTO dto = new InsertResponseDTO();
        dto.setStatus(ResponseStatusConstants.INSERTED);
        dto.setId(ids);
        return dto;
    }

    /**
     * Fabrique une réponse d’échec d’insertion.
     *
     * @param descritpion descritpion de la première erreur detectée
     * 
     * @return un objet {@link XmlErrorResponseDTO}
     *         avec statut {@code "error"} et une description de l'erreur
     */
    public static InsertResponseDTO error(String description) {
        InsertResponseDTO dto = new InsertResponseDTO();
        dto.setStatus(ResponseStatusConstants.ERROR);
        dto.setDescription(description);
        return dto;
    }
}