package fr.univrouen.rss25SB.utils;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

import lombok.NoArgsConstructor;

/**
 * Classe utilitaire fournissant des méthodes de manipulation et de formatage
 * des dates/horaires selon des standards reconnus, notamment RFC 3339.
 * <p>
 * Cette classe est non instanciable et utilise des méthodes statiques.
 * </p>
 *
 * <p><strong>Exemple de format RFC 3339 :</strong> {@code 2025-05-18T15:42:00+02:00}</p>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@NoArgsConstructor
public class DateTimeUtil {

    /**
     * Formatteur de date/heure conforme à la norme RFC 3339 (identique à ISO_OFFSET_DATE_TIME).
     * Utilisé pour convertir des objets {@link OffsetDateTime} en chaînes au format lisible.
     */
    private static final DateTimeFormatter RFC3339_FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * Formate un objet {@link OffsetDateTime} en chaîne conforme à la norme RFC 3339.
     *
     * @param dateTime la date à formater (avec fuseau horaire)
     * @return une chaîne représentant la date au format RFC 3339, ou lève une exception si l’entrée est invalide
     *
     * <p><b>Exemple :</b><br>
     * Entrée : {@code 2025-05-18T15:42:00+02:00}<br>
     * Sortie : {@code "2025-05-18T15:42:00+02:00"}
     * </p>
     */
    public static String formatToRfc3339(OffsetDateTime dateTime) {
        return RFC3339_FORMATTER.format(dateTime);
    }
}