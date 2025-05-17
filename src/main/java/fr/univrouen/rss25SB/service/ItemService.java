package fr.univrouen.rss25SB.service;

import fr.univrouen.rss25SB.dto.ItemSummaryDTO;
import fr.univrouen.rss25SB.repository.ItemRepository;
import fr.univrouen.rss25SB.utils.DateTimeUtil;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
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
 * @version 1.0
 * @see ItemSummaryDTO
 * @see ItemRepository
 */
@AllArgsConstructor
@Service
public class ItemService {

    /** Repository JPA permettant l’accès aux entités {@code ItemEntity} en base de données. */
    private final ItemRepository itemRepository;

    /**
     * Récupère tous les articles stockés en base de données, puis les convertit
     * en objets {@link ItemSummaryDTO}, contenant uniquement les informations :
     * <ul>
     *     <li>id : identifiant unique</li>
     *     <li>title : titre de l’article</li>
     *     <li>date : date de publication (au format RFC 3339)</li>
     *     <li>guid : identifiant global</li>
     * </ul>
     *
     * @return une liste d’objets {@link ItemSummaryDTO}
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
}