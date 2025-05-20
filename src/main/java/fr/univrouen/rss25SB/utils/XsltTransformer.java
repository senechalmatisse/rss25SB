package fr.univrouen.rss25SB.utils;

import lombok.extern.slf4j.Slf4j;

import javax.xml.transform.*;
import javax.xml.transform.stream.*;

import org.springframework.stereotype.Component;

import jakarta.xml.bind.JAXBContext;

import java.io.*;

/**
 * Utilitaire de transformation permettant de convertir du contenu XML en HTML
 * à l’aide de feuilles de style XSLT. Cette classe fournit deux méthodes :
 * <ul>
 *     <li>{@link #transform(String, String)} pour transformer une chaîne XML</li>
 *     <li>{@link #marshalAndTransform(Object, String)} pour transformer un objet JAXB</li>
 * </ul>
 *
 * <p>
 * Ces méthodes sont principalement utilisées dans les contrôleurs REST pour
 * générer dynamiquement des pages HTML à partir des flux RSS stockés dans l’application.
 * </p>
 *
 * <p>
 * En cas d’erreur de transformation, un message HTML d’erreur est généré et journalisé.
 *
 * @author Matisse SENECHAL
 * @version 2.0
 */
@Slf4j
@Component
public class XsltTransformer {

    /**
     * Transforme un contenu XML (au format chaîne) en HTML via une feuille XSLT.
     *
     * @param xmlContent   Chaîne XML à transformer (doit être bien formée)
     * @param xsltFilePath Chemin relatif du fichier XSLT dans le classpath (ex: {@code "/xslt/rss25-list.xslt"})
     * @return Chaîne HTML générée par la transformation ; si une erreur survient, un message HTML d’erreur est retourné
     *
     * @throws IllegalArgumentException si le fichier XSLT est introuvable dans le classpath
     */
    public static String transform(String xmlContent, String xsltFilePath) {
        try {
            // Création des sources XML et XSLT
            Source xmlSource = new StreamSource(new StringReader(xmlContent));
            InputStream xsltStream = XsltTransformer.class.getResourceAsStream(xsltFilePath);
            if (xsltStream == null) {
                throw new IllegalArgumentException("Fichier XSLT introuvable : " + xsltFilePath);
            }
            Source xsltSource = new StreamSource(xsltStream);

            // Initialisation du transformateur
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSource);

            // Définition des propriétés de sortie HTML
            transformer.setOutputProperty(OutputKeys.METHOD, "html");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            // Transformation en mémoire
            StringWriter outputWriter = new StringWriter();
            transformer.transform(xmlSource, new StreamResult(outputWriter));

            return outputWriter.toString();
        } catch (TransformerException e) {
            log.error("Erreur de transformation XSLT", e);
            return "<html><body><p>Erreur de transformation XSLT : " + e.getMessage() + "</p></body></html>";
        }
    }

    /**
     * Transforme un objet Java (annoté JAXB) en HTML via une feuille XSLT.
     * <p>
     * Cette méthode effectue en interne une sérialisation JAXB (objet → XML),
     * suivie d’une transformation XSLT (XML → HTML).
     *
     * @param jaxbObject   Objet Java à transformer (doit être compatible JAXB)
     * @param xsltFilePath Chemin du fichier XSLT dans le classpath (ex: {@code "/xslt/rss25.item.xslt"})
     * @return Chaîne HTML résultante après transformation
     * @throws Exception en cas d’erreur de sérialisation JAXB ou de transformation XSLT
     */
    public static String marshalAndTransform(Object jaxbObject, String xsltFilePath) throws Exception {
        JAXBContext context = JAXBContext.newInstance(jaxbObject.getClass());
        StringWriter xmlWriter = new StringWriter();
        context.createMarshaller().marshal(jaxbObject, xmlWriter);
        return transform(xmlWriter.toString(), xsltFilePath);
    }
}