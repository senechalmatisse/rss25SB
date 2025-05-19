package fr.univrouen.rss25SB.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;

import org.springframework.http.*;

import fr.univrouen.rss25SB.model.db.ItemEntity;
import fr.univrouen.rss25SB.service.ItemService;
import fr.univrouen.rss25SB.utils.HtmlRenderer;
import lombok.AllArgsConstructor;

/**
 * Contrôleur REST exposant un endpoint pour afficher le détail complet d’un article
 * RSS au format HTML, selon son identifiant.
 * <p>
 * Ce contrôleur utilise le moteur Thymeleaf pour générer dynamiquement une page HTML
 * contenant toutes les informations associées à un {@link ItemEntity}, telles que :
 * </p>
 * 
 * <p>Les informations affichées peuvent inclure :</p>
 * <ul>
 *   <li>le titre,</li>
 *   <li>le guid (lien unique),</li>
 *   <li>les dates (publication, mise à jour),</li>
 *   <li>les catégories, auteurs, contributeurs, image et contenu.</li>
 * </ul>
 * 
 * <p>En cas d’erreur (identifiant non trouvé), une page HTML contenant un message d’erreur
 * avec l’ID recherché est renvoyée.
 *
 * @author Matisse SENECHAL
 * @version 1.1
 */
@AllArgsConstructor
@RestController
@RequestMapping("/rss25SB")
public class ItemController {

    /** Service métier permettant d'accéder aux articles résumés stockés en base. */
    private final ItemService itemService;

    /** Moteur de rendu HTML (basé sur Thymeleaf) pour afficher les vues HTML. */
    private final HtmlRenderer htmlRenderer;

    /**
     * Endpoint GET permettant d’obtenir un article complet au format HTML à partir de son identifiant.
     * <p>
     * Le comportement est le suivant :
     * <ul>
     *   <li>Si l’article est trouvé : on insère l’objet {@link ItemEntity} dans le modèle,
     *       puis on rend la vue "item.html".</li>
     *   <li>Si l’article est introuvable : on injecte les variables "id" et "error = true"
     *       dans le modèle pour afficher un message d’erreur dans la vue.</li>
     * </ul>
     * </p>
     *
     * @param id identifiant de l’article à rechercher en base
     * @return {@link ResponseEntity} contenant le code HTTP 200 avec la page HTML générée
     *
     * <p><b>URL :</b> <code>/rss25SB/html/{id}</code></p>
     * <p><b>Méthode :</b> GET</p>
     * <p><b>Produit :</b> text/html</p>
     */
    @GetMapping(value = "/html/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getItemByIdAsHTML(@PathVariable Long id) {
        // Recherche de l’article en base
        Optional<ItemEntity> itemOptional = itemService.getItemById(id);

        // Si l’article existe, on ajoute l’objet au modèle
        // sinon on insère un drapeau d’erreur
        Map<String, Object> variables = itemOptional.isPresent()
                ? Map.of("item", itemOptional.get())
                : Map.of(
                    "error", true,
                    "id", id,
                    "message", "L’article avec l’identifiant " + id + " est introuvable."
                );

        // Génération du HTML via le moteur de template
        String html = htmlRenderer.render("item", variables);

        HttpStatus status = itemOptional.isPresent()
                                            ? HttpStatus.OK
                                            : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(html);
    }
}