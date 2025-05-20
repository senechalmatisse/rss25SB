package fr.univrouen.rss25SB.service;

import org.springframework.stereotype.Service;

import fr.univrouen.rss25SB.model.OperationInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Service fournissant la liste des opérations disponibles sur l'API RSS25SB.
 * <p>
 * Ce service centralise la description détaillée des endpoints REST proposés par le projet.
 * Il est utilisé pour alimenter dynamiquement la page d’aide (/help).
 * </p>
 * 
 * @author Matisse SENECHAL
 * @version 1.2
 */
@Slf4j
@Service
public class HelpInfoService {

    /**
     * Retourne la liste des opérations exposées par l'API REST.
     * Chaque opération contient son URL, sa méthode HTTP et une description
     * précisant les formats acceptés, les objectifs, et le format de retour.
     * 
     * @return une liste d'objets {@link OperationInfo}
     */
    public List<OperationInfo> getOperations() {
        log.debug("Début récupération de la liste des opérations d'aide");
        List<OperationInfo> operations = new ArrayList<>();

        operations.add(new OperationInfo(
            "/", "GET", "Affiche la page d’accueil du service RSS25SB (format HTML)."));

        operations.add(new OperationInfo(
            "/help", "GET", "Affiche la page d’aide avec la liste des opérations disponibles (format HTML)."));

        operations.add(new OperationInfo(
            "/rss25SB/resume/xml", "GET",
            "Retourne la liste des articles disponibles sous forme synthétique (id, date, guid) au format XML."));

        operations.add(new OperationInfo(
            "/rss25SB/resume/html", "GET",
            "Retourne la liste synthétique des articles sous forme HTML (via transformation XSLT)."));

        operations.add(new OperationInfo(
            "/rss25SB/resume/xml/{id}", "GET",
            "Affiche un article complet au format XML. L’identifiant doit être valide."));

        operations.add(new OperationInfo(
            "/rss25SB/html/{id}", "GET",
            "Affiche un article complet en HTML via transformation XSLT à partir du XML identifié."));

        operations.add(new OperationInfo(
            "/rss25SB/insert", "POST",
            "Insère un flux XML conforme au XSD rss25SB. Le flux doit être envoyé en format XML (Content-Type: application/xml). "
            + "Retourne un statut XML indiquant le succès ou l’échec."));

        operations.add(new OperationInfo("/rss25SB/insert", "GET", "Affiche un formulaire HTML pour téléverser un fichier XML local (multipart/form-data)."));

        operations.add(new OperationInfo("/rss25SB/insert/html", "POST", "Traite un fichier XML envoyé via formulaire (multipart/form-data), effectue une insertion après validation ou conversion, "
                + "et affiche un retour HTML via transformation XSLT."));

        operations.add(new OperationInfo(
            "/rss25SB/delete/{id}", "DELETE",
            "Supprime l’article identifié par l’id fourni. "
            + "Retourne un flux XML indiquant le statut de suppression (DELETED ou ERROR)."));

        log.debug("Liste des opérations construite ({} éléments)", operations.size());
        return operations;
    }
}