package fr.univrouen.rss25SB.converter;

import fr.univrouen.rss25SB.converter.sources.LeMondeFluxConverter;
import fr.univrouen.rss25SB.model.xml.Feed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

/**
 * Composant chargé d’identifier la source d’un flux RSS externe (non conforme au format {@code rss25SB})
 * et de déléguer sa conversion à un convertisseur spécifique.
 * <p>
 * Ce sélecteur agit comme un point d’entrée unique pour la détection et la transformation de flux
 * provenant de plateformes tierces telles que Le Monde, AFP, etc.
 * </p>
 *
 * @author Matisse SENECHAL
 * @version 1.0
 * @see LeMondeFluxConverter
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class FluxSourceSelector {

    /**
     * Convertisseur dédié aux flux RSS provenant du site {@code lemonde.fr}.
     * Utilisé pour transformer automatiquement un flux Le Monde en un flux {@link Feed} conforme.
     */
    private final LeMondeFluxConverter leMondeFluxConverter;

    /**
     * Analyse un flux RSS brut et sélectionne dynamiquement un convertisseur adapté à sa source.
     *
     * @param rawXml le contenu brut du flux XML externe à convertir
     * @return un objet {@link Feed} conforme au format {@code rss25SB}
     * @throws UnsupportedOperationException si la source du flux est inconnue ou non gérée
     */
    public Feed convert(String rawXml) {
        log.debug("FluxSourceSelector.convert() appelé, taille du XML = {} caractères", rawXml.length());
        if (rawXml.contains("lemonde.fr")) {
            log.debug("Source détectée : LeMonde.fr -> utilisation de LeMondeFluxConverter");
            Feed feed = leMondeFluxConverter.convert(rawXml);
            log.debug("Conversion via LeMondeFluxConverter réussie, {} articles obtenus", feed.getItem().size());
            return feed;
        }

        String err = "Flux source non reconnu ou non supporté.";
        log.warn("FluxSourceSelector.convert() : {}", err);
        throw new UnsupportedOperationException(err);
    }
}