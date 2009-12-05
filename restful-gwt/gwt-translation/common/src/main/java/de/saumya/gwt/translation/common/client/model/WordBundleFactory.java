package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;

public class WordBundleFactory extends ResourceFactory<WordBundle> {

    private final WordFactory wordFactory;

    public WordBundleFactory(final Repository repository,
            final WordFactory wordFactory) {
        super(repository);
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
