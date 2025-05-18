package fr.univrouen.rss25SB.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente une catégorie associée à un article dans un flux RSS personnalisé.
 * <p>
 * Cette classe est utilisée pour modéliser les éléments {@code <category>} dans JAXB,
 * avec un seul attribut obligatoire : {@code term}.
 * </p>
 *
 * <p>Exemple XML généré :</p>
 * <pre>{@code
 * <category term="Technologie"/>
 * }</pre>
 *
 * <p><b>Attributs :</b></p>
 * <ul>
 *     <li>{@code term} : libellé de la catégorie (obligatoire)</li>
 * </ul>
 * 
 * Cette classe peut être utilisée dans des listes de catégories associées à un {@code <item>}.
 * 
 * @see fr.univrouen.rss25SB.model.xml.Item
 * @version 1.0
 * @author Matisse SENECHAL
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {

    /** Libellé de la catégorie (obligatoire). */
    @XmlAttribute(name = "term", required = true)
    private String term;
}