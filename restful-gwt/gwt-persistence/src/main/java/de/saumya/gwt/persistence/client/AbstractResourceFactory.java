/**
 *
 */
package de.saumya.gwt.persistence.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;

public abstract class AbstractResourceFactory<E extends AbstractResource<E>> {

    protected final Map<Integer, E> cache = new HashMap<Integer, E>();

    protected final Repository      repository;

    public AbstractResourceFactory(final Repository repository,
            final ResourceNotifications notifications) {
        this.repository = repository;
    }

    public boolean isImmutable() {
        return false;
    }

    abstract void putIntoCache(final E resource);

    abstract void removeFromCache(final E resource);

    abstract void clearCache();

    // TODO most properly will need to delete this method since default is
    // needed when updating
    // the object and the server does not send this info. in such a case the
    // already set child
    // object should stay where it is.
    public E getChildResource(final Element root, final String name) {
        return getChildResource(root, name, null);
    }

    public E getChildResource(final Element root, final String name,
            final E defaultValue) {
        final Element element = child(root, name);
        if (element == null) {
            return defaultValue;
        }
        final E resource = getResource(element);
        resource.fromRootElement(element);
        putIntoCache(resource);
        return resource;
    }

    abstract E getResource(Element root);

    protected Element child(final Element root, final String name) {
        final NodeList list = root.getElementsByTagName(name);
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i).getParentNode().equals(root)) {
                return (Element) list.item(i);
            }
        }
        return null;
    }

    public String defaultSearchParameterName() {
        return "query";
    }

    public String storagePluralName() {
        return storageName() + "s";
    }

    abstract public String storageName();
}
