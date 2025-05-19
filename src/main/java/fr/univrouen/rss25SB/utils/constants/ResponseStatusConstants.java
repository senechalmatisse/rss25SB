package fr.univrouen.rss25SB.utils.constants;

/**
 * Classe utilitaire contenant les constantes de statut pour les réponses XML.
 * <p>
 * Ces constantes sont utilisées notamment dans les objets de type
 * {@link fr.univrouen.rss25SB.dto.InsertResponseDTO} afin de normaliser
 * les statuts de retour dans les flux XML de réponse.
 * </p>
 *
 * <p>Cette classe est déclarée {@code final} et ne peut pas être instanciée.
 *
 * @author Matisse SENECHAL
 * @version 2.0
 */
public final class ResponseStatusConstants {

    /**
     * Constructeur privé pour empêcher l’instanciation de cette classe utilitaire.
     */
    private ResponseStatusConstants() {

    }

    /** Statut de succès à utiliser dans les réponses XML d’insertion. */
    public static final String INSERTED = "INSERTED";

    /** Statut de succès à utiliser dans les réponses XML de suppression. */
    public static final String DELETED = "DELETED";

    /** Statut d’échec à utiliser dans les réponses XML en cas d’erreur. */
    public static final String ERROR = "ERROR";
}