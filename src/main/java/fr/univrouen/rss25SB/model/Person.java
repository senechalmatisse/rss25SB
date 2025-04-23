package fr.univrouen.rss25SB.model;

import jakarta.xml.bind.annotation.*;
import jakarta.validation.constraints.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
public abstract class Person {
    @XmlAttribute(name = "name", required = true)
    private String name;

    @XmlAttribute(name = "email")
    @Pattern(regexp = "[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z]{2,}", message = "Invalid email format")
    private String email;

    @XmlAttribute(name = "uri")
    @Pattern(regexp = "[^\\s\\-_~@]+(\\.[^\\s\\-_~@]+)*", message = "Invalid URI format")
    private String uri;

    public Person(String name, String email, String uri) {
        this.name = name;
        this.email = email;
        this.uri = uri;
    }

    public Person() {}
}