package fr.univrouen.rss25SB.model;

/**
 * Modèle représentant une opération REST disponible sur l'API RSS25SB.
 * <p>
 * Chaque instance de cette classe décrit :
 * <ul>
 *   <li>L'URL de l'opération.</li>
 *   <li>La méthode HTTP utilisée (GET, POST, DELETE, etc.).</li>
 *   <li>Un résumé ou une description de ce que réalise cette opération.</li>
 * </ul>
 * Ce modèle est utilisé pour alimenter dynamiquement la page d'aide (/help).
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class OperationInfo {

    /** URL relative de l'opération REST. */
    private String url;

    /** Méthode HTTP utilisée par l'opération (ex: GET, POST, DELETE). */
    private String method;

    /** Description de l'objectif ou du comportement de l'opération. */
    private String description;

    /**
     * Constructeur de l'objet {@code OperationInfo}.
     *
     * @param url         URL de l'opération
     * @param method      méthode HTTP utilisée
     * @param description description de l'opération
     */
    public OperationInfo(String url, String method, String description) {
        this.url = url;
        this.method = method;
        this.description = description;
    }

    /**
     * Retourne l'URL de l'opération.
     *
     * @return URL sous forme de chaîne de caractères
     */
    public String getUrl() {
        return url;
    }

    /**
     * Retourne la méthode HTTP de l'opération.
     *
     * @return méthode HTTP utilisée
     */
    public String getMethod() {
        return method;
    }

    /**
     * Retourne la description de l'opération.
     *
     * @return description textuelle de l'opération
     */
    public String getDescription() {
        return description;
    }
}