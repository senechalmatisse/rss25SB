package fr.univrouen.rss25SB.model;

import lombok.*;

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
@Getter
@AllArgsConstructor
public class OperationInfo {

    /** URL relative de l'opération REST. */
    private String url;

    /** Méthode HTTP utilisée par l'opération (ex: GET, POST, DELETE). */
    private String method;

    /** Description de l'objectif ou du comportement de l'opération. */
    private String description;
}