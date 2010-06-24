/**
 *
 */
package de.saumya.gwt.persistence.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import de.saumya.gwt.persistence.client.AbstractResource.State;

public abstract class ResourceFactory<E extends Resource<E>> extends
        AbstractResourceFactory<E> {

    protected final Map<Integer, E> cache = new HashMap<Integer, E>();

    protected final Repository      repository;

    protected ResourceCollection<E> all;

    public ResourceFactory(final Repository repository,
            final ResourceNotifications notifications) {
        super(repository, notifications);
        this.repository = repository;
    }

    @Override
    protected E getResource(final Element root) {
        if (root == null) {
            return null;
        }
        final NodeList list = root.getElementsByTagName("id");
        for (int i = 0; i < list.getLength(); i++) {
            final Node node = list.item(i);
            if (node.getParentNode().equals(root)) {
                return node.getFirstChild() == null
                        ? null
                        : getResource(Integer.parseInt(node.getFirstChild()
                                .getNodeValue()));
            }
        }
        return null;
    }

    @Override
    protected void putIntoCache(final E resource) {
        if (!this.cache.containsKey(resource.id)) {
            if (this.all != null) {
                this.all.addResource(resource);
                this.all.fireResourcesLoadedEvents();
            }
            if (resource.id == 0) {
                throw new IllegalStateException("no ID " + resource);
            }
            this.cache.put(resource.id, resource);
        }
    }

    @Override
    protected void removeFromCache(final E resource) {
        if (this.all != null) {
            this.all.removeResource(resource);
            this.all.fireResourcesLoadedEvents();
        }
        this.cache.remove(resource.id);
    }

    @Override
    void clearCache() {
        this.cache.clear();
        if (this.all != null) {
            this.all.clearResources();
            this.all.fireResourcesLoadedEvents();
        }
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

    protected E getResource(final int id) {
        E result = this.cache.get(id);
        if (result == null) {
            result = newResource(id);
            this.cache.put(id, result);
        }
        return result;
    }

    public abstract E newResource(final int id);

    public E newResource(final int id, final ResourceChangeListener<E> listener) {
        final E resource = newResource(id);
        if (listener != null) {
            resource.addResourceChangeListener(listener);
        }
        return resource;
    }

    public E get(final int key) {
        return get(key, null);
    }

    public E get(final int key, final ResourceChangeListener<E> listener) {
        final E resource = getResource(key);
        resource.addResourceChangeListener(listener);
        if (isImmutable() && resource.state != State.NEW) {
            resource.fireResourceChangeEvents();
            return resource;
        }
        resource.state = State.TO_BE_LOADED;
        this.repository.get(storagePluralName(),
                            key,
                            new ResourceRequestCallback<E>(this.repository,
                                    resource,
                                    this));
        return resource;
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
        boolean loadIt = true;
        if (query == null || query.isEmpty()) {
            loadIt = this.all == null || this.all.size() == 0 || !isImmutable();
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
        if (loadIt) {
            this.repository.all(storagePluralName(),
                                query,
                                new ResourceCollectionRequestCallback(list));
        }
        return list;
    }
}
