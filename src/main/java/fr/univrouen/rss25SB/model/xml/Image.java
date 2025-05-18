package fr.univrouen.rss25SB.model.xml;

import jakarta.xml.bind.annotation.*;
import lombok.*;

/**
 * Représente une image associée à un article RSS dans le flux XML.
 * <p>
 * Cette image est décrite à l’aide d’attributs XML et permet d’enrichir un article
 * avec un visuel ou une illustration associée.
 * </p>
 *
 * <p>Attributs XML :</p>
 * <ul>
 *     <li>{@code type} : type MIME de l’image (ex. "image/png") – requis</li>
 *     <li>{@code href} : URL de l’image – requis</li>
 *     <li>{@code alt} : texte alternatif – requis</li>
 *     <li>{@code length} : taille de l’image en octets – optionnel</li>
 * </ul>
 *
 * <p>Exemple XML généré :</p>
 * <pre>{@code
 * <image type="image/jpeg" href="http://example.com/image.jpg" alt="logo" length="1024"/>
 * }</pre>
 *
 * Cette classe est utilisée dans les éléments {@code <item>} du flux RSS personnalisé.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter @Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Image {

    /**
     * Type MIME de l’image (ex. "JPEG", "PNG").
     * Attribut requis.
     */
    @XmlAttribute(name = "type", required = true)
    private String type;

    /** Lien URL vers l’image. Attribut requis. */
    @XmlAttribute(name = "href", required = true)
    private String href;

    /**
     * Texte alternatif de l’image (pour accessibilité ou fallback).
     * Attribut requis.
     */
    @XmlAttribute(name = "alt", required = true)
    private String alt;

    /** Taille de l’image en octets. Attribut optionnel. */
    @XmlAttribute(name = "length")
    private Integer length;
}