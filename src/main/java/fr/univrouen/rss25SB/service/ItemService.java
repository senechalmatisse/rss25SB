package fr.univrouen.rss25SB.service;

import fr.univrouen.rss25SB.dto.*;
import fr.univrouen.rss25SB.model.*;
import fr.univrouen.rss25SB.repository.ItemRepository;
import fr.univrouen.rss25SB.utils.ItemValidator;

import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service métier pour la gestion des articles RSS.
 * <p>
 * Fournit des opérations permettant de récupérer et valider
 * des articles au format conforme au XSD (RFC3339 pour les dates,
 * RFC4122 pour les GUID), ainsi que de les exposer sous forme de
 * {@link ItemDTO} pour export XML.
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
@Service
public class ItemService {

    /** Repository permettant l'accès aux entités {@link Item} en base. */
    private final ItemRepository itemRepository;

    /**
     * Constructeur du service avec injection du repository {@link ItemRepository}.
     *
     * @param itemRepository le repository Spring Data JPA pour les entités Item
     */
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Récupère tous les articles enregistrés en base de données
     * et les convertit en objets {@link ItemDTO} pour exposition XML.
     * <p>
     * La conversion s'accompagne d'une validation des champs :
     * <ul>
     *     <li>La date doit être au format RFC3339</li>
     *     <li>Le GUID doit être conforme à la RFC4122 (UUID dans URL)</li>
     * </ul>
     * En cas d'invalidité, une exception est levée.
     *
     * @return une instance de {@link ItemListDTO} contenant les résumés valides
     * @throws IllegalArgumentException si un article contient une date ou un GUID invalide
     */
    public ItemListDTO getAllItems() {
        List<ItemDTO> items = itemRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ItemListDTO(items);
    }

    /**
     * Convertit une entité {@link Item} vers un {@link ItemDTO},
     * avec formatage de la date (ISO_OFFSET_DATE_TIME) et validation
     * des champs (via {@link ItemValidator}).
     *
     * @param item l'article d'origine
     * @return un objet {@link ItemDTO} prêt à être exposé
     * @throws IllegalArgumentException si la date ou le GUID sont invalides
     */
    private ItemDTO convertToDTO(Item item) {
        String formattedDate = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(item.getDate());
        String guid = item.getGuid();

        if (!ItemValidator.isValidDate(formattedDate)) {
            throw new IllegalArgumentException("Date invalide pour l'article ID " + item.getId());
        }

        if (!ItemValidator.isValidGuid(guid)) {
            throw new IllegalArgumentException("GUID invalide pour l'article ID " + item.getId());
        }

        return new ItemDTO(item.getId(), formattedDate, guid);
    }
}