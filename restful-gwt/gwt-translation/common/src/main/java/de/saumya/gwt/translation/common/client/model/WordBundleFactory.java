package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class WordBundleFactory extends ResourceFactory<WordBundle> {

    private final WordFactory wordFactory;

    public WordBundleFactory(final Repository repository,
            final ResourceNotification notification,
            final WordFactory wordFactory) {
        super(repository, notification);
        this.wordFactory = wordFactory;
    }

    @Override
    public String storageName() {
        return "word_bundle";
    }

    @Override
    public String keyName() {
        return "locale";
    }

    @Override
    public WordBundle newResource() {
        return new WordBundle(this.repository, this, this.wordFactory);
    }

}
