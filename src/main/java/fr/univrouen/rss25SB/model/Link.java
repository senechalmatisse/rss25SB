package fr.univrouen.rss25SB.model;

import jakarta.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
public class Link {
    @XmlAttribute(name = "rel", required = true)
    private String rel;

    @XmlAttribute(name = "type", required = true)
    private String type;

    @XmlAttribute(name = "href", required = true)
    private String href;
}