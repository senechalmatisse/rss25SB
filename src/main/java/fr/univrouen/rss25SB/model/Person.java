package fr.univrouen.rss25SB.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

/**
 * Entité embarquée représentant une personne (author ou contributor) associée à un article RSS.
 * Conforme à la structure XSD avec validation des attributs.
 * 
 * <p>Les contraintes de validation appliquées sont :</p>
 * <ul>
 *   <li>name : obligatoire, ≤ 64 caractères</li>
 *   <li>email : optionnel, conforme RFC6530</li>
 *   <li>uri : optionnel, conforme RFC3987</li>
 *   <li>role : obligatoire, "author" ou "contributor"</li>
 * </ul>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    /** Nom de la personne : obligatoire, ≤ 64 caractères */
    @NotBlank
    @Column(nullable = false, length = 64)
    @Pattern(
        regexp = "^[A-Za-z]+([ -][A-Za-z]+)*$",
        message = "Le nom ne doit contenir que des lettres, avec éventuellement des espaces ou tirets entre les mots"
    )
    private String name;
    

    /** Adresse email (optionnelle) conforme à la RFC6530. */
    @Pattern(
        regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}",
        message = "L'email doit respecter la RFC6530"
    )
    private String email;

    /** URI (optionnelle) conforme à la RFC3987. */
    @Pattern(
        regexp = "[^\\s\\-_~@]+(\\.[^\\s\\-_~@]+)*",
        message = "L'URI doit respecter la RFC3987"
    )
    private String uri;

    /** Rôle de la personne : "author" ou "contributor". */
    @NotBlank
    @Column(nullable = false)
    @Pattern(
        regexp = "author|contributor",
        message = "Le rôle doit être soit 'author' soit 'contributor'"
    )
    private String role;
}