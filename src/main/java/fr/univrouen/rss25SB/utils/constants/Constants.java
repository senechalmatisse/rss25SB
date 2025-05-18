package fr.univrouen.rss25SB.utils.constants;

/**
 * Classe utilitaire contenant les constantes globales de l'application.
 * <p>
 * Cette classe est marquée comme {@code final} et son constructeur est privé,
 * afin de garantir une utilisation purement statique.
 * </p>
 *
 * <p>Elle centralise les chemins de fichiers, noms de dossiers, et autres
 * chaînes de configuration utilisées dans plusieurs parties du projet.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
public final class Constants {

    /**
     * Constructeur privé pour empêcher l’instanciation de cette classe utilitaire.
     */
    private Constants() {

    }

    /**
     * Nom du fichier XSD utilisé pour valider les flux RSS en entrée.
     * Ce fichier doit être présent dans le classpath (ex: {@code src/main/resources}).
     */
    public static final String XSD_PATH = "rss25.xsd";
}