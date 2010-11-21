package de.saumya.gwt.persistence.client;

import java.util.Map;

import com.google.gwt.xml.client.Element;

public abstract class AnonymousResourceFactory<E extends AnonymousResource<E>>
        extends AbstractResourceFactory<E> {

    protected ResourceCollection<E> all;

    public AnonymousResourceFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
    }

    @Override
    E getResource(final Element root) {
        if (root == null) {
            return null;
        }
        return newResource();
    }

    @Override
    void putIntoCache(final E resource) {
    }

    @Override
    void removeFromCache(final E resource) {
    }

    @Override
    void clearCache() {
    }

    public abstract E newResource();

    public E newResource(final ResourceChangeListener<E> listener) {
        final E resource = newResource();
        if (listener != null) {
            resource.addResourceChangeListener(listener);
        }
        return resource;
    }

    public ResourceCollection<E> newResources() {
        return new ResourceCollection<E>(this);
    }

    public ResourceCollection<E> getChildResourceCollection(final Element root,
            final String name) {
        final Element element = child(root, name);
        final ResourceCollection<E> resources = newResources();
        if (element != null) {
            resources.fromXml(element);
        }
        return resources;
    }

    public ResourceCollection<E> all() {
        return all(null, null);
    }

    public ResourceCollection<E> all(final Map<String, String> query) {
        return all(query, null);
    }

    public ResourceCollection<E> all(final ResourcesChangeListener<E> listener) {
        return all(null, listener);
    }

    public ResourceCollection<E> all(final Map<String, String> query,
            final ResourcesChangeListener<E> listener) {
        final ResourceCollection<E> list;
        if (query == null || query.isEmpty()) {
            if (this.all == null) {
                this.all = new ResourceCollection<E>(this);
                this.all.addAll(this.cache.values());
            }
            list = this.all;
            this.all.freeze();
        }
        else {
            list = new ResourceCollection<E>(this);
        }
        list.addResourcesChangeListener(listener);
        this.repository.all(storagePluralName(),
                            query,
                            new ResourceCollectionRequestCallback(list));
        return list;
    }
}
