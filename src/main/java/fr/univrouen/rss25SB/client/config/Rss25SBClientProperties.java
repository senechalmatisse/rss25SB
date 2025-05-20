package fr.univrouen.rss25SB.client.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration des propriétés client pour se connecter au service REST rss25SB.
 * <p>
 * Cette classe permet d'injecter dynamiquement les paramètres du serveur REST (host, port, endpoint)
 * à partir du fichier {@code application.properties} ou d'un fichier {@code .env}.
 * Les propriétés doivent être définies avec le préfixe {@code rss25sb.server}.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Getter @Setter
@Configuration
@ConfigurationProperties(prefix = "rss25sb.server")
public class Rss25SBClientProperties {

    /** Hôte du serveur REST */
    private String host;

    /** Port du serveur REST */
    private int port;

    /** Endpoint REST à appeler */
    private String endpoint;

    /**
     * Construit l’URL complète pour l’appel HTTP au service REST,
     * en combinant host, port et endpoint.
     *
     * @return URL complète du service REST
     */
    public String getFullUrl() {
        return host + ":" + port + endpoint;
    }
}