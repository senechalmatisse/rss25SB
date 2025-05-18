package fr.univrouen.rss25SB.model.db;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant une catégorie associée à un article RSS.
 * <p>
 * Cette classe est mappée sur la table {@code category} dans la base de données.
 * Elle contient un identifiant unique ainsi qu’un terme décrivant la catégorie.
 * </p>
 *
 * <ul>
 *     <li><b>id</b> : identifiant unique de la catégorie (clé primaire)</li>
 *     <li><b>term</b> : libellé ou mot-clé de la catégorie (obligatoire)</li>
 * </ul>
 *
 * Cette entité peut être liée à un ou plusieurs articles afin de les classer
 * ou les filtrer par thématique.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Entity
@Table(name = "category")
@Getter @Setter @NoArgsConstructor
public class CategoryEntity {

    /** Identifiant unique de la catégorie (clé primaire auto-générée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Terme ou mot-clé décrivant la catégorie. Ce champ est obligatoire. */
    @Column(name = "term", nullable = false)
    private String term;
}