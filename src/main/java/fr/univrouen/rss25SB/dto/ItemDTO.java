package fr.univrouen.rss25SB.dto;

import jakarta.xml.bind.annotation.*;
import java.io.Serializable;

/**
 * DTO (Data Transfer Object) représentant un article RSS au format résumé.
 * <p>
 * Cette classe est utilisée pour exposer une version simplifiée d’un article
 * dans les réponses XML générées par l’API REST.
 * Elle contient uniquement :
 * <ul>
 *     <li>L'identifiant unique de l'article</li>
 *     <li>La date de publication (au format RFC3339)</li>
 *     <li>Le GUID (URL contenant un UUID conforme à la RFC4122)</li>
 * </ul>
 * </p>
 * 
 * <p>
 * Elle est sérialisable et utilisable directement avec JAXB
 * grâce à l’annotation {@link XmlRootElement}.
 * </p>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@XmlRootElement(name = "item")
public class ItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /** Identifiant unique de l'article (généré par la base de données). */
    private int id;

    /** Date de publication de l'article, au format ISO_OFFSET_DATE_TIME (RFC3339). */
    private String date;

    /** GUID de l'article, sous forme d'URL contenant un UUID (RFC4122). */
    private String guid;

    /**
     * Constructeur vide requis pour JAXB lors de la désérialisation XML.
     */
    public ItemDTO() {}

    /**
     * Constructeur complet.
     *
     * @param id    identifiant de l’article
     * @param date  date de publication (format RFC3339)
     * @param guid  GUID unique de l’article (URL + UUID)
     */
    public ItemDTO(int id, String date, String guid) {
        this.id = id;
        this.date = date;
        this.guid = guid;
    }

    /**
     * Retourne l’identifiant de l’article.
     *
     * @return l’ID
     */
    @XmlElement
    public int getId() {
        return id;
    }

    /**
     * Retourne la date de publication.
     *
     * @return la date au format RFC3339
     */
    @XmlElement
    public String getDate() {
        return date;
    }

    /**
     * Retourne le GUID de l’article.
     *
     * @return l’URL contenant l’UUID de l’article
     */
    @XmlElement
    public String getGuid() {
        return guid;
    }
}