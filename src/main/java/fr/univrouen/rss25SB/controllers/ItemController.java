package fr.univrouen.rss25SB.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.*;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import fr.univrouen.rss25SB.model.db.ItemEntity;
import fr.univrouen.rss25SB.service.ItemService;
import lombok.AllArgsConstructor;

/**
 * Contrôleur REST exposant un endpoint pour afficher le détail complet d’un article
 * RSS au format HTML, selon son identifiant.
 * <p>
 * Ce contrôleur utilise le moteur Thymeleaf pour générer dynamiquement une page HTML
 * contenant toutes les informations associées à un {@link ItemEntity}, telles que :
 * <ul>
 *   <li>le titre,</li>
 *   <li>le guid (lien unique),</li>
 *   <li>les dates (publication, mise à jour),</li>
 *   <li>les catégories, auteurs, contributeurs, image et contenu.</li>
 * </ul>
 * En cas d’erreur (ID inexistant), une page HTML contenant l’ID demandé et un statut "ERROR"
 * est retournée à l’utilisateur.
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@AllArgsConstructor
@RestController
@RequestMapping("/rss25SB")
public class ItemController {

    /** Service métier permettant d'accéder aux articles résumés stockés en base. */
    private final ItemService itemService;

    /** Moteur de template Thymeleaf utilisé pour générer la vue HTML des articles. */
    private final TemplateEngine templateEngine;

    /**
     * Endpoint GET permettant d’obtenir un article complet au format HTML selon son identifiant.
     * <p>
     * Si l’article est trouvé, une vue HTML avec toutes ses informations est rendue.
     * Sinon, une vue d’erreur avec l’ID et le status "ERROR" est retournée.
     * </p>
     *
     * @param id identifiant de l’article à rechercher
     * @return {@link ResponseEntity} contenant la page HTML de l’article ou une erreur HTML
     *
     * <p><b>URL :</b> <code>/rss25SB/resume/html/{id}</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> text/html</p>
     */
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemByIdAsHTML(@PathVariable Long id) {
        Optional<ItemEntity> itemOptional = itemService.getItemById(id);

        Context context = new Context();
        if (itemOptional.isPresent()) {
            context.setVariable("item", itemOptional.get());
        } else {
            context.setVariable("error", true);
            context.setVariable("id", id);
        }

        String html = templateEngine.process("item", context);
        return ResponseEntity.ok(html);
    } 
}