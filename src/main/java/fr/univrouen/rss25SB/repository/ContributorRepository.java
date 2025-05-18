package fr.univrouen.rss25SB.repository;

import fr.univrouen.rss25SB.model.db.ContributorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContributorRepository extends JpaRepository<ContributorEntity, Long> {
}