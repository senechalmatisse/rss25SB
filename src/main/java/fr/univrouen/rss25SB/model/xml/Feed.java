package fr.univrouen.rss25SB.model.xml;

import java.time.OffsetDateTime;
import java.util.*;
import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente un flux RSS personnalisé au format XML.
 * <p>
 * Cet objet correspond à l’élément racine {@code <feed>} du flux XML et contient des métadonnées
 * générales sur le flux ainsi qu'une liste d’articles ({@link Item}).
 * </p>
 *
 * <p>Structure attendue :</p>
 * <pre>{@code
 * <feed lang="fr" version="2.5" xmlns="http://univ.fr/rss25">
 *     <title>Flux RSS</title>
 *     <pubDate>2025-05-18T10:00:00+02:00</pubDate>
 *     <copyright>Université de Rouen</copyright>
 *     <link href="..." rel="self"/>
 *     <item>...</item>
 *     <item>...</item>
 * </feed>
 * }</pre>
 *
 * <p>Champs obligatoires :</p>
 * <ul>
 *     <li>{@code title} : titre du flux</li>
 *     <li>{@code pubDate} : date de publication du flux</li>
 *     <li>{@code copyright} : informations de droits</li>
 *     <li>{@code link} : liste de liens liés au flux</li>
 *     <li>{@code item} : liste des articles</li>
 *     <li>{@code lang}, {@code version} : attributs de métadonnées</li>
 * </ul>
 * 
 * @see Item
 * @see Link
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "feed", namespace = "http://univ.fr/rss25")
@XmlType(name = "", propOrder = {
    "title",
    "pubDate",
    "copyright",
    "link",
    "item"
})
public class Feed {

    /** Titre du flux RSS (obligatoire). */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String title;

    /** Date de publication du flux. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    @XmlSchemaType(name = "dateTime")
    private OffsetDateTime pubDate;

    /** Informations de droits d’auteur. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String copyright;

    /** Liste des liens associés au flux. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private List<Link> link;

    /** Liste des articles ({@code <item>}) contenus dans le flux. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private List<Item> item;

    /** Langue du flux (attribut XML requis). */
    @XmlAttribute(name = "lang", required = true)
    private String lang;

    /** Version du flux RSS (attribut XML requis). */
    @XmlAttribute(name = "version", required = true)
    private String version;

    public List<Link> getLink() {
        if (link == null) {
            link = new ArrayList<>();
        }
        return link;
    }

    public List<Item> getItem() {
        if (item == null) {
            item = new ArrayList<>();
        }
        return item;
    }
}