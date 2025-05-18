package fr.univrouen.rss25SB.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente un contenu associé à un article RSS dans le flux XML.
 * <p>
 * Ce contenu est décrit à l’aide de deux attributs XML :
 * <ul>
 *     <li>{@code type} : type MIME du contenu (obligatoire)</li>
 *     <li>{@code src} : source du contenu (optionnelle), généralement une URL ou un lien vers une ressource</li>
 * </ul>
 * </p>
 *
 * <p>Exemple XML généré :</p>
 * <pre>{@code
 * <content type="text/html" src="http://example.com/article.html"/>
 * }</pre>
 *
 * Cette classe est utilisée pour la sérialisation JAXB dans les objets {@code <item>}.
 * 
 * @see fr.univrouen.rss25SB.model.xml.Item
 * @version 1.0
 * @author Matisse SENECHAL
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Content")
public class Content {

    /**
     * Type MIME du contenu (ex. "text/html", "application/xml").
     * Attribut obligatoire.
     */
    @XmlAttribute(name = "type", required = true)
    private String type;

    /**
     * Source du contenu, souvent une URL ou un lien vers une ressource.
     * Attribut optionnel.
     */
    @XmlAttribute(name = "src")
    private String src;
}