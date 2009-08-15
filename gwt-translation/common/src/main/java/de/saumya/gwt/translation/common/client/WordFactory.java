/**
 * 
 */
package de.saumya.gwt.translation.common.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class WordFactory extends ResourceFactory<Word> {

    public WordFactory(final Repository repository) {
        super(repository);
    }

    @Override
    public String storageName() {
        return "word";
    }

    @Override
    public String keyName() {
        return "code";
    }

    @Override
    public Word newResource() {
        return new Word(this.repository, this);
    }

}