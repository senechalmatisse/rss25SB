package fr.univrouen.rss25SB.service;

import fr.univrouen.rss25SB.dto.ItemSummaryDTO;
import fr.univrouen.rss25SB.model.db.ItemEntity;
import fr.univrouen.rss25SB.model.xml.Item;
import fr.univrouen.rss25SB.repository.ItemRepository;
import fr.univrouen.rss25SB.utils.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service métier responsable de la gestion des articles RSS dans la base de données.
 * <p>
 * Cette classe encapsule toutes les opérations liées à la persistance,
 * la récupération et la transformation des entités {@link ItemEntity},
 * ainsi que leur conversion vers ou depuis le format XML ({@link Item}).
 * </p>
 *
 * <p>Les responsabilités couvertes incluent :</p>
 * <ul>
 *     <li>Chargement des articles sous forme résumée ou complète</li>
 *     <li>Insertion transactionnelle de nouveaux articles</li>
 *     <li>Conversion entre entités base de données et objets JAXB XML</li>
 *     <li>Suppression sécurisée d’articles</li>
 *     <li>Validation d’existence via GUID</li>
 * </ul>
 *
 * <p>Ce service garantit également le respect de la structure du modèle RSS25SB
 * via la conversion centralisée et l’application des règles métiers.</p>
 *
 * @author Matisse SENECHAL
 * @version 3.0
 * @see ItemRepository
 * @see ItemEntity
 * @see Item
 * @see ItemSummaryDTO
 */
@Slf4j
@AllArgsConstructor
@Service
public class ItemService {

    /** Repository JPA permettant l’accès aux entités {@code ItemEntity} en base de données. */
    private final ItemRepository itemRepository;

    /**
     * Récupère tous les articles stockés en base, puis les convertit
     * en objets {@link ItemSummaryDTO}, contenant uniquement :
     * <ul>
     *     <li>ID</li>
     *     <li>Title</li>
     *     <li>GUID</li>
     *     <li>Date (formatée RFC3339)</li>
     * </ul>
     *
     * @return liste d’articles sous forme résumée
     */
    public List<ItemSummaryDTO> getAllItemSummaries() {
        log.debug("Chargement de tous les résumés d'articles depuis la base");

        List<ItemSummaryDTO> summaries = itemRepository.findAll().stream()
            .map(entity -> new ItemSummaryDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getGuid(),
                DateTimeUtil.formatToRfc3339(entity.getPublished())
            ))
            .collect(Collectors.toList());

        log.info("{} résumés d'articles récupérés", summaries.size());
        return summaries;
    }

    /**
     * Recherche un article complet en base et le convertit au format XML JAXB.
     *
     * @param id identifiant de l’article
     * @return {@link Optional} contenant l’objet {@link Item} si trouvé, vide sinon
     */
    public Optional<Item> getItemAsXmlById(Long id) {
        log.debug("Recherche de l'article XML avec l'ID {}", id);
        Optional<Item> item = itemRepository.findById(id)
            .map(ItemMapper::toXml);

        if (item.isPresent()) {
            log.info("Article {} trouvé pour export XML", id);
        } else {
            log.warn("Article {} introuvable pour export XML", id);
        }

        return item;
    }

    /**
     * Récupère une entité brute {@link ItemEntity} depuis la base.
     * 
     * @param id identifiant de l’article
     * @return {@link Optional} avec l’entité si trouvée
     */
    public Optional<ItemEntity> getItemById(Long id) {
        log.debug("Recherche de l'article Entity avec l'ID {}", id);
        Optional<ItemEntity> entity = itemRepository.findById(id);

        if (entity.isPresent()) {
            log.info("Article {} trouvé en base", id);
        } else {
            log.warn("Article {} introuvable en base", id);
        }

        return entity;
    }

    /**
     * Vérifie si un article avec le guid spécifié existe déjà en base.
     *
     * @param guid identifiant global unique de l’article (RFC 4122)
     * @return {@code true} si un article avec ce guid est déjà présent, sinon {@code false}
     */
    public boolean itemExists(String guid) {
        boolean exists = itemRepository.existsByGuid(guid);

        log.debug("Vérification existence pour GUID '{}': {}", guid, exists);

        return exists;
    }

    /**
     * Convertit un objet XML {@link Item}
     * en entité {@link ItemEntity} et l’enregistre en base.
     *
     * @param item objet XML à enregistrer
     * @return identifiant de l’article persisté
     */
    public Long saveItemFromXml(Item item) {
        log.debug("Enregistrement d'un nouvel article GUID='{}'", item.getGuid());

        Long id = itemRepository.save(ItemMapper.toEntity(item)).getId();
        log.info("Article inséré avec ID {}", id);

        return id;
    }

    /**
     * Supprime un article à partir de son identifiant.
     * 
     * @param id identifiant de l’article à supprimer
     * @return {@code true} si la suppression a été effectuée, sinon {@code false}
     */
    public boolean deleteItemById(Long id) {
        log.debug("Suppression de l'article ID {}", id);

        if (!itemRepository.existsById(id)) {
            log.warn("Impossible de supprimer l'article {}: introuvable", id);
            return false;
        }

        itemRepository.deleteById(id);
        log.info("Article {} supprimé avec succès", id);

        return true;
    }

    /**
     * Enregistre une liste d'articles en base de façon atomique.
     * <p>
     * Cette opération est transactionnelle : si l’insertion d’un seul élément échoue,
     * l’ensemble de la transaction est annulé.
     * Tous les objets doivent avoir été validés ou convertis préalablement.
     * </p>
     *
     * @param items liste d'entités {@link ItemEntity} à insérer
     * @return liste des identifiants des articles insérés
     *
     * @throws org.springframework.dao.DataIntegrityViolationException
     *         si une contrainte de base est violée (ex : champ trop long, duplicata)
     */
    @Transactional
    public List<Long> saveAllItems(List<ItemEntity> items) {
        log.debug("Insertion transactionnelle de {} articles", items.size());

        List<ItemEntity> savedEntities = itemRepository.saveAll(items); // insertion groupée

        List<Long> ids = savedEntities.stream()
            .map(ItemEntity::getId)
            .toList();

        log.info("Articles insérés avec succès : {}", ids);
        return ids;
    }
}