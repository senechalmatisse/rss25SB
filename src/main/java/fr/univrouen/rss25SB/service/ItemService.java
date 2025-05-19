package fr.univrouen.rss25SB.service;

import fr.univrouen.rss25SB.dto.ItemSummaryDTO;
import fr.univrouen.rss25SB.model.db.ItemEntity;
import fr.univrouen.rss25SB.model.xml.Item;
import fr.univrouen.rss25SB.repository.ItemRepository;
import fr.univrouen.rss25SB.utils.*;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service métier responsable de la récupération et de la transformation
 * des entités {@code ItemEntity} en objets résumés {@link ItemSummaryDTO}.
 * <p>
 * Ce service permet d’exposer une vue simplifiée des articles RSS
 * en encapsulant la logique d’accès aux données et de formatage des dates.
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 3.0
 * @see ItemSummaryDTO
 * @see ItemRepository
 * @see Item
 */
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
        return itemRepository.findAll().stream()
                .map(entity -> new ItemSummaryDTO(
                        entity.getId(),
                        entity.getTitle(),
                        entity.getGuid(),
                        DateTimeUtil.formatToRfc3339(entity.getPublished())
                ))
                .collect(Collectors.toList());
    }

    /**
     * Recherche un article complet en base et le convertit au format XML JAXB.
     *
     * @param id identifiant de l’article
     * @return {@link Optional} contenant l’objet {@link Item} si trouvé, vide sinon
     */
    public Optional<Item> getItemAsXmlById(Long id) {
        return itemRepository.findById(id)
            .map(itemEntity -> ItemMapper.toXml(itemEntity));
    }

    /**
     * Récupère une entité brute {@link ItemEntity} depuis la base.
     * 
     * @param id identifiant de l’article
     * @return {@link Optional} avec l’entité si trouvée
     */
    public Optional<ItemEntity> getItemById(Long id) {
        return itemRepository.findById(id);
    }

    /**
     * Vérifie si un article avec le guid spécifié existe déjà en base.
     *
     * @param guid identifiant global unique de l’article (RFC 4122)
     * @return {@code true} si un article avec ce guid est déjà présent, sinon {@code false}
     */
    public boolean itemExists(String guid) {
        return itemRepository.existsByGuid(guid);
    }

    /**
     * Convertit un objet XML {@link Item}
     * en entité {@link ItemEntity} et l’enregistre en base.
     *
     * @param item objet XML à enregistrer
     * @return identifiant de l’article persisté
     */
    public Long saveItemFromXml(Item item) {
        ItemEntity entity = ItemMapper.toEntity(item);
        return itemRepository.save(entity).getId();
    }

    /**
     * Supprime un article à partir de son identifiant.
     * 
     * @param id identifiant de l’article à supprimer
     * @return {@code true} si la suppression a été effectuée, sinon {@code false}
     */
    public boolean deleteItemById(Long id) {
        if (!itemRepository.existsById(id)) {
            return false;
        }

        itemRepository.deleteById(id);

        return true;
    }
}