package fr.univrouen.rss25SB.dto;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * DTO représentant un résumé d’article RSS destiné à l’export XML.
 * <p>
 * Ce résumé contient uniquement :
 * <ul>
 *     <li><b>id</b> : identifiant unique de l’article</li>
 *     <li><b>guid</b> : identifiant global unique (généralement une URL)</li>
 *     <li><b>date</b> : date de publication ou de mise à jour</li>
 * </ul>
 * </p>
 *
 * Cette classe est utilisée pour générer un flux XML avec JAXB.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemSummaryDTO {

    /** Identifiant unique de l’article. */
    private Long id;

    /** Titre de l’article. */
    private String title;

    /** GUID (Global Unique Identifier) de l’article. */
    private String guid;

    /** Date de publication ou de mise à jour de l’article. */
    private String date;
}