package fr.univrouen.rss25SB.model.db;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entity représentant un article RSS stocké en base de données.
 * <p>
 * Cette entité est mappée sur la table {@code item}.  
 * Elle contient les informations essentielles d’un article :
 * <ul>
 *     <li><b>id</b> : identifiant unique auto-généré</li>
 *     <li><b>title</b> : titre de l’article</li>
 *     <li><b>guid</b> : identifiant global unique</li>
 *     <li><b>published</b> : date de publication de l’article</li>
 * </ul>
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "item")
public class ItemEntity {

    /**
     * Identifiant unique de l’article.
     * Généré automatiquement par la base de données (clé primaire).
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Titre de l’article.
     * Ce champ est obligatoire (non nul).
     */
    @Column(nullable = false)
    private String title;

    /**
     * GUID (Global Unique Identifier) de l’article.
     * Généralement une URL ou une référence unique.
     * Ce champ est obligatoire (non nul).
     */
    @Column(nullable = false)
    private String guid;

    /**
     * Date de publication de l’article.
     * Stockée en format date/heure avec fuseau horaire.
     */
    @Column(nullable = false)
    private OffsetDateTime published;

    /**
     * Constructeur avec paramètres.
     *
     * @param title     le titre de l’article
     * @param guid      l’identifiant global de l’article
     * @param published la date de publication
     */
    public ItemEntity(String title, String guid, OffsetDateTime published) {
        this.title = title;
        this.guid = guid;
        this.published = published;
    }
}