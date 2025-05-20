package fr.univrouen.rss25SB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import fr.univrouen.rss25SB.service.HelpInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
@RequiredArgsConstructor
@Slf4j
public class HelpController {

    /** Service métier injecté fournissant les informations d’aide pour l’interface utilisateur. */
    private final HelpInfoService helpInfoService;

    /**
     * Affiche la page d'aide listant les opérations REST disponibles.
     * 
     * @param model modèle de données Thymeleaf
     * @return le nom du template HTML à afficher
     */
    @GetMapping("/help")
    public String help(Model model) {
        log.debug("GET /help appelé");
        model.addAttribute("operations", helpInfoService.getOperations());
        return "help";
    }
}