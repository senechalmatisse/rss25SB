package fr.univrouen.rss25SB.converter.sources;

import fr.univrouen.rss25SB.model.xml.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.*;

import javax.xml.parsers.*;
import java.io.StringReader;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.xml.sax.InputSource;

/**
 * Convertisseur spécifique pour transformer un flux RSS 2.0 émis par le site LeMonde.fr
 * en un objet {@link Feed} conforme au format personnalisé rss25SB.
 *
 * <p>
 * Ce convertisseur analyse le XML brut, extrait les balises pertinentes de chaque élément
 * <code>&lt;item&gt;</code> du flux RSS (titre, description, date, guid, image...), puis
 * les mappe dans le modèle de données XML utilisé dans le projet.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Component
@Slf4j
public class LeMondeFluxConverter {

    /** Type d’image par défaut utilisé si absent dans le flux RSS */
    private static final String DEFAULT_IMAGE_TYPE = "image/JPEG";

    /** Texte alternatif utilisé par défaut pour l’image */
    private static final String DEFAULT_IMAGE_ALT = "Image Le Monde";

    /** Type de contenu associé à la description d’un article */
    private static final String DEFAULT_CONTENT_TYPE = "text/plain";

    /**
     * Convertit un flux RSS 2.0 complet (sous forme XML brut) en objet {@link Feed}.
     *
     * @param xmlContent chaîne XML du flux RSS d'origine (provenant de LeMonde.fr)
     * @return un objet {@link Feed} contenant la liste d’articles extraits et convertis
     *
     * @throws RuntimeException si une erreur de parsing ou de conversion survient
     */
    public Feed convert(String xmlContent) {
        try {
            // Préparation du parseur DOM
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xmlContent)));

            // Extraction des éléments <item>
            NodeList itemNodes = doc.getElementsByTagName("item");
            List<Item> items = new ArrayList<>();

            // Conversion de chaque <item> en objet rss25SB
            for (int i = 0; i < itemNodes.getLength(); i++) {
                Element itemElement = (Element) itemNodes.item(i);
                items.add(convertItem(itemElement));
            }

            // Assemblage final dans un objet Feed
            Feed feed = new Feed();
            feed.setItem(items);
            return feed;

        } catch (Exception e) {
            log.error("Erreur de conversion flux Le Monde : {}", e.getMessage());
            throw new RuntimeException("Échec de conversion du flux RSS Le Monde", e);
        }
    }

    /**
     * Convertit un élément <code>&lt;item&gt;</code> XML en objet {@link Item}.
     *
     * @param itemElement élément DOM représentant un article RSS
     * @return l’objet {@link Item} converti à partir du XML
     */
    private Item convertItem(Element itemElement) {
        Item item = new Item();

        item.setTitle(getTextContent(itemElement, "title"));
        item.setGuid(getTextContent(itemElement, "guid"));

        // Parsing de la date de publication au format RFC 1123
        String pubDate = getTextContent(itemElement, "pubDate");
        item.setPublished(OffsetDateTime.parse(pubDate, DateTimeFormatter.RFC_1123_DATE_TIME));

        // Construction du contenu
        Content content = new Content();
        content.setType(DEFAULT_CONTENT_TYPE);
        content.setSrc(getTextContent(itemElement, "description"));
        item.setContent(content);

        // Image (si disponible)
        Image image = extractImage(itemElement);
        if (image != null) {
            item.setImage(image);
        }

        return item;
    }

    /**
     * Extrait les données d’une image à partir d’un élément <code>&lt;media:content&gt;</code>,
     * en supposant qu’un seul bloc media est présent.
     *
     * @param itemElement élément <code>&lt;item&gt;</code> DOM contenant potentiellement une image
     * @return un objet {@link Image} si une image est présente, sinon {@code null}
     */
    private Image extractImage(Element itemElement) {
        NodeList mediaNodes = itemElement.getElementsByTagName("media:content");
        if (mediaNodes.getLength() == 0) {
            return null;
        }

        Element media = (Element) mediaNodes.item(0);
        Image image = new Image();
        image.setHref(media.getAttribute("url"));
        image.setType(DEFAULT_IMAGE_TYPE);
        image.setAlt(getTextContent(media, "media:description", DEFAULT_IMAGE_ALT));
        return image;
    }

    /**
     * Récupère le contenu textuel d’une balise spécifique dans un élément DOM.
     *
     * @param parent élément DOM parent
     * @param tagName nom de la balise à rechercher
     * @return contenu textuel trouvé, ou {@code null} si absent
     */
    private String getTextContent(Element parent, String tagName) {
        return getTextContent(parent, tagName, null);
    }

    /**
     * Récupère le contenu textuel d’une balise spécifique dans un élément DOM,
     * avec une valeur par défaut si la balise est absente.
     *
     * @param parent élément DOM parent
     * @param tagName nom de la balise à rechercher
     * @param defaultValue valeur de repli si la balise est absente
     * @return contenu textuel ou valeur par défaut
     */
    private String getTextContent(Element parent, String tagName, String defaultValue) {
        NodeList nodes = parent.getElementsByTagName(tagName);
        return (nodes.getLength() > 0) ? nodes.item(0).getTextContent().trim() : defaultValue;
    }
}