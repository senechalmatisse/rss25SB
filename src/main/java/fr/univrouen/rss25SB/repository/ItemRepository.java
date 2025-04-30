package fr.univrouen.rss25SB.repository;

import fr.univrouen.rss25SB.model.db.ItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface de persistance pour l’entité {@link ItemEntity}.
 * <p>
 * Étend {@link JpaRepository} pour fournir automatiquement
 * les opérations CRUD standard (create, read, update, delete),
 * ainsi que des méthodes de pagination et de tri.
 * </p>
 *
 * <p>Ce repository permet notamment de :</p>
 * <ul>
 *     <li>récupérer tous les articles via {@code findAll()}</li>
 *     <li>trouver un article par son identifiant via {@code findById(Long)}</li>
 *     <li>supprimer ou enregistrer un article</li>
 * </ul>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 * @see ItemEntity
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {
}