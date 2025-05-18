package fr.univrouen.rss25SB.repository;

import fr.univrouen.rss25SB.model.db.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
}