/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;

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