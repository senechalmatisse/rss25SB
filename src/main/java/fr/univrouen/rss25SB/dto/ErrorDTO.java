package fr.univrouen.rss25SB.dto;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "error")
public class ErrorDTO {

    private int id;
    private String status;

    public ErrorDTO() {}

    public ErrorDTO(int id, String status) {
        this.id = id;
        this.status = status;
    }

    @XmlElement
    public int getId() {
        return id;
    }

    @XmlElement
    public String getStatus() {
        return status;
    }
}