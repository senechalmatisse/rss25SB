package fr.univrouen.rss25SB.model;

import lombok.*;

/**
 * Modèle représentant une opération REST exposée par le service RSS25SB.
 * <p>
 * Utilisé pour alimenter dynamiquement la page d’aide (/help), ce modèle décrit :
 * <ul>
 *   <li>l’URL associée à l’opération,</li>
 *   <li>la méthode HTTP à utiliser,</li>
 *   <li>un résumé clair du comportement de l’opération, incluant :
 *     <ul>
 *         <li>les formats attendus en entrée,</li>
 *         <li>le format de retour,</li>
 *         <li>les remarques spécifiques éventuelles.</li>
 *     </ul>
 *   </li>
 * </ul>
 * Cette classe est utilisée comme source de données dans la vue HTML de l’aide.
 *
 * @author Matisse SENECHAL
 * @version 1.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class OperationInfo {

    /**
     * URL relative de l’opération REST (ex : {@code /rss25SB/resume/xml}).
     */
    private String url;

    /**
     * Méthode HTTP utilisée pour accéder à cette ressource (ex : {@code GET}, {@code POST}).
     */
    private String method;

    /**
     * Description complète de l’opération : objectifs, formats acceptés, type de réponse attendu.
     */
    private String description;
}