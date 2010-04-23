/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithIdGenerator;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class WordFactory extends ResourceFactoryWithIdGenerator<Word> {

    public WordFactory(final Repository repository,
            final ResourceNotifications notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "word";
    }

    @Override
    public Word newResource(final int key) {
        return new Word(this.repository, this, key);
    }

    @Override
    public String defaultSearchParameterName() {
        return null;
    }

}