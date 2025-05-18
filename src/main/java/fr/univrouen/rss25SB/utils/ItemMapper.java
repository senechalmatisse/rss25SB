package fr.univrouen.rss25SB.utils;

import fr.univrouen.rss25SB.model.db.ItemEntity;
import fr.univrouen.rss25SB.model.xml.*;

/**
 * Classe utilitaire chargée de faire la conversion entre le modèle
 * de persistance {@link ItemEntity} et le modèle XML JAXB {@link Item}.
 * <p>
 * Cette transformation est utilisée pour exposer les articles en réponse XML
 * dans le flux RSS personnalisé.
 * </p>
 *
 * <p>Le mappage couvre :</p>
 * <ul>
 *     <li>Champs simples : {@code guid}, {@code title}, {@code published}, {@code updated}</li>
 *     <li>Objets embarqués : {@link Content}, {@link Image}</li>
 *     <li>Listes : {@link Category}, {@link Author}, {@link Contributor}</li>
 * </ul>
 *
 * <p>Exemple d’usage :<br>
 * {@code Item itemXml = ItemMapper.toXml(itemEntity);}
 * </p>
 * 
 * @author Matisse SENECHAL
 * @version 1.0
 */
public class ItemMapper {

    /**
     * Convertit un objet {@link ItemEntity} (entité JPA) en {@link Item} (objet XML JAXB).
     *
     * @param entity l’entité représentant un article en base de données
     * @return un objet {@link Item} prêt à être sérialisé en XML
     */
    public static Item toXml(ItemEntity entity) {
        Item item = new Item();
        item.setGuid(entity.getGuid());
        item.setTitle(entity.getTitle());
        item.setPublished(entity.getPublished());
        item.setUpdated(entity.getUpdated());

        if (entity.getContent() != null) {
            Content content = new Content();
            content.setType(entity.getContent().getType());
            content.setSrc(entity.getContent().getSrc());
            item.setContent(content);
        }

        if (entity.getImage() != null) {
            Image image = new Image();
            image.setType(entity.getImage().getType());
            image.setHref(entity.getImage().getHref());
            image.setAlt(entity.getImage().getAlt());
            image.setLength(entity.getImage().getLength());
            item.setImage(image);
        }

        entity.getCategories().forEach(cat -> {
            Category category = new Category();
            category.setTerm(cat.getTerm());
            item.getCategory().add(category);
        });

        entity.getAuthors().forEach(authorEntity -> {
            Author author = new Author();
            author.setName(authorEntity.getName());
            author.setEmail(authorEntity.getEmail());
            author.setUri(authorEntity.getUri());
            item.getAuthorOrContributor().add(author);
        });

        entity.getContributors().forEach(contributorEntity -> {
            Contributor contributor = new Contributor();
            contributor.setName(contributorEntity.getName());
            contributor.setEmail(contributorEntity.getEmail());
            contributor.setUri(contributorEntity.getUri());
            item.getAuthorOrContributor().add(contributor);
        });

        return item;
    }
}