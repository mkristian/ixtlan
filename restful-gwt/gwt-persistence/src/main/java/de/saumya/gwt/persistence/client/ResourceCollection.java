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

import de.saumya.gwt.persistence.client.Resource.State;

@SuppressWarnings("serial")
public class ResourceCollection<E extends Resource<E>> extends ArrayList<E> {

    private final List<ResourcesChangeListener<E>> listeners = new ArrayList<ResourcesChangeListener<E>>();

    private final ResourceFactory<E>               factory;

    public ResourceCollection(final ResourceFactory<E> factory) {
        this.factory = factory;
    }

    public void fromXml(final String xml) {
        GWT.log(xml, null);
        fromXml(XMLParser.parse(xml).getDocumentElement(), false);
        fireResourcesLoadedEvents();
    }

    public void fromXml(final Element root) {
        fromXml(root, true);
    }

    private void fromXml(final Element root, final boolean all) {
        clear();
        final NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            if (list.item(i) instanceof Element) {
                final Element element = (Element) list.item(i);
                final E resource = this.factory.getResource(element);
                // final boolean isNew = resource.isNew();
                resource.fromXml(element);
                resource.state = State.UP_TO_DATE;
                // if (isNew || all) {
                add(resource);
                // fireResourcesChangeEvents(resource);
                // }
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
        for (final E rsrc : this) {
            rsrc.toXml(buf);
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

    // private void fireResourcesChangeEvents(final E resource) {
    // for (final ResourcesChangeListener<E> listener : this.listeners) {
    // listener.onChange(this, resource);
    // }
    // }

    private void fireResourcesLoadedEvents() {
        for (final ResourcesChangeListener<E> listener : this.listeners) {
            listener.onLoaded(this);
        }
    }

}