package fr.univrouen.rss25SB.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Category {
    @XmlAttribute(name = "term", required = true)
    private String term;
}