package fr.univrouen.rss25SB.dto;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * DTO racine pour encapsuler une liste d’articles RSS dans les réponses XML.
 * <p>
 * Cette classe est utilisée pour représenter une collection de {@link ItemDTO}
 * sous forme de flux XML sérialisé. Elle est annotée avec {@link XmlRootElement}
 * pour permettre la génération correcte du XML de réponse.
 * </p>
 *
 * <p>Exemple de rendu XML attendu :</p>
 * <pre>{@code
 * <items>
 *     <item>
 *         <id>1</id>
 *         <date>2025-04-30T12:00:00+02:00</date>
 *         <guid>https://example.com/550e8400-e29b-41d4-a716-446655440000</guid>
 *     </item>
 *     ...
 * </items>
 * }</pre>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@XmlRootElement(name = "items")
public class ItemListDTO {

    /** Liste des articles résumés. */
    private List<ItemDTO> items;

    /** Constructeur vide requis par JAXB. */
    public ItemListDTO() {
        // Nécessaire pour la désérialisation XML
    }

    /**
     * Constructeur permettant d’injecter la liste d’articles.
     *
     * @param items liste des {@link ItemDTO} à exposer
     */
    public ItemListDTO(List<ItemDTO> items) {
        this.items = items;
    }

    /**
     * Getter de la liste d’articles.
     *
     * @return liste des articles sous forme de {@link ItemDTO}
     */
    @XmlElement(name = "item")
    public List<ItemDTO> getItems() {
        return items;
    }

    /**
     * Setter de la liste d’articles.
     *
     * @param items liste des articles
     */
    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }
}