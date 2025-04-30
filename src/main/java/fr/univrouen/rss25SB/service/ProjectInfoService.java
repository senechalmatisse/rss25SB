package fr.univrouen.rss25SB.service;

import org.springframework.stereotype.Service;
import fr.univrouen.rss25SB.model.ProjectInfo;

/**
 * Service fournissant les informations générales du projet RSS25SB.
 * <p>
 * Cette classe centralise les métadonnées telles que le nom du projet,
 * la version, le développeur et le chemin du logo, utilisées notamment
 * pour l'affichage sur la page d'accueil.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Service
public class ProjectInfoService {

    /**
     * Retourne un objet {@link ProjectInfo} contenant les informations statiques du projet.
     * 
     * @return les métadonnées du projet encapsulées dans une instance de {@link ProjectInfo}
     */
    public ProjectInfo getProjectInfo() {
        return new ProjectInfo(
            "rss25SB",                          // Nom du projet
            "1.0",                           // Version du projet
            "Matisse Senechal",            // Nom du développeur
            "/logo_univ_rouen.png"          // Chemin relatif du logo
        );
    }
}