/**
 * 
 */
package de.saumya.gwt.persistence.client;


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