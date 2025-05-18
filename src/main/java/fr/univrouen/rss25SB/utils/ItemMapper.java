package fr.univrouen.rss25SB.utils;

import fr.univrouen.rss25SB.model.db.*;
import fr.univrouen.rss25SB.model.xml.*;

/**
 * Classe utilitaire chargée de faire la conversion bidirectionnelle entre :
 * <ul>
 *     <li>Le modèle de persistance {@link ItemEntity} (utilisé pour la base de données)</li>
 *     <li>Le modèle XML JAXB {@link Item} (utilisé pour le flux RSS)</li>
 * </ul>
 *
 * <p>Ce mapper permet d’assurer la cohérence des données entre la couche
 * base de données et la couche de communication XML, en gérant également les
 * objets imbriqués (catégories, auteurs, contributeurs, image, contenu, etc.).</p>
 *
 * <p>Les conversions incluent :</p>
 * <ul>
 *     <li>Champs simples : {@code title}, {@code guid}, {@code published}, {@code updated}</li>
 *     <li>Objets embarqués : {@link Content}, {@link Image}</li>
 *     <li>Collections : {@link Category}, {@link Author}, {@link Contributor}</li>
 * </ul>
 *
 * @author Matisse SENECHAL
 * @version 2.1
 */
public class ItemMapper {

    /**
     * Convertit une entité JPA {@link ItemEntity} en objet XML {@link Item}.
     *
     * @param entity l'entité à convertir
     * @return un objet {@link Item} prêt à être sérialisé en XML
     */
    public static Item toXml(ItemEntity entity) {
        Item item = new Item();
        item.setGuid(entity.getGuid());
        item.setTitle(entity.getTitle());
        item.setPublished(entity.getPublished());
        item.setUpdated(entity.getUpdated());

        // Conversion du contenu
        if (entity.getContent() != null) {
            item.setContent(toXmlContent(entity.getContent()));
        }

        // Conversion de l'image
        if (entity.getImage() != null) {
            item.setImage(toXmlImage(entity.getImage()));
        }

        // Conversion des catégories, auteurs et contributeurs
        entity.getCategories().forEach(
            cat -> item.getCategory().add(toXmlCategory(cat))
        );
        entity.getAuthors().forEach(
            author -> item.getAuthorOrContributor().add(toXmlAuthor(author))
        );
        entity.getContributors().forEach(
            contributor -> item.getAuthorOrContributor().add(toXmlContributor(contributor))
        );

        return item;
    }

    /**
     * Convertit un objet XML {@link Item} en entité JPA {@link ItemEntity}.
     * Cette méthode est utilisée pour stocker un article extrait d’un flux XML dans la base.
     *
     * @param item l’objet XML à convertir
     * @return une entité prête à être persistée
     * @throws IllegalArgumentException si aucune date n’est fournie
     */
    public static ItemEntity toEntity(Item item) {
        ItemEntity entity = new ItemEntity();
        entity.setGuid(item.getGuid());
        entity.setTitle(item.getTitle());

        // Gestion des dates : au moins une des deux est obligatoire
        if (item.getPublished() != null) {
            entity.setPublished(item.getPublished());
            entity.setUpdated(item.getUpdated());
        } else if (item.getUpdated() != null) {
            entity.setPublished(item.getUpdated());
            entity.setUpdated(item.getUpdated());
        } else {
            throw new IllegalArgumentException("Item must have at least a published or updated date.");
        }

        // Conversion du contenu
        if (item.getContent() != null) {
            entity.setContent(toEntityContent(item.getContent()));
        }

        // Conversion de l'image (seulement si elle est valide)
        if (isValidImage(item.getImage())) {
            entity.setImage(toEntityImage(item.getImage()));
        }

        // Conversion des catégories
        if (item.getCategory() != null) {
            entity.setCategories(
                item.getCategory().stream().map(ItemMapper::toEntityCategory).toList()
            );
        }

        // Conversion des auteurs et contributeurs
        if (item.getAuthorOrContributor() != null) {
            entity.setAuthors(item.getAuthorOrContributor().stream()
                .filter(Author.class::isInstance)
                .map(a -> toEntityAuthor((Author) a)).toList());

            entity.setContributors(item.getAuthorOrContributor().stream()
                .filter(Contributor.class::isInstance)
                .map(c -> toEntityContributor((Contributor) c)).toList());
        }

        return entity;
    }

    /**
     * Convertit un objet {@link ContentEntity} vers {@link Content}.
     *
     * @param content l'entité à convertir
     * @return un objet XML {@link Content}
     */
    private static Content toXmlContent(ContentEntity content) {
        Content xml = new Content();
        xml.setType(content.getType());
        xml.setSrc(content.getSrc());
        return xml;
    }

    /**
     * Convertit un objet {@link Content} vers {@link ContentEntity}.
     *
     * @param content l'objet XML à convertir
     * @return l'entité persistable {@link ContentEntity}
     */
    private static ContentEntity toEntityContent(Content content) {
        ContentEntity entity = new ContentEntity();
        entity.setType(content.getType());
        entity.setSrc(content.getSrc());
        return entity;
    }

    /**
     * Convertit un objet {@link ImageEntity} vers {@link Image}.
     *
     * @param image l'entité image de la base
     * @return l'image XML
     */
    private static Image toXmlImage(ImageEntity image) {
        Image xml = new Image();
        xml.setType(image.getType());
        xml.setHref(image.getHref());
        xml.setAlt(image.getAlt());
        xml.setLength(image.getLength());
        return xml;
    }

    /**
     * Convertit un objet {@link Image} vers {@link ImageEntity}.
     *
     * @param image l'image XML
     * @return l'entité {@link ImageEntity}
     */
    private static ImageEntity toEntityImage(Image image) {
        ImageEntity entity = new ImageEntity();
        entity.setType(image.getType());
        entity.setHref(image.getHref());
        entity.setAlt(image.getAlt());
        entity.setLength(image.getLength());
        return entity;
    }

    /**
     * Vérifie si une image XML est complète (tous les champs obligatoires présents).
     *
     * @param img image XML à valider
     * @return true si elle est valide, false sinon
     */
    private static boolean isValidImage(Image img) {
        return img != null &&
               img.getAlt() != null && !img.getAlt().isBlank() &&
               img.getHref() != null && !img.getHref().isBlank() &&
               img.getType() != null && !img.getType().isBlank();
    }

    /**
     * Convertit une entité {@link CategoryEntity} vers une catégorie XML.
     *
     * @param entity la catégorie en base
     * @return la catégorie XML {@link Category}
     */
    private static Category toXmlCategory(CategoryEntity entity) {
        Category xml = new Category();
        xml.setTerm(entity.getTerm());
        return xml;
    }

    /**
     * Convertit une catégorie XML {@link Category} vers {@link CategoryEntity}.
     *
     * @param xml la catégorie XML
     * @return la catégorie pour la base de données
     */
    private static CategoryEntity toEntityCategory(Category xml) {
        CategoryEntity entity = new CategoryEntity();
        entity.setTerm(xml.getTerm());
        return entity;
    }

    /**
     * Convertit une entité {@link AuthorEntity} vers {@link Author} XML.
     *
     * @param entity l’auteur en base
     * @return l’auteur XML
     */
    private static Author toXmlAuthor(AuthorEntity entity) {
        Author author = new Author();
        author.setName(entity.getName());
        author.setEmail(entity.getEmail());
        author.setUri(entity.getUri());
        return author;
    }

    /**
     * Convertit une entité {@link ContributorEntity} vers {@link Contributor} XML.
     *
     * @param entity le contributeur en base
     * @return le contributeur XML
     */
    private static Contributor toXmlContributor(ContributorEntity entity) {
        Contributor contributor = new Contributor();
        contributor.setName(entity.getName());
        contributor.setEmail(entity.getEmail());
        contributor.setUri(entity.getUri());
        return contributor;
    }

    /**
     * Convertit un objet {@link Author} XML vers une entité {@link AuthorEntity}.
     *
     * @param author l’auteur XML
     * @return l’entité persistable
     */
    private static AuthorEntity toEntityAuthor(Author author) {
        AuthorEntity entity = new AuthorEntity();
        entity.setName(author.getName());
        entity.setEmail(author.getEmail());
        entity.setUri(author.getUri());
        return entity;
    }

    /**
     * Convertit un objet {@link Contributor} XML vers une entité {@link ContributorEntity}.
     *
     * @param contributor le contributeur XML
     * @return l’entité persistable
     */
    private static ContributorEntity toEntityContributor(Contributor contributor) {
        ContributorEntity entity = new ContributorEntity();
        entity.setName(contributor.getName());
        entity.setEmail(contributor.getEmail());
        entity.setUri(contributor.getUri());
        return entity;
    }
}