package fr.univrouen.rss25SB.utils;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.AllArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Composant utilitaire pour générer dynamiquement du contenu HTML
 * à partir de templates Thymeleaf.
 * <p>
 * Ce composant centralise le traitement des vues HTML du projet,
 * notamment dans les contrôleurs affichant des articles RSS au format HTML.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@AllArgsConstructor
@Component
public class HtmlRenderer {

    /** Moteur Thymeleaf injecté automatiquement par Spring. */
    private final TemplateEngine templateEngine;

    /**
     * Génère une page HTML à partir d’un template Thymeleaf et d’un ensemble de variables.
     *
     * @param templateName nom du template HTML
     * @param variables    map des paires clé-valeur à injecter dans le modèle Thymeleaf
     * @return le code HTML généré sous forme de chaîne
     */
    public String render(String templateName, Map<String, Object> variables) {
        // Création du contexte Thymeleaf et injection des variables
        Context context = new Context();
        context.setVariables(variables);

        // Traitement du template et retour du HTML rendu
        return templateEngine.process(templateName, context);
    }
}