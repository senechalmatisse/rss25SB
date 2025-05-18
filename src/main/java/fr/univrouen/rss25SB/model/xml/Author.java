package fr.univrouen.rss25SB.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente un auteur d’article dans un flux RSS personnalisé.
 * <p>
 * Cet auteur est modélisé avec des attributs XML et peut être utilisé
 * dans les éléments {@code <item>} du flux RSS.
 * </p>
 *
 * <p>Exemple XML généré :</p>
 * <pre>{@code
 * <author name="Jean Dupont" email="jean@example.com" uri="http://example.com/auteurs/jean" />
 * }</pre>
 *
 * <p>Les attributs incluent :</p>
 * <ul>
 *     <li>{@code name} : nom complet de l’auteur (obligatoire)</li>
 *     <li>{@code email} : adresse email (optionnelle)</li>
 *     <li>{@code uri} : lien ou identifiant de l’auteur (optionnel)</li>
 * </ul>
 *
 * Cette classe est utilisée dans le modèle JAXB lors de la sérialisation XML.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Author {

    /** Nom complet de l’auteur. Attribut requis dans le flux XML. */
    @XmlAttribute(name = "name", required = true)
    private String name;

    /** Adresse email de l’auteur (facultatif). */
    @XmlAttribute(name = "email")
    private String email;

    /** URI ou lien associé à l’auteur (facultatif). */
    @XmlAttribute(name = "uri")
    private String uri;
}