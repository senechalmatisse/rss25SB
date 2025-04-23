package fr.univrouen.rss25SB.model;

import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
public class Item {
    @XmlElement(name = "guid", required = true)
    private String guid;

    @XmlElement(name = "title", required = true)
    private String title;

    @XmlElement(name = "category", required = true)
    private List<Category> categories;

    @XmlElement(name = "published")
    private String published;

    @XmlElement(name = "updated")
    private String updated;

    @XmlElement(name = "image")
    private Image image;

    @XmlElement(name = "content", required = true)
    private Content content;
        @XmlElements({
        @XmlElement(name = "author", type = Author.class),
        @XmlElement(name = "contributor", type = Contributor.class)
    })
    private List<Person> people;

    @XmlAccessorType(XmlAccessType.FIELD)
    class Category {
        @XmlAttribute(name = "term", required = true)
        private String term;
    }

    public Item(String guid, String title, String published) {
        super();
        this.guid = guid;
        this.title = title;
        this.published = published;
    }

    public Item() {
    }

    @Override
    public String toString() {
        return ("Article : " + title + "\n(" + guid
        + ") Le = " + published );
    }
}