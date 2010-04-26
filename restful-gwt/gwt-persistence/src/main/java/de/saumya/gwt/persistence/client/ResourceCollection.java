/**
 *
 */
package de.saumya.gwt.persistence.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

import de.saumya.gwt.persistence.client.AbstractResource.State;

public class ResourceCollection<E extends AbstractResource<E>> extends
        ArrayList<E> {

    private static final long                      serialVersionUID = 1L;

    private final List<ResourcesChangeListener<E>> listeners        = new ArrayList<ResourcesChangeListener<E>>();

    private final AbstractResourceFactory<E>       factory;

    private boolean                                frozen           = false;

    public ResourceCollection(final AbstractResourceFactory<E> factory) {
        this.factory = factory;
    }

    public void fromXml(final String xml) {
        GWT.log(xml, null);
        fromXml(XMLParser.parse(xml).getDocumentElement());
        fireResourcesLoadedEvents();
    }

    public void fromXml(final Element root) {
        clearResources();
        final NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                final Element element = (Element) list.item(i);
                final E resource = this.factory.getResource(element);
                resource.fromElement(element);
                resource.state = State.UP_TO_DATE;
                addResource(resource);
            }
        }
    }

    public String toXml() {
        final StringBuilder buf = new StringBuilder();
        toXml(buf);
        return buf.toString();
    }

    public void toXml(final StringBuilder buf) {
        buf.append("<").append(this.factory.storagePluralName()).append(">");
        for (final E resource : this) {
            resource.toXml(buf);
        }
        buf.append("</").append(this.factory.storagePluralName()).append(">");
    }

    public void addResourcesChangeListener(
            final ResourcesChangeListener<E> listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public void removeResourcesChangeListener(
            final ResourcesChangeListener<E> listener) {
        this.listeners.remove(listener);
    }

    private void fireResourcesLoadedEvents() {
        for (final ResourcesChangeListener<E> listener : this.listeners) {
            listener.onLoaded(this);
        }
    }

    boolean addResource(final E e) {
        return super.add(e);
    }

    void clearResources() {
        super.clear();
    }

    boolean removeResource(final Object o) {
        return super.remove(o);
    }

    public void freeze() {
        this.frozen = true;
    }

    public boolean isFrozen() {
        return this.frozen;
    }

    @Override
    public ResourceCollection<E> clone() {
        final ResourceCollection<E> result = new ResourceCollection<E>(this.factory);
        final boolean old = this.frozen;
        this.frozen = false;
        result.addAll(this);
        this.frozen = old;
        return result;
    }

    @Override
    public boolean add(final E e) {
        if (this.frozen) {
            throw new UnsupportedOperationException();
        }
        else {
            if (!contains(e)) {
                return super.add(e);
            }
            else {
                return true;
            }
        }
    }

    @Override
    public void clear() {
        if (this.frozen) {
            throw new UnsupportedOperationException();
        }
        else {
            super.clear();
        }
    }

    @Override
    public boolean remove(final Object o) {
        if (this.frozen) {
            throw new UnsupportedOperationException();
        }
        else {
            return super.remove(o);
        }
    }

}
