package de.saumya.gwt.translation.common.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

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
