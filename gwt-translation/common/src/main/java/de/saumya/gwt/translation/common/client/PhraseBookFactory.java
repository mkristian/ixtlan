package de.saumya.gwt.translation.common.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class PhraseBookFactory extends ResourceFactory<PhraseBook> {

    private final PhraseFactory factory;

    public PhraseBookFactory(final Repository repository,
            final PhraseFactory factory) {
        super(repository);
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

}
