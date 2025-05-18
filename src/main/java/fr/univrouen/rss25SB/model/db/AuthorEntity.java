package fr.univrouen.rss25SB.model.db;

import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant un auteur dans la base de données.
 * <p>
 * Cette classe est mappée sur la table {@code author} et contient les
 * informations essentielles liées à un auteur d'article RSS :
 * <ul>
 *     <li><b>id</b> : identifiant unique (clé primaire)</li>
 *     <li><b>name</b> : nom complet de l’auteur (obligatoire)</li>
 *     <li><b>email</b> : adresse email de l’auteur (optionnelle)</li>
 *     <li><b>uri</b> : URI associée à l’auteur (optionnelle)</li>
 * </ul>
 * </p>
 *
 * Cette entité peut être utilisée pour enrichir les articles avec des métadonnées auteurs.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Entity
@Table(name = "author")
@Getter @Setter @NoArgsConstructor
public class AuthorEntity {

    /** Identifiant unique de l’auteur (clé primaire auto-générée). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom complet de l’auteur. Ce champ est obligatoire. */
    @Column(name = "author_name", nullable = false)
    private String name;

    /** Adresse email de l’auteur. Ce champ est optionnel. */
    @Column(name = "author_email")
    private String email;

    /** URI (lien ou identifiant unique) associée à l’auteur. Ce champ est optionnel. */
    @Column(name = "author_uri")
    private String uri;
}