package fr.univrouen.rss25SB.utils;

import jakarta.xml.bind.*;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.validation.*;
import java.io.*;

/**
 * Utilitaire pour la manipulation XML avec JAXB, incluant :
 * <ul>
 *     <li>la désérialisation d’un flux XML en objet Java avec validation XSD</li>
 *     <li>la sérialisation d’un objet Java en chaîne XML</li>
 * </ul>
 * <p>
 * Ce composant est utilisé notamment dans les contrôleurs pour convertir
 * les flux RSS personnalisés vers/depuis les classes JAXB.
 * </p>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class XmlUtil {

    /**
     * Désérialise un flux XML en un objet Java, en le validant à l’aide d’un schéma XSD.
     *
     * @param xmlContent le contenu XML brut sous forme de chaîne
     * @param clazz      la classe de destination (ex : {@code Feed.class})
     * @param xsdPath    le chemin du fichier XSD (doit se trouver dans {@code resources/})
     * @param <T>        le type générique de l’objet retourné
     * @return une instance de type {@code T} correspondant au contenu XML
     *
     * @throws JAXBException si une erreur de désérialisation se produit (structure incorrecte, balise inconnue, etc.)
     * @throws SAXException si le contenu XML ne respecte pas le schéma défini dans le fichier XSD
     */
    public static <T> T unmarshal(String xmlContent, Class<T> clazz, String xsdPath)
            throws JAXBException, SAXException {
        // Création du contexte JAXB pour la classe cible
        JAXBContext context = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        // Chargement du schéma XSD depuis le classpath
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = factory.newSchema(XmlUtil.class.getClassLoader().getResource(xsdPath));

        // Attache le schéma au désérialiseur pour activer la validation XSD
        unmarshaller.setSchema(schema);

        // Conversion de la chaîne XML vers un objet Java
        return (T) unmarshaller.unmarshal(new StringReader(xmlContent));
    }

    /**
     * Sérialise un objet Java en XML sous forme de chaîne lisible.
     *
     * @param object l’objet à convertir (ex : {@code Feed}, {@code Item}, {@code XmlErrorResponseDTO}, etc.)
     * @return une chaîne XML formatée représentant l’objet
     *
     * @throws JAXBException si la sérialisation échoue (ex : attributs manquants, structure incorrecte)
     */
    public static String marshal(Object object) throws JAXBException {
        // Création du contexte JAXB à partir de la classe réelle de l'objet
        JAXBContext context = JAXBContext.newInstance(object.getClass());
        Marshaller marshaller = context.createMarshaller();

        // Option pour indenter le XML pour plus de lisibilité
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        // Sérialisation de l'objet vers une chaîne XML
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    public static String extractFirstErrorMessage(Throwable exception) {
        Throwable current = exception;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        return current.getMessage();
    }
}