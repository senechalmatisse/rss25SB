package fr.univrouen.rss25SB.utils;

import java.util.regex.Pattern;

/**
 * Classe utilitaire pour valider les champs d’un article RSS selon les contraintes du schéma XSD.
 * <p>
 * Ce validateur permet notamment de vérifier la conformité :
 * <ul>
 *     <li>du champ {@code guid} avec la norme <strong>RFC4122</strong> (UUID sous forme d’URL),</li>
 *     <li>du champ {@code date} avec la norme <strong>RFC3339</strong> (format date-heure ISO avec fuseau horaire).</li>
 * </ul>
 * Cette classe ne peut pas être instanciée.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class ItemValidator {

    /** Expression régulière pour valider un GUID conforme à la RFC4122, sous forme d’URL HTTP/HTTPS. */
    private static final Pattern GUID_PATTERN = Pattern.compile(
        "^https?://[a-zA-Z0-9.-]+/[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$"
    );

    /** Expression régulière pour valider un GUID conforme à la RFC4122, sous forme d’URL HTTP/HTTPS. */
    private static final Pattern DATE_PATTERN = Pattern.compile(
        "^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(Z|[+-]\\d{2}:\\d{2})$"
    );

    /**
     * Constructeur privé pour empêcher l’instanciation de la classe utilitaire.
     */
    private ItemValidator() {
        // Classe utilitaire, ne pas instancier
    }

    /**
     * Vérifie que le champ {@code guid} respecte le format d’un identifiant unique RFC4122
     * sous forme d’URL (par exemple : {@code https://example.com/550e8400-e29b-41d4-a716-446655440000}).
     *
     * @param guid la chaîne à valider
     * @return {@code true} si le format est valide, {@code false} sinon
     */
    public static boolean isValidGuid(String guid) {
        return GUID_PATTERN.matcher(guid).matches();
    }

    /**
     * Vérifie que le champ {@code date} respecte le format RFC3339
     * (ex. : {@code 2025-04-30T14:12:00Z} ou {@code 2025-04-30T14:12:00+02:00}).
     *
     * @param date la chaîne à valider
     * @return {@code true} si la date est valide, {@code false} sinon
     */
    public static boolean isValidDate(String date) {
        return DATE_PATTERN.matcher(date).matches();
    }
}