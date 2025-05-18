package fr.univrouen.rss25SB;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Classe d’initialisation utilisée pour le déploiement de l’application Spring Boot
 * dans un conteneur de servlets externe (comme Tomcat ou Jetty) sous forme de WAR.
 * <p>
 * Elle étend {@link SpringBootServletInitializer} pour permettre l’intégration
 * avec un serveur d’applications Java EE, en appelant explicitement
 * la classe principale {@link Rss25SbApplication}.
 * </p>
 *
 * <p>Ce fichier est nécessaire si l’application est packagée en WAR au lieu de JAR :</p>
 * <pre>{@code
 * <packaging>war</packaging>
 * }</pre>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class ServletInitializer extends SpringBootServletInitializer {

    /**
     * Configure l’application Spring Boot pour le déploiement WAR.
     *
     * @param application le builder Spring à configurer
     * @return le builder modifié pointant vers {@link Rss25SbApplication}
     */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(Rss25SbApplication.class);
	}
}