package fr.univrouen.rss25SB.model.db;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un contributeur lié à un article RSS dans la base de données.
 * <p>
 * Un contributeur peut être une personne ou un organisme ayant participé à la rédaction,
 * la révision ou la publication d’un article.
 * </p>
 * 
 * <p>Cette classe est mappée sur la table {@code contributor}.</p>
 *
 * <ul>
 *     <li><b>id</b> : identifiant unique (clé primaire)</li>
 *     <li><b>name</b> : nom du contributeur (obligatoire)</li>
 *     <li><b>email</b> : adresse email (optionnelle)</li>
 *     <li><b>uri</b> : URI ou lien d’identification (optionnelle)</li>
 * </ul>
 * 
 * Cette entité peut être utilisée pour enrichir les articles avec des métadonnées collaboratives.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Entity
@Table(name = "contributor")
@Getter @Setter @NoArgsConstructor
public class ContributorEntity {

    /** Identifiant unique du contributeur (clé primaire auto-générée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom complet du contributeur. Ce champ est obligatoire. */
    @Column(name = "contributor_name", nullable = false)
    private String name;

    /** Adresse email du contributeur. Ce champ est optionnel. */
    @Column(name = "contributor_email")
    private String email;

    /**
     * URI (Uniform Resource Identifier) du contributeur.
     * Ce champ est optionnel.
     */
    @Column(name = "contributor_uri")
    private String uri;
}