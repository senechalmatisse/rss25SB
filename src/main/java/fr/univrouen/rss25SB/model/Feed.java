package fr.univrouen.rss25SB.model;

import jakarta.xml.bind.annotation.*;
import jakarta.validation.constraints.*;

import java.util.List;

@XmlRootElement(name = "feed", namespace = "http://univ.fr/rss25")
@XmlAccessorType(XmlAccessType.FIELD)
public class Feed {
    @XmlElement(name = "title", required = true)
    @NotBlank
    private String title;

    @XmlAttribute(name = "lang", required = true)
    @Pattern(regexp = "^[a-z]{2,3}(-[A-Z]{2}|-\\d{3})?$", 
             message = "Langue invalide")
    private String lang;

    @XmlAttribute(name = "version", required = true)
    @Pattern(regexp = "^25$",
             message = "La version doit être '25'")
    private String version;

    @XmlElement(name = "pubDate", required = true)
    @NotBlank
    private String pubDate;

    @XmlElement(name = "copyright", required = true)
    @NotBlank
    private String copyright;

    @XmlElement(name = "link")
    private List<Link> links;

    @XmlElement(name = "item")
    private List<Item> items;
}
