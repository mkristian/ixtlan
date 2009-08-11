/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public abstract class Resource<E extends Resource<E>> {

    private final List<ResourceChangeListener<E>> listeners = new ArrayList<ResourceChangeListener<E>>();
    private final Repository                      repository;

    enum State {
        NEW, TO_BE_CREATED, TO_BE_UPDATED, UP_TO_DATE, TO_BE_DELETED, DELETED, TO_BE_LOADED
    }

    State        state = State.NEW;

    final String storageName;

    protected Resource(final Repository repository,
            final ResourceFactory<E> factory) {
        this.repository = repository;
        this.storageName = factory.storageName();
    }

    public boolean isNew() {
        return this.state == State.NEW;
    }

    public boolean isUptodate() {
        return this.state == State.UP_TO_DATE;
    }

    public boolean isDeleted() {
        return this.state == State.DELETED;
    }

    public void save() {
        switch (this.state) {
        case NEW:
        case TO_BE_CREATED:
            this.state = State.TO_BE_CREATED;
            this.repository.post(this, new ResourceRequestCallback(this));
            break;
        case UP_TO_DATE:
        case TO_BE_UPDATED:
        case TO_BE_DELETED:
            this.state = State.TO_BE_UPDATED;
            this.repository.put(this, new ResourceRequestCallback(this));
            break;
        default:
            throw new IllegalStateException("can not save with state "
                    + this.state);
        }
    }

    public void destroy() {
        switch (this.state) {
        case UP_TO_DATE:
        case TO_BE_DELETED:
            this.state = State.TO_BE_DELETED;
            this.repository.delete(this, new ResourceRequestCallback(this));
            break;
        default:
            throw new IllegalStateException("can not delete with state "
                    + this.state);
        }
    }

    public void fromXml(final String xml) {
        final Document doc = XMLParser.parse(xml);
        fromXml(doc.getDocumentElement());
    }

    public String toXml() {
        final StringBuffer buf = new StringBuffer();
        toXml(buf);
        return buf.toString();
    }

    public void toXml(final StringBuffer buf) {
        buf.append("<").append(this.storageName).append(">");
        appendXml(buf);
        buf.append("</").append(this.storageName).append(">");
    }

    protected void append(final StringBuffer buf, final String name,
            final Object value) {
        if (value != null) {
            append(buf, name, value.toString());
        }
    }

    protected void append(final StringBuffer buf, final String name,
            final Resource<?> value) {
        if (value != null) {
            buf.append("<").append(name).append(">");
            value.appendXml(buf);
            buf.append("</").append(name).append(">");
        }
    }

    protected void append(final StringBuffer buf, final String name,
            final String value) {
        if (value != null) {
            buf.append("<")
                    .append(name)
                    .append(">")
                    .append(value)
                    .append("</")
                    .append(name)
                    .append(">");
        }
    }

    public void addResourceChangeListener(
            final ResourceChangeListener<E> listener) {
        this.listeners.add(listener);
    }

    public void removeResourceChangeListener(
            final ResourceChangeListener<E> listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    void fireResourceChangeEvents() {
        for (final ResourceChangeListener<E> listener : this.listeners) {
            listener.onChange((E) this);
        }
    }

    protected Timestamp getTimestamp(final Element root, final String name) {
        final String value = getString(root, name);
        return value == null ? null : (value.matches("[0-9]*")
                ? new Timestamp(Long.parseLong(value))
                : Timestamp.valueOf(value));
    }

    protected Date getDate(final Element root, final String name) {
        final String value = getString(root, name);
        return value == null ? null : Date.valueOf(value);
    }

    protected Time getTime(final Element root, final String name) {
        final String value = getString(root, name);
        return value == null ? null : Time.valueOf(value);
    }

    protected int getInt(final Element root, final String name) {
        final String value = getString(root, name);
        return value == null ? 0 : Integer.parseInt(value);
    }

    protected String getString(final Element root, final String name) {
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

    protected Element getChildElement(final Element root, final String name) {
        return (Element) root.getElementsByTagName(name).item(0);
    }

    @Override
    public String toString() {
        final StringBuffer buf = new StringBuffer(getClass().getName()).append("(");
        toString(buf);

        return buf.append(")").toString();
    }

    @Override
    public boolean equals(final Object other) {
        if (key() == null || !(other instanceof Resource<?>)) {
            return false;
        }
        return key().equals(((Resource<?>) other).key());
    }

    @Override
    public int hashCode() {
        return key() == null ? super.hashCode() : key().hashCode();
    }

    protected abstract void fromXml(Element root);

    protected abstract String key();

    protected abstract void appendXml(StringBuffer buf);

    protected abstract void toString(StringBuffer buf);

}
