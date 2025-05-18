package fr.univrouen.rss25SB.model.adapter;

import fr.univrouen.rss25SB.utils.DateTimeUtil;
import jakarta.xml.bind.annotation.adapters.XmlAdapter;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Adaptateur JAXB permettant de convertir les objets {@link OffsetDateTime}
 * en chaînes de caractères au format RFC 3339, et inversement.
 * <p>
 * Cet adaptateur est utilisé avec l'annotation {@code @XmlJavaTypeAdapter}
 * pour permettre la sérialisation/désérialisation correcte des champs de type {@code OffsetDateTime}
 * dans les classes XML JAXB.
 * </p>
 *
 * <p>
 * Format utilisé : {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME}, conforme à RFC 3339.
 * Exemple : {@code "2025-05-18T16:45:00+02:00"}
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class OffsetDateTimeXmlAdapter extends XmlAdapter<String, OffsetDateTime> {

    /** Formatteur conforme à ISO_OFFSET_DATE_TIME (RFC 3339). */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    /**
     * Convertit une chaîne de texte au format RFC 3339 en {@link OffsetDateTime}.
     *
     * @param value la chaîne à convertir
     * @return un objet {@link OffsetDateTime}, ou {@code null} si la chaîne est vide ou nulle
     */
    @Override
    public OffsetDateTime unmarshal(String value) {
        return (value == null || value.isEmpty()) ? null : OffsetDateTime.parse(value, FORMATTER);
    }

    /**
     * Convertit un objet {@link OffsetDateTime} en chaîne au format RFC 3339.
     *
     * @param value la date à convertir
     * @return une chaîne formatée, ou {@code null} si l’objet est {@code null}
     */
    @Override
    public String marshal(OffsetDateTime value) {
        return (value == null) ? null : DateTimeUtil.formatToRfc3339(value);
    }
}