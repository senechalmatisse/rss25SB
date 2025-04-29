package fr.univrouen.rss25SB.model;

/**
 * Représente les informations générales du projet RSS25SB à afficher sur la page d'accueil.
 * <p>
 * Cette classe est utilisée pour encapsuler les métadonnées du projet
 * telles que le nom, la version, le développeur et le chemin vers le logo.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class ProjectInfo {
    /** Nom du projet. */
    private final String name;

    /** Version du projet. */
    private final String version;

    /** Nom du ou des développeurs du projet. */
    private final String developer;

    /** Chemin relatif du logo de l’université à afficher. */
    private final String logoPath;

    /**
     * Constructeur de la classe {@code ProjectInfo}.
     *
     * @param name       le nom du projet
     * @param version    la version du projet
     * @param developer  le nom du développeur
     * @param logoPath   le chemin relatif du logo à utiliser dans les vues HTML
     */
    public ProjectInfo(String name, String version, String developer, String logoPath) {
        this.name = name;
        this.version = version;
        this.developer = developer;
        this.logoPath = logoPath;
    }

    /**
     * Retourne le nom du projet.
     *
     * @return le nom du projet
     */
    public String getName() {
        return name;
    }

    /**
     * Retourne la version du projet.
     *
     * @return la version du projet
     */
    public String getVersion() {
        return version;
    }

    /**
     * Retourne le nom du ou des développeurs.
     *
     * @return le nom du développeur
     */
    public String getDeveloper() {
        return developer;
    }

    /**
     * Retourne le chemin du logo à afficher dans les pages HTML.
     *
     * @return le chemin relatif du logo
     */
    public String getLogoPath() {
        return logoPath;
    }
}