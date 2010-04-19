/**
 * 
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceFactoryWithID;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourceWithId;

public abstract class ResourceFactoryWithIdGenerator<E extends ResourceWithId<E>>
        extends ResourceFactoryWithID<E> {

    public ResourceFactoryWithIdGenerator(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    private int nextId = 0;

    public int nextId() {
        return this.nextId++;
    }

}