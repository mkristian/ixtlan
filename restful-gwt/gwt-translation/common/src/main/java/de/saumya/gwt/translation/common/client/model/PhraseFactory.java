/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;

public class PhraseFactory extends ResourceFactory<Phrase> {

    private final TranslationFactory translationFactory;
    private final UserFactory        factory;
    private final LocaleFactory      localeFactory;

    public PhraseFactory(final Repository repository,
            final ResourceNotifications notification,
            final UserFactory factory, final LocaleFactory localeFactory,
            final TranslationFactory translationFactory) {
        super(repository, notification);
        this.factory = factory;
        this.translationFactory = translationFactory;
        this.localeFactory = localeFactory;
    }

    @Override
    public String storageName() {
        return "phrase";
    }

    @Override
    public Phrase newResource(final int id) {
        return new Phrase(this.repository,
                this,
                this.translationFactory,
                this.factory,
                this.localeFactory,
                id);
    }

    @Override
    public String defaultSearchParameterName() {
        return "current_text";
    }

}