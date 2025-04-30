package fr.univrouen.rss25SB.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import fr.univrouen.rss25SB.model.Item;

/**
 * Interface de repository JPA pour accéder aux entités {@link Item}.
 * <p>
 * Étend {@link JpaRepository} afin de bénéficier automatiquement des opérations
 * CRUD standard (findAll, findById, save, delete, etc.) sans implémentation manuelle.
 * </p>
 * 
 * <p>
 * Ce repository permet notamment de récupérer tous les articles stockés
 * ou d’en enregistrer de nouveaux dans la base PostgreSQL.
 *
 * @author Matisse SENECHAL
 * @version 1.0
 */
public interface ItemRepository extends JpaRepository<Item, Integer> {

}