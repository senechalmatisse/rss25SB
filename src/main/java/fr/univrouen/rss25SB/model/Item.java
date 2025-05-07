package fr.univrouen.rss25SB.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.URL;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Entité JPA représentant un article RSS minimal stocké en base de données.
 * <p>
 * Cette classe contient les informations essentielles à exposer
 * dans le flux résumé : identifiant, date de publication et GUID.
 * </p>
 *
 * <p>Annotations :</p>
 * <ul>
 *   <li>{@link Entity} : Indique qu’il s’agit d’une entité persistée par JPA</li>
 *   <li>{@link Id} : Clé primaire</li>
 *   <li>{@link GeneratedValue} : Auto-incrémentation gérée par la BDD (IDENTITY)</li>
 *   <li>{@link Getter}/{@link Setter} : Générés automatiquement par Lombok</li>
 * </ul>
 * 
 * <p>
 * Le champ {@code date} utilise {@link OffsetDateTime} afin de respecter
 * la norme RFC3339 avec décalage horaire explicite.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Entity
@Getter
@Setter
public class Item {

    /** Identifiant unique de l’article (clé primaire en base de données). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Date de publication ou de mise à jour de l’article.
     * Doit être au format ISO-8601 avec offset (RFC3339).
     */
    private OffsetDateTime date;

    /**
     * GUID de l’article : une URL contenant un UUID valide (RFC4122).
     * Exemple : http://example.com/uuid/123e4567-e89b-12d3-a456-426614174000
     */
    @NotBlank(message = "Le GUID est obligatoire")
    @URL(message = "Le GUID doit être une URL valide")
    @Pattern(
        regexp = ".*[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}$",
        message = "Le GUID doit contenir un UUID valide conforme à la RFC4122"
    )
    @Column(nullable = false)
    private String guid;

    /** Titre de l’article (texte, maximum 128 caractères). */
    @NotBlank(message = "Le titre est obligatoire et ne peut pas être vide")
    @Size(max = 128, message = "Le titre ne doit pas dépasser 128 caractères")
    @Column(length = 128, nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(name = "item_categories", joinColumns = @JoinColumn(name = "item_id"))
    @Column(name = "term")
    private List<String> categories;

    private String imageType;
    private String imageHref;
    private String imageAlt;
    private Integer imageLength;

    private String contentType;
    private String contentSrc;

    @ElementCollection
    @CollectionTable(name = "item_authors", joinColumns = @JoinColumn(name = "item_id"))
    private List<Person> authors;

    @ElementCollection
    @CollectionTable(name = "item_contributors", joinColumns = @JoinColumn(name = "item_id"))
    private List<Person> contributors;
}