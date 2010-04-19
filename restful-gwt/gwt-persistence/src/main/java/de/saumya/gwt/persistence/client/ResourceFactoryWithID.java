/**
 * 
 */
package de.saumya.gwt.persistence.client;


public abstract class ResourceFactoryWithID<E extends ResourceWithId<E>>
        extends ResourceFactory<E> {

    @Override
    public E newResource() {
        return newResource(0);
    }

    @Override
    public E newResource(final String key) {
        return newResource(Integer.parseInt(key));
    }

    public ResourceFactoryWithID(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    public String keyName() {
        return "id";
    }

    protected abstract E newResource(final int id);

}