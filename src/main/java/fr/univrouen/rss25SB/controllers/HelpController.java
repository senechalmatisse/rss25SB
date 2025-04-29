package fr.univrouen.rss25SB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import fr.univrouen.rss25SB.service.HelpInfoService;

/**
 * Contrôleur Spring Boot pour la gestion de la page d'aide (/help).
 * <p>
 * Il expose une page listant l'ensemble des opérations disponibles
 * sur l'API REST RSS25SB avec leurs détails (URL, méthode, description).
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Controller
public class HelpController {

    /** Service métier injecté fournissant les informations d’aide pour l’interface utilisateur. */
    private final HelpInfoService helpInfoService;

    /**
     * Constructeur avec injection du service {@link HelpInfoService}.
     * 
     * @param helpInfoService service fournissant les informations d'aide
     */
    public HelpController(HelpInfoService helpInfoService) {
        this.helpInfoService = helpInfoService;
    }

    /**
     * Affiche la page d'aide listant les opérations REST disponibles.
     * 
     * @param model modèle de données Thymeleaf
     * @return le nom du template HTML à afficher
     */
    @GetMapping("/help")
    public String help(Model model) {
        model.addAttribute("operations", helpInfoService.getOperations());
        return "help";
    }
}