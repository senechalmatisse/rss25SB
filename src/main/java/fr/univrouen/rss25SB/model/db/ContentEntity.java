package fr.univrouen.rss25SB.model.db;

import jakarta.persistence.*;
import lombok.*;

/**
 * Classe embarquée représentant le contenu d’un article RSS dans la base de données.
 * <p>
 * Cette entité est annotée avec {@link Embeddable}, ce qui signifie qu’elle
 * sera incluse dans une autre entité (par exemple, une entité {@code ItemEntity})
 * et ne dispose pas de table propre.
 * </p>
 *
 * <ul>
 *     <li><b>type</b> : type MIME du contenu (obligatoire), par exemple {@code text/html} ou {@code image/png}</li>
 *     <li><b>src</b> : source ou URI du contenu (optionnelle)</li>
 * </ul>
 *
 * Cette structure permet de modéliser des contenus variés (texte, image, vidéo, etc.) liés à un article RSS.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Embeddable
@Getter @Setter
public class ContentEntity {

    /**
     * Type MIME du contenu (ex : "text/html", "application/xml").
     * Ce champ est obligatoire.
     */
    @Column(name = "content_type", nullable = false)
    private String type;

    /**
     * Source du contenu, typiquement une URL ou un identifiant de ressource externe.
     * Ce champ est optionnel.
     */
    @Column(name = "src")
    private String src;
}