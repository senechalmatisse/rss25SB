package fr.univrouen.rss25SB.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente un lien associé au flux RSS ou à un article.
 * <p>
 * Un lien est décrit à l’aide de trois attributs XML :
 * <ul>
 *     <li>{@code rel} : type de relation (ex. "self", "alternate")</li>
 *     <li>{@code type} : type MIME de la ressource liée (ex. "application/xml")</li>
 *     <li>{@code href} : URL de la ressource</li>
 * </ul>
 * </p>
 *
 * <p>Exemple XML généré :</p>
 * <pre>{@code
 * <link rel="self" type="application/xml" href="http://example.com/feed.xml"/>
 * }</pre>
 *
 * Cette classe est utilisée dans les flux RSS enrichis (élément {@code <feed>}) pour indiquer
 * des liens vers des ressources utiles (auto-référence, page HTML, etc.).
 * 
 * @version 1.0
 * @author Matisse SENECHAL
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

    /**
     * Relation du lien (ex. "self", "alternate", "related").
     * Attribut requis.
     */
    @XmlAttribute(name = "rel", required = true)
    private String rel;

    /**
     * Type MIME du contenu lié (ex. "application/xml", "text/html").
     * Attribut requis.
     */
    @XmlAttribute(name = "type", required = true)
    private String type;

    /** URL de la ressource liée. Attribut requis. */
    @XmlAttribute(name = "href", required = true)
    private String href;
}