package fr.univrouen.rss25SB.model;

import lombok.*;

/**
 * Représente les informations générales du projet RSS25SB à afficher sur la page d'accueil.
 * <p>
 * Cette classe est utilisée pour encapsuler les métadonnées du projet
 * telles que le nom, la version, le développeur et le chemin vers le logo.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter
@AllArgsConstructor
public class ProjectInfo {

    /** Nom du projet. */
    private final String name;

    /** Version du projet. */
    private final String version;

    /** Nom du ou des développeurs du projet. */
    private final String developer;

    /** Chemin relatif du logo de l’université à afficher. */
    private final String logoPath;
}