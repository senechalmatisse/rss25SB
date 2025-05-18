package fr.univrouen.rss25SB.model.xml;

import java.time.OffsetDateTime;
import java.util.*;

import fr.univrouen.rss25SB.model.adapter.OffsetDateTimeXmlAdapter;
import jakarta.xml.bind.annotation.*;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import lombok.*;

/**
 * Représente un élément {@code <item>} dans un flux RSS personnalisé au format XML.
 * <p>
 * Cette classe est utilisée par JAXB pour la (dé)sérialisation XML, selon le schéma RSS 2.5 défini avec l’espace de noms {@code http://univ.fr/rss25}.
 * Elle inclut plusieurs sous-éléments, dont :
 * <ul>
 *     <li>{@code guid} : identifiant global unique de l’article (type URI)</li>
 *     <li>{@code title} : titre de l’article</li>
 *     <li>{@code category} : catégories associées à l’article</li>
 *     <li>{@code published} : date de publication</li>
 *     <li>{@code updated} : date de mise à jour</li>
 *     <li>{@code image} : image liée à l’article</li>
 *     <li>{@code content} : contenu principal</li>
 *     <li>{@code author} ou {@code contributor} : personnes associées à l’article</li>
 * </ul>
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.1
 */
@Getter @Setter
@XmlRootElement(name = "item", namespace = "http://univ.fr/rss25")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "guid",
    "title",
    "category",
    "published",
    "updated",
    "image",
    "content",
    "authorOrContributor"
})
public class Item {

    /** Identifiant global unique de l’article. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    @XmlSchemaType(name = "anyURI")
    private String guid;

    /** Titre de l’article. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private String title;

    /** Liste des catégories associées à l’article. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private List<Category> category;

    /** Date de publication de l’article. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    @XmlJavaTypeAdapter(OffsetDateTimeXmlAdapter.class)
    private OffsetDateTime published;

    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    @XmlJavaTypeAdapter(OffsetDateTimeXmlAdapter.class)
    private OffsetDateTime updated;

    /** Image liée à l’article. */
    @XmlElement(namespace = "http://univ.fr/rss25")
    private Image image;

    /** Contenu principal de l’article. */
    @XmlElement(namespace = "http://univ.fr/rss25", required = true)
    private Content content;

    /**
     * Liste des auteurs ou contributeurs de l’article.
     * Peut contenir des objets de type {@link Author} ou {@link Contributor}.
     */
    @XmlElements({
        @XmlElement(name = "author", namespace = "http://univ.fr/rss25", type = Author.class),
        @XmlElement(name = "contributor", namespace = "http://univ.fr/rss25", type = Contributor.class)
    })
    private List<Object> authorOrContributor;

    /**
     * Retourne la liste des catégories. Initialise la liste si elle est null.
     * @return liste de {@link Category}
     */
    public List<Category> getCategory() {
        if (category == null) {
            category = new ArrayList<>();
        }
        return category;
    }

    /**
     * Retourne la liste des auteurs ou contributeurs.
     * Initialise la liste si elle est null.
     * @return liste de {@link Author} ou {@link Contributor}
     */
    public List<Object> getAuthorOrContributor() {
        if (authorOrContributor == null) {
            authorOrContributor = new ArrayList<>();
        }
        return authorOrContributor;
    }
}