/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.UserFactory;

public class PhraseFactory extends ResourceFactory<Phrase> {

    private final TranslationFactory translationFactory;
    private final UserFactory        factory;

    public PhraseFactory(final Repository repository,
            final UserFactory factory,
            final TranslationFactory translationFactory) {
        super(repository);
        this.factory = factory;
        this.translationFactory = translationFactory;
    }

    @Override
    public String storageName() {
        return "phrase";
    }

    @Override
    public String keyName() {
        return "id";
    }

    @Override
    public Phrase newResource() {
        return new Phrase(this.repository,
                this,
                this.translationFactory,
                this.factory);
    }

}