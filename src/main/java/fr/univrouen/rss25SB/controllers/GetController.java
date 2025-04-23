package fr.univrouen.rss25SB.controllers;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import fr.univrouen.rss25SB.model.Item;

@RestController
public class GetController {

    @GetMapping("/resume")
    public String getListRSSinXML() {
        return "Envoi de la liste des flux RSS";
    }

    @GetMapping("/guid")
    public String getRSSinXML(@RequestParam(value = "guid") String texte) {
        return ("Détail du flux RSS demandé : " + texte);
    }

    @GetMapping(value = "/test")
    public String testParams(
        @RequestParam(value = "nb") int nb,
        @RequestParam(value = "search") String search) {

        return "Test :<br>guid = " + nb + "<br>titre = " + search;
    }

    @RequestMapping(value = "/xml",
                    produces = MediaType.APPLICATION_XML_VALUE)
    public @ResponseBody Item getXML() {
        Item it = new Item("12345678","Test item","2022-05-01T11:22:33");
        return it;
    }
}