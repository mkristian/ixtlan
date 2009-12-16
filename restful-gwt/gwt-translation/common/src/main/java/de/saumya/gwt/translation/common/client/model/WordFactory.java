/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotification;

public class WordFactory extends ResourceFactory<Word> {

    public WordFactory(final Repository repository,
            final ResourceNotification notification) {
        super(repository, notification);
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

    // TODO you can NOT save or delete this resource !!

}