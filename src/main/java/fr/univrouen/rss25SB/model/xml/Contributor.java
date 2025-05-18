package fr.univrouen.rss25SB.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente un contributeur associé à un article RSS dans le flux XML.
 * <p>
 * Un contributeur peut être une personne ou une entité ayant participé à la rédaction,
 * révision ou publication de l’article. Il est modélisé avec des attributs XML :
 * </p>
 *
 * <ul>
 *     <li>{@code name} : nom complet du contributeur (obligatoire)</li>
 *     <li>{@code email} : adresse email (optionnelle)</li>
 *     <li>{@code uri} : URI ou lien du contributeur (optionnel)</li>
 * </ul>
 *
 * <p>Exemple XML généré :</p>
 * <pre>{@code
 * <contributor name="Claire Martin" email="claire@example.com" uri="http://example.com/contributors/claire"/>
 * }</pre>
 *
 * Cette classe est utilisée dans les objets {@code <item>} lors de la sérialisation XML avec JAXB.
 * 
 * @version 1.0
 * @author Matisse SENECHAL
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Contributor {

    /**
     * Nom complet du contributeur.
     * Attribut requis dans le flux XML.
     */
    @XmlAttribute(name = "name", required = true)
    private String name;

    /** Adresse email du contributeur (facultatif). */
    @XmlAttribute(name = "email")
    private String email;

    /** URI ou lien associé au contributeur (facultatif). */
    @XmlAttribute(name = "uri")
    private String uri;
}