/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import de.saumya.gwt.datamapper.client.Resource.State;

public abstract class ResourceFactory<E extends Resource<E>> {

    protected final Repository repository;

    public ResourceFactory(Repository repository) {
        this.repository = repository;
    }

    abstract public String storageName();

    abstract protected E newResource();

    public E get(int id, ResourceChangeListener<E> listener) {
        E resource = newResource();
        resource.state = State.TO_BE_LOADED;
        resource.addResourceChangeListener(listener);
        repository.get(storageName(), id, new ResourceRequestCallback(resource));
        return resource;
    }

    public Resources<E> all(ResourcesChangeListener<E> listener) {
        Resources<E> list = new Resources<E>(this);
        list.addResourcesChangeListener(listener);
        repository.all(storageName(), new ResourceListRequestCallback(list));
        return list;
    }
}