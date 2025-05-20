package fr.univrouen.rss25SB.client.config;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rss25sb.server")
public class Rss25SBClientProperties {

    /** URL complète prioritaire */
    private String fullUrl;

    /** Hôte du serveur REST */
    private String host;

    /** Port du serveur REST */
    private Integer port;

    /** Endpoint REST à appeler */
    private String endpoint;

    /**
     * Construit dynamiquement l’URL du service REST en utilisant la propriété `fullUrl` si présente.
     * Sinon, recompose l’URL depuis host, port et endpoint.
     *
     * @return URL complète
     */
    public String getEffectiveUrl() {
        if (fullUrl != null && !fullUrl.isBlank()) {
            log.debug("Utilisation de l’URL explicite fournie : {}", fullUrl);
            return fullUrl;
        }

        if (host == null || endpoint == null) {
            log.warn("Impossible de construire l'URL (host ou endpoint manquants)");
            return null;
        }

        StringBuilder url = new StringBuilder(host);
        if (port != null && port > 0 && !host.startsWith("https")) {
            url.append(":").append(port);
        }
        url.append(endpoint);

        String result = url.toString();
        log.debug("URL du service REST construite dynamiquement : {}", result);
        return result;
    }
}