package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class PhraseBookFactory extends ResourceFactory<PhraseBook> {

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
    public String keyName() {
        return "locale";
    }

    @Override
    public PhraseBook newResource() {
        return new PhraseBook(this.repository, this, this.factory);
    }

    @Override
    public String defaultSearchParameterName() {
        return keyName();
    }

}
