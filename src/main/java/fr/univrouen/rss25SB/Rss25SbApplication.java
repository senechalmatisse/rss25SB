package fr.univrouen.rss25SB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import fr.univrouen.rss25SB.client.config.Rss25SBClientProperties;

/**
 * Classe principale de l’application Spring Boot RSS 2.5 SB.
 * <p>
 * Cette classe sert de point d’entrée à l’exécution de l’application.
 * Elle configure automatiquement le contexte Spring et déclenche le lancement
 * du serveur embarqué (Tomcat, Jetty, ...).
 * </p>
 *
 * <p>
 * Le lancement de cette classe déclenchera l’exécution du projet, y compris :
 * </p>
 * <ul>
 *     <li>Le démarrage de l’API REST de gestion des flux rss25SB</li>
 *     <li>Le chargement du client d’envoi de flux (outil de transfert)</li>
 *     <li>L’accès aux fonctionnalités de conversion et d’affichage</li>
 * </ul>
 *
 * @author Matisse SENECHAL
 * @version 2.0
 */
@SpringBootApplication
@EnableConfigurationProperties(Rss25SBClientProperties.class)
public class Rss25SbApplication {

    /**
     * Méthode principale (main) de l’application.
     * <p>
     * Elle utilise {@link SpringApplication#run(Class, String...)} pour
     * lancer l’environnement Spring Boot et exposer les endpoints REST.
     * </p>
     *
     * @param args arguments de la ligne de commande, inutilisés ici
     */
	public static void main(String[] args) {
		SpringApplication.run(Rss25SbApplication.class, args);
    }
}