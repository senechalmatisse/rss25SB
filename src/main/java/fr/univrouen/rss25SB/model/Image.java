package fr.univrouen.rss25SB.model;

import jakarta.xml.bind.annotation.*;
import jakarta.validation.constraints.Pattern;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "image")
public class Image {
    @XmlAttribute(name = "type", required = true)
    @Pattern(regexp = "image/(GIF|JPEG|JPG|BMP|PNG)",
             message = "Type invalide. Doit être image/GIF, image/JPEG, image/JPG, image/BMP ou image/PNG")
    private String type;

    @XmlAttribute(name = "href", required = true)
    @Pattern(regexp = "[^\\s\\-_~@]+(\\.[^\\s\\-_~@]+)*",
            message = "Href invalide selon la RFC 3987")
    private String href;

    @XmlAttribute(name = "alt", required = true)
    private String alt;

    @XmlAttribute(name = "length")
    private Integer length;

    public Image(String type, String href, String alt, Integer length) {
        this.type = type;
        this.href = href;
        this.alt = alt;
        this.length = length;
    }

    public Image(String type, String href, String alt) {
        this(type, href, alt, null);
    }

    @Override
    public String toString() {
        return "Image{" +
                "type='" + type + '\'' +
                ", href='" + href + '\'' +
                ", alt='" + alt + '\'' +
                ", length=" + length +
                '}';
    }
}