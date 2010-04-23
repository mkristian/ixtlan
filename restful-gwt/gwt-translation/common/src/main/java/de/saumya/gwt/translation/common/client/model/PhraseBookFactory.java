package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithIdGenerator;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class PhraseBookFactory extends
        ResourceFactoryWithIdGenerator<PhraseBook> {

    private final PhraseFactory factory;

    public PhraseBookFactory(final Repository repository,
            final ResourceNotifications notification,
            final PhraseFactory factory) {
        super(repository, notification);
        this.factory = factory;
    }

    @Override
    public String storageName() {
        return "phrase_book";
    }

    @Override
    public PhraseBook newResource(final int key) {
        return new PhraseBook(this.repository, this, this.factory, key);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
