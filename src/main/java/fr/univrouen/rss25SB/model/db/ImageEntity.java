package fr.univrouen.rss25SB.model.db;

import jakarta.persistence.*;
import lombok.*;

/**
 * Classe embarquée représentant les métadonnées d’une image associée à un article RSS.
 * <p>
 * Cette classe est annotée avec {@link Embeddable} et est destinée à être incluse
 * dans une entité parent (comme {@code ItemEntity}) via l’annotation {@code @Embedded}.
 * </p>
 *
 * <ul>
 *     <li><b>type</b> : type MIME de l’image (ex. {@code image/png}, {@code image/jpeg})</li>
 *     <li><b>href</b> : URL de l’image</li>
 *     <li><b>alt</b> : texte alternatif pour l’image</li>
 *     <li><b>length</b> : taille en octets de l’image (optionnelle)</li>
 * </ul>
 * 
 * Cette structure permet de modéliser proprement les attributs liés à une image dans le modèle RSS.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Embeddable
@Getter @Setter
public class ImageEntity {

    /**
     * Type MIME de l’image (ex. "image/png", "image/jpeg").
     * Ce champ est obligatoire.
     */
    @Column(name = "image_type", nullable = false)
    private String type;

    /** URL de l’image. Ce champ est obligatoire. */
    @Column(name = "href", nullable = false)
    private String href;

    /** Texte alternatif pour l’image. Ce champ est obligatoire. */
    @Column(name = "alt", nullable = false)
    private String alt;

    /** Taille de l’image en octets. Ce champ est optionnel. */
    @Column(name = "length")
    private Integer length;
}