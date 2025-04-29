package fr.univrouen.rss25SB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import fr.univrouen.rss25SB.model.ProjectInfo;
import fr.univrouen.rss25SB.service.ProjectInfoService;

/**
 * Contrôleur responsable de la page d'accueil du projet RSS25SB.
 * <p>
 * Ce contrôleur récupère les informations sur le projet via {@link ProjectInfoService}
 * et les transmet à la vue Thymeleaf pour affichage.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Controller
public class IndexController {

    /** Service injecté pour fournir les informations du projet. */
    private final ProjectInfoService projectInfoService;

    /**
     * Constructeur du contrôleur avec injection du service {@link ProjectInfoService}.
     * 
     * @param projectInfoService le service contenant les données du projet
     */
    public IndexController(ProjectInfoService projectInfoService) {
        this.projectInfoService = projectInfoService;
    }

    /**
     * Gère les requêtes HTTP GET vers la racine du site ("/").
     * 
     * @param model le modèle permettant de transmettre les données à la vue
     * @return le nom de la vue Thymeleaf à afficher ("index")
     */
    @GetMapping("/")
    public String index(Model model) {
        ProjectInfo projectInfo = projectInfoService.getProjectInfo();
        model.addAttribute("project", projectInfo);
        return "index";
    }
}