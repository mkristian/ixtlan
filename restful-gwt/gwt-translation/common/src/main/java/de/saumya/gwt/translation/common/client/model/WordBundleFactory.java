package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithIdGenerator;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class WordBundleFactory extends
        ResourceFactoryWithIdGenerator<WordBundle> {

    private final WordFactory wordFactory;

    public WordBundleFactory(final Repository repository,
            final ResourceNotifications notification,
            final WordFactory wordFactory) {
        super(repository, notification);
        this.wordFactory = wordFactory;
    }

    @Override
    public String storageName() {
        return "word_bundle";
    }

    @Override
    public WordBundle newResource(final int key) {
        return new WordBundle(this.repository, this, this.wordFactory, key);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}
