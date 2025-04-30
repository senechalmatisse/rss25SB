package fr.univrouen.rss25SB.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

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

    /** GUID de l’article, représenté comme une URL contenant un UUID (RFC4122). */
    private String guid;
}