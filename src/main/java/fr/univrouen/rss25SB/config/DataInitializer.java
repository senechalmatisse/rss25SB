package fr.univrouen.rss25SB.config;

import fr.univrouen.rss25SB.model.db.*;
import fr.univrouen.rss25SB.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.*;

import java.time.OffsetDateTime;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional
    public CommandLineRunner initDatabase(
            ItemRepository itemRepository,
            CategoryRepository categoryRepository,
            AuthorRepository authorRepository,
            ContributorRepository contributorRepository
    ) {
        return args -> {

            // === Création d'un article ===
            ItemEntity item1 = new ItemEntity();
            item1.setTitle("Introduction à Spring Boot");
            item1.setGuid("https://example.com/article-spring");
            item1.setPublished(OffsetDateTime.parse("2025-05-18T08:00:00Z"));
            item1.setUpdated(OffsetDateTime.parse("2025-05-18T09:00:00Z"));

            // Image (embeddable)
            ImageEntity image = new ImageEntity();
            image.setType("image/PNG");
            image.setHref("https://example.com/images/spring.png");
            image.setAlt("Logo Spring");
            image.setLength(2048);
            item1.setImage(image);

            // Content (embeddable)
            ContentEntity content = new ContentEntity();
            content.setType("text");
            content.setSrc("https://example.com/article-spring#content");
            item1.setContent(content);

            // Catégories
            CategoryEntity cat1 = new CategoryEntity();
            cat1.setTerm("Framework");
            CategoryEntity cat2 = new CategoryEntity();
            cat2.setTerm("Java");
            item1.setCategories(List.of(cat1, cat2));

            // Auteurs
            AuthorEntity author = new AuthorEntity();
            author.setName("Natasha VeigorVision");
            author.setEmail("n.veigor@example.com");
            author.setUri("https://example.com/natasha");
            item1.setAuthors(List.of(author));

            // Contributeur (optionnel)
            ContributorEntity contributor = new ContributorEntity();
            contributor.setName("John Doe");
            contributor.setEmail("j.doe@example.com");
            contributor.setUri("https://example.com/johndoe");
            item1.setContributors(List.of(contributor));

            // Sauvegarde (cascade automatique)
            itemRepository.save(item1);
        };
    }
}