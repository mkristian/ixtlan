/**
 * 
 */
package de.saumya.gwt.persistence.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;

import de.saumya.gwt.persistence.client.Resource.State;

public abstract class ResourceFactory<E extends Resource<E>> {

    protected final Map<String, E> cache = new HashMap<String, E>();

    protected final Repository     repository;

    private E                      singleton;

    private ResourceCollection<E>  all;

    public ResourceFactory(final Repository repository,
            final ResourceNotifications notifications) {
        this.repository = repository;
    }

    private String getString(final Element root, final String name) {
        if (root == null) {
            return null;
        }
        final NodeList list = root.getElementsByTagName(name);
        for (int i = 0; i < list.getLength(); i++) {
            final Node node = list.item(i);
            if (node.getParentNode().equals(root)) {
                return node == null || node.getFirstChild() == null
                        ? null
                        : node.getFirstChild().getNodeValue();
            }
        }
        return null;
    }

    private String keyFromXml(final Element root) {
        return keyName() == null ? null : getString(root, keyName());
    }

    void putIntoCache(final E resource) {
        if (resource.key() != null) {
            if (this.cache.containsKey(resource.key())) {
                throw new IllegalStateException("just created resource already in cache: "
                        + resource);
            }
            this.cache.put(resource.key(), resource);
        }
        else {
            this.singleton = resource;
        }
    }

    void removeFromCache(final E resource) {
        if (resource.key() != null) {
            this.cache.remove(resource.key());
        }
    }

    void clearCache() {
        this.singleton = null;
        this.cache.clear();
    }

    E getResource() {
        if (this.singleton == null) {
            this.singleton = newResource();
        }
        return this.singleton;
    }

    protected E getResource(final String key) {
        if (key == null) {
            return newResource();
        }
        else {
            E result = this.cache.get(key);
            if (result == null) {
                result = newResource(key);
                this.cache.put(key, result);
            }
            return result;
        }
    }

    E getResource(final Element root) {
        return getResource(keyFromXml(root));
    }

    abstract public String storageName();

    public String storagePluralName() {
        return storageName() + "s";
    }

    public abstract String keyName();

    public abstract String defaultSearchParameterName();

    public E getChildResource(final Element root, final String name) {
        final Element element = child(root, name);
        if (element == null) {
            return null;
        }
        final E resource = getResource(keyFromXml(element));
        resource.fromXml(element);
        return resource;
    }

    private Element child(final Element root, final String name) {
        final NodeList list = root.getElementsByTagName(name);
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getParentNode().equals(root)) {
                return (Element) list.item(i);
            }
        }
        return null;
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

    abstract public E newResource();

    abstract public E newResource(String key);

    public ResourceCollection<E> newResources() {
        return new ResourceCollection<E>(this);
    }

    public E get() {
        return get((ResourceChangeListener<E>) null, null);
    }

    public E get(final ResourceNotifications notifications) {
        return get((ResourceChangeListener<E>) null, notifications);
    }

    public E get(final ResourceChangeListener<E> listener) {
        return get(listener, null);
    }

    public E get(final ResourceChangeListener<E> listener,
            final ResourceNotifications notifications) {
        final E resource = getResource();
        if (resource.isImmutable() && resource.state != State.NEW) {
            return resource;
        }
        if (notifications != null) {
            resource.setResourceNotification(notifications);
        }
        resource.state = State.TO_BE_LOADED;
        resource.addResourceChangeListener(listener);
        this.repository.get(storageName(),
                            new ResourceRequestCallback<E>(resource, this));
        return resource;
    }

    public E get(final int key) {
        return get("" + key, null, null);
    }

    public E get(final int key, final ResourceNotifications notifications) {
        return get("" + key, null, notifications);
    }

    public E get(final int key, final ResourceChangeListener<E> listener) {
        return get("" + key, listener, null);
    }

    public E get(final int key, final ResourceChangeListener<E> listener,
            final ResourceNotifications notifications) {
        return get("" + key, listener, notifications);
    }

    public E get(final String key) {
        return get(key, null, null);
    }

    public E get(final String key, final ResourceNotifications notifications) {
        return get(key, null, notifications);
    }

    public E get(final String key, final ResourceChangeListener<E> listener) {
        return get(key, listener, null);
    }

    public E get(final String key, final ResourceChangeListener<E> listener,
            final ResourceNotifications notifications) {
        final E resource = getResource(key);
        resource.addResourceChangeListener(listener);
        if (resource.isImmutable() && resource.state != State.NEW) {
            resource.fireResourceChangeEvents(resource.state.message);
            return resource;
        }
        if (notifications != null) {
            resource.setResourceNotification(notifications);
        }
        resource.state = State.TO_BE_LOADED;
        this.repository.get(storagePluralName(),
                            key,
                            new ResourceRequestCallback<E>(resource, this));
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
        if (query == null || query.isEmpty()) {
            if (this.all == null) {
                list = new ResourceCollection<E>(this);
                list.addAll(this.cache.values());
                this.all = list;
            }
            else {
                list = this.all;
            }
        }
        else {
            list = new ResourceCollection<E>(this);
        }
        list.addResourcesChangeListener(listener);
        this.repository.all(storagePluralName(),
                            query,
                            new ResourceListRequestCallback(list));
        return list;
    }
}
