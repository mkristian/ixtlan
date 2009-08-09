/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import de.saumya.gwt.datamapper.client.Resource.State;

public abstract class ResourceFactory<E extends Resource<E>> {

    protected final Repository repository;

    public ResourceFactory(final Repository repository) {
        this.repository = repository;
    }

    abstract public String storageName();

    public String storagePluralName() {
        return storageName() + "s";
    }

    abstract public E newResource();

    public Resources<E> newResources() {
        return new Resources<E>(this);
    }

    public E get(final int id, final ResourceChangeListener<E> listener) {
        return get("" + id, listener);
    }

    public E get(final String id, final ResourceChangeListener<E> listener) {
        final E resource = newResource();
        resource.state = State.TO_BE_LOADED;
        resource.addResourceChangeListener(listener);
        this.repository.get(storageName(),
                            id,
                            new ResourceRequestCallback(resource));
        return resource;
    }

    public Resources<E> all(final ResourcesChangeListener<E> listener) {
        final Resources<E> list = new Resources<E>(this);
        list.addResourcesChangeListener(listener);
        this.repository.all(storageName(),
                            new ResourceListRequestCallback(list));
        return list;
    }
}