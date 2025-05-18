package fr.univrouen.rss25SB.repository;

import fr.univrouen.rss25SB.model.db.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
}