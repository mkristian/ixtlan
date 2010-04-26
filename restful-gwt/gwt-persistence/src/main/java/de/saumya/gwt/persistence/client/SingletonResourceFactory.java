/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.AbstractResource.State;

public abstract class SingletonResourceFactory<E extends SingletonResource<E>>
        extends AbstractResourceFactory<E> {

    private E singleton;

    public SingletonResourceFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    E getResource(final Element root) {
        if (root == null) {
            return null;
        }
        return getResource();
    }

    @Override
    void putIntoCache(final E resource) {
        this.singleton = resource;
    }

    @Override
    void removeFromCache(final E resource) {
        this.singleton = null;
    }

    @Override
    void clearCache() {
        this.singleton = null;
    }

    E getResource() {
        if (this.singleton == null) {
            this.singleton = newResource();
        }
        return this.singleton;
    }

    public abstract E newResource();

    public E get() {
        return get(null);
    }

    public E get(final ResourceChangeListener<E> listener) {
        final E resource = getResource();
        if (resource.isImmutable() && resource.state != State.NEW) {
            // TODO fire events
            return resource;
        }
        resource.state = State.TO_BE_LOADED;
        resource.addResourceChangeListener(listener);
        this.repository.get(storageName(),
                            new ResourceRequestCallback<E>(resource, this));
        return resource;
    }
}
