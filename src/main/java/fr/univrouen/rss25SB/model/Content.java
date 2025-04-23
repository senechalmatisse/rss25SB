package fr.univrouen.rss25SB.model;

import jakarta.xml.bind.annotation.*;

public class Content {
    @XmlAttribute(name = "type", required = true)
    private String type;

    @XmlAttribute(name = "src")
    private String src;
}
