package fr.univrouen.rss25SB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principale de l’application Spring Boot RSS 2.5 SB.
 * <p>
 * Cette classe contient la méthode {@code main} qui sert de point d’entrée
 * à l’application. Elle initialise et lance le contexte Spring Boot.
 * </p>
 *
 * <p>Annotation {@code @SpringBootApplication} :
 * <ul>
 *     <li>Active la configuration automatique de Spring</li>
 *     <li>Scanne les composants dans le package courant et les sous-packages</li>
 *     <li>Configure l’application en tant qu’application Spring Boot autonome</li>
 * </ul>
 * </p>
 *
 * <p>Exécution :</p>
 * <pre>{@code
 * $ mvn spring-boot:run
 * }</pre>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
@SpringBootApplication
public class Rss25SbApplication {

    /**
     * Point d’entrée principal de l’application RSS 2.5 SB.
     *
     * @param args arguments de la ligne de commande
     */
	public static void main(String[] args) {
        // Pour changer dynamiquement le port :
        // System.getProperties().put("server.port", 8100);
		SpringApplication.run(Rss25SbApplication.class, args);
    }
}