package fr.univrouen.rss25SB.utils;

/**
 * Classe utilitaire fournissant des fonctions d’assistance pour le traitement des chaînes de caractères.
 * <p>
 * Elle permet notamment de tronquer une chaîne de caractères si sa longueur dépasse une limite spécifiée,
 * en y ajoutant une terminaison « ... » pour indiquer qu’un contenu a été raccourci.
 * </p>
 *
 * <p>Cette classe est utilisée dans le projet pour s’assurer que les champs texte insérés en base
 * ne dépassent pas la taille maximale autorisée par les colonnes SQL (ex: {@code VARCHAR(255)}).</p>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class StringUtil {

    /**
     * Tronque une chaîne de caractères si sa longueur dépasse une taille maximale.
     * <p>
     * Si la chaîne dépasse {@code maxLength}, elle est coupée à {@code maxLength - 3} caractères
     * et terminée par « ... ».
     *
     * @param value     la chaîne de caractères à traiter (peut être {@code null})
     * @param maxLength la taille maximale autorisée (doit être ≥ 4 si tronquage attendu)
     * @return la chaîne tronquée si nécessaire, ou {@code null} si l’entrée était {@code null}
     */
    public static String truncate(String value, int maxLength) {
        if (value == null) return null;
        return value.length() > maxLength ? value.substring(0, maxLength - 3) + "..." : value;
    }
}