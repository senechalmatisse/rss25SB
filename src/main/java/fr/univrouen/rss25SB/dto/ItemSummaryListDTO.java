package fr.univrouen.rss25SB.dto;

import jakarta.xml.bind.annotation.*;
import lombok.*;

import java.util.List;

/**
 * DTO représentant une liste de résumés d’articles RSS.
 * <p>
 * Ce wrapper est utilisé pour générer une structure XML contenant plusieurs éléments {@link ItemSummaryDTO}.
 * Il est sérialisé avec JAXB sous la forme :
 * <pre>
 * {@code
 * <items>
 *     <item>...</item>
 *     <item>...</item>
 * </items>
 * }
 * </pre>
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 * @see ItemSummaryDTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@XmlRootElement(name = "items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemSummaryListDTO {

    /**
     * Liste des articles au format résumé.
     * Chaque élément correspond à un objet {@link ItemSummaryDTO} sérialisé en balise {@code <item>}.
     */
    @XmlElement(name = "item")
    private List<ItemSummaryDTO> items;
}