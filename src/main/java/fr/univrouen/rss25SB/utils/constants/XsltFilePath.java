package fr.univrouen.rss25SB.utils.constants;

/**
 * <p>
 * Enumération centralisant les chemins des fichiers XSLT utilisés
 * pour transformer les contenus XML en affichage HTML dans l'application RSS25SB.
 * </p>
 *
 * <p>
 * Chaque constante représente une feuille XSLT spécifique à un type de transformation :
 * <ul>
 *     <li>{@link #LIST} : pour les résumés d'articles</li>
 *     <li>{@link #ITEM} : pour les articles complets</li>
 *     <li>{@link #ERROR} : pour les messages d'erreur XML</li>
 *     <li>{@link #INSERT} : pour le retour après insertion</li>
 * </ul>
 * </p>
 *
 * <p>
 * Ces chemins correspondent à des fichiers présents dans le classpath,
 * généralement situés sous <code>resources/xslt/</code>.
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
public enum XsltFilePath {

    /**
     * Feuille XSLT utilisée pour transformer une liste synthétique des articles (résumés).
     * Correspond à {@code /xslt/rss25-list.xslt}.
     */
    LIST("/xslt/rss25-list.xslt"),

    /**
     * Feuille XSLT utilisée pour transformer un article complet en HTML.
     * Correspond à {@code /xslt/rss25.item.xslt}.
     */
    ITEM("/xslt/rss25.item.xslt"),

    /**
     * Feuille XSLT utilisée pour transformer les messages d’erreur XML en affichage lisible HTML.
     * Correspond à {@code /xslt/rss25.error.xslt}.
     */
    ERROR("/xslt/rss25.error.xslt"),

    /**
     * Feuille XSLT utilisée pour transformer la réponse suite à une insertion XML.
     * Correspond à {@code /xslt/rss25.insert.xslt}.
     */
    INSERT("/xslt/rss25.insert.xslt");

    /** Chemin du fichier XSLT dans les ressources du projet (classpath). */
    private final String path;

    /**
     * Constructeur de l’énumération.
     *
     * @param path Chemin relatif du fichier XSLT dans les ressources
     */
    XsltFilePath(String path) {
        this.path = path;
    }

    /**
     * Retourne le chemin complet du fichier XSLT.
     *
     * @return Le chemin du fichier XSLT, ex. {@code "/xslt/rss25-list.xslt"}
     */
    public String getPath() {
        return path;
    }
}