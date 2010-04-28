/**
 *
 */
package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AnonymousResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public class WordFactory extends AnonymousResourceFactory<Word> {

    public WordFactory(final Repository repository,
            final ResourceNotifications notification) {
        super(repository, notification);
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    public String storageName() {
        return "word";
    }

    @Override
    public Word newResource() {
        return new Word(this.repository, this);
    }

}
