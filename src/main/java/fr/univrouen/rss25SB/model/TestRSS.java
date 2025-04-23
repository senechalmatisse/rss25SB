package fr.univrouen.rss25SB.model;

import org.springframework.core.io.*;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class TestRSS {
    private final Resource resource;

    public TestRSS() {
        this.resource = new DefaultResourceLoader().getResource("classpath:xml/item.xml");
    }

    public String loadFileXML() {
        StringBuilder content = new StringBuilder();
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (IOException e) {
            return "<error>Erreur lors du chargement du fichier XML: " + e.getMessage() + "</error>";
        }

        return content.toString();
    }
}