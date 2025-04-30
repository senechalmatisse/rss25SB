package fr.univrouen.rss25SB.service;

import org.springframework.stereotype.Service;

import fr.univrouen.rss25SB.model.OperationInfo;

import java.util.*;

/**
 * Service fournissant la liste des opérations disponibles sur l'API RSS25SB.
 * <p>
 * Ce service centralise la description des endpoints REST proposés par le projet.
 * </p>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Service
public class HelpInfoService {

    /**
     * Retourne la liste des opérations exposées par l'API REST.
     * 
     * @return une liste d'objets {@link OperationInfo}
     */
    public List<OperationInfo> getOperations() {
        List<OperationInfo> operations = new ArrayList<>();
        
        operations.add(new OperationInfo("/", "GET", "Page d'accueil du projet"));
        operations.add(new OperationInfo("/help", "GET", "Affiche cette page d'aide"));
        operations.add(new OperationInfo("/rss25SB/resume/xml", "GET", "Affiche la liste des articles au format XML"));
        operations.add(new OperationInfo("/rss25SB/resume/html", "GET", "Affiche la liste des articles au format HTML"));
        operations.add(new OperationInfo("/rss25SB/resume/xml/id", "GET", "Affiche un article précis au format XML"));
        operations.add(new OperationInfo("/rss25SB/html/id", "GET", "Affiche un article précis au format HTML"));
        operations.add(new OperationInfo("/rss25SB/insert", "POST", "Ajoute un nouvel article (XML validé par XSD)"));
        operations.add(new OperationInfo("/rss25SB/delete/id", "DELETE", "Supprime un article par son identifiant"));

        return operations;
    }
}