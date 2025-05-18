package fr.univrouen.rss25SB.model.db;

import java.time.OffsetDateTime;
import java.util.*;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité JPA représentant un article RSS stocké en base de données.
 * <p>
 * Elle correspond à un élément {@code <item>} dans un flux RSS étendu
 * et contient les métadonnées essentielles d’un article :
 * titre, date, contenu, auteurs, catégories, image, etc.
 * </p>
 * 
 * <p>Cette entité est mappée sur la table {@code item}.</p>
 * 
 * @author Matisse SENECHAL
 * @version 1.1
 */
@Entity
@Getter @Setter @NoArgsConstructor
@Table(name = "item")
public class ItemEntity {

    /**
     * Identifiant unique de l’article.
     * Généré automatiquement (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Titre de l’article. Ce champ est obligatoire. */
    @Column(nullable = false)
    private String title;

    /**
     * GUID (Global Unique Identifier) de l’article.
     * Généralement une URL ou un identifiant stable.
     * Ce champ est obligatoire.
     */
    @Column(nullable = false)
    private String guid;

    /** Date de publication de l’article (avec fuseau horaire). */
    @Column(nullable = false)
    private OffsetDateTime published;

    /** Date de mise à jour de l’article (optionnelle). */
    @Column
    private OffsetDateTime updated;

    /**
     * Contenu principal de l’article, incluant type MIME et source.
     * Représenté comme classe embarquée {@link ContentEntity}.
     */
    @Embedded
    private ContentEntity content;

    /**
     * Image associée à l’article (type, URL, alt, taille).
     * Représentée comme classe embarquée {@link ImageEntity}.
     */
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "type", column = @Column(name = "image_type", nullable = true)),
        @AttributeOverride(name = "href", column = @Column(name = "href", nullable = true)),
        @AttributeOverride(name = "alt", column = @Column(name = "alt", nullable = true)),
        @AttributeOverride(name = "length", column = @Column(name = "length", nullable = true))
    })
    private ImageEntity image;

    /**
     * Liste des catégories associées à l’article.
     * Chaque catégorie est persistée via relation {@code @OneToMany}.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<CategoryEntity> categories = new ArrayList<>();

    /**
     * Liste des auteurs de l’article.
     * Représentés via une relation {@code @OneToMany}.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<AuthorEntity> authors = new ArrayList<>();

    /**
     * Liste des contributeurs de l’article.
     * Représentés via une relation {@code @OneToMany}.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<ContributorEntity> contributors = new ArrayList<>();

    /**
     * Constructeur partiel avec les champs obligatoires.
     *
     * @param title     le titre de l’article
     * @param guid      l’identifiant global unique
     * @param published la date de publication
     */
    public ItemEntity(String title, String guid, OffsetDateTime published) {
        this.title = title;
        this.guid = guid;
        this.published = published;
    }
}