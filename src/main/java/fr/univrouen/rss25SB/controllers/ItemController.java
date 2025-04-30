package fr.univrouen.rss25SB.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import fr.univrouen.rss25SB.dto.ItemListDTO;
import fr.univrouen.rss25SB.service.ItemService;

/**
 * Contrôleur REST pour la gestion des articles RSS.
 * <p>
 * Ce contrôleur fournit une interface RESTful permettant de récupérer 
 * une liste résumée des articles RSS stockés dans la base de données, 
 * au format XML valide (XSD conforme).
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@RestController
public class ItemController {

    /** Service métier pour la gestion des articles RSS. */
    private final ItemService itemService;

    /**
     * Constructeur injectant le {@link ItemService}.
     *
     * @param itemService service permettant d'accéder aux données des articles
     */
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    /**
     * Endpoint GET exposant la liste synthétique des articles RSS.
     * <p>
     * Retourne un flux XML contenant les champs : <code>id</code>, <code>date</code> (RFC3339)
     * et <code>guid</code> (RFC4122), encapsulés dans un objet {@link ItemListDTO}.
     *
     * @return un objet {@link ItemListDTO} sérialisé au format XML
     */
    @GetMapping(value = "/rss25SB/resume/xml", produces = "application/xml")
    public ItemListDTO getAllItems() {
        return itemService.getAllItems();
    }

    @GetMapping(value = "/rss25SB/resume/html", produces = "text/html")
    public ModelAndView getAllItemsAsHtml(Model model) {
        ItemListDTO itemList = itemService.getAllItems();
        ModelAndView modelAndView = new ModelAndView("items");
        modelAndView.addObject("items", itemList.getItems());
        return modelAndView;
    }    
}