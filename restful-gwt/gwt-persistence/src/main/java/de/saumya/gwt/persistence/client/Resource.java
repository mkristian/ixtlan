/**
 * 
 */
package de.saumya.gwt.persistence.client;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

public abstract class Resource<E extends Resource<E>> {

    protected enum State {
        NEW, TO_BE_CREATED("created"), TO_BE_UPDATED("updated"), UP_TO_DATE, TO_BE_DELETED(
                "deleted"), DELETED, TO_BE_LOADED("loaded"), STALE;

        final String message;

        State() {
            this.message = null;
        }

        State(final String message) {
            this.message = message;
        }
    }

    private final Repository                     repository;

    private final Set<ResourceChangeListener<E>> listeners = new HashSet<ResourceChangeListener<E>>();
    private ResourceNotifications                changeNotifications;

    final ResourceFactory<E>                     factory;

    protected State                              state     = State.NEW;

    protected Resource(final Repository repository,
            final ResourceFactory<E> factory) {
        this.repository = repository;
        this.factory = factory;
    }

    public boolean isImmutable() {
        return false;
    }

    public boolean isNew() {
        return this.state == State.NEW; // || this.state == State.TO_BE_CREATED;
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
            this.repository.post(this, new ResourceRequestCallback<E>(this,
                    this.factory));
            break;
        case UP_TO_DATE:
        case TO_BE_UPDATED:
        case TO_BE_DELETED:
            this.state = State.TO_BE_UPDATED;
            this.repository.put(this, new ResourceRequestCallback<E>(this,
                    this.factory));
            break;
        default:
            throw new IllegalStateException("can not save in state "
                    + this.state);
        }
    }

    public void save(final String verb) {
        switch (this.state) {
        case UP_TO_DATE:
        case TO_BE_UPDATED:
            this.state = State.TO_BE_UPDATED;
            this.repository.put(this,
                                verb,
                                new ResourceRequestCallback<E>(this,
                                        this.factory));
            break;
        default:
            throw new IllegalStateException("can not save with verb " + verb
                    + " in state " + this.state);
        }

    }

    public void destroy() {
        switch (this.state) {
        case UP_TO_DATE:
        case TO_BE_DELETED:
            this.state = State.TO_BE_DELETED;
            this.repository.delete(this, new ResourceRequestCallback<E>(this,
                    this.factory));
            break;
        default:
            throw new IllegalStateException("can not delete with state "
                    + this.state);
        }
    }

    public void reload() {
        if (key() == null) {
            this.factory.get();
        }
        else {
            this.factory.get(key());
        }
    }

    @SuppressWarnings("unchecked")
    public void fromXml(final String xml) {
        final Document doc = XMLParser.parse(xml);
        fromXml(doc.getDocumentElement());
        if (this.state == State.TO_BE_CREATED) {
            this.factory.putIntoCache((E) this);
        }
    }

    public String toXml() {
        final StringBuilder buf = new StringBuilder();
        toXml(buf);
        return buf.toString();
    }

    public void toXml(final StringBuilder buf) {
        buf.append("<").append(this.factory.storageName()).append(">");
        appendXml(buf);
        buf.append("</").append(this.factory.storageName()).append(">");
    }

    protected void appendXml(final StringBuilder buf, final String name,
            final Resource<?> value) {
        if (value != null) {
            buf.append("<").append(name).append(">");
            value.appendXml(buf);
            buf.append("</").append(name).append(">");
        }
    }

    protected void appendXml(final StringBuilder buf, final String name,
            final ResourceCollection<?> value) {
        if (value != null) {
            value.toXml(buf);
        }
        else {
            buf.append("<").append(name).append(">");
            buf.append("</").append(name).append(">");
        }
    }

    protected void appendXml(final StringBuilder buf, final String name,
            final String value) {
        // follow what the browser does with empty strings: do not send them
        if (value != null && !"".equals(value)) {
            buf.append("<")
                    .append(name)
                    .append(">")
                    .append(value)
                    .append("</")
                    .append(name)
                    .append(">");
        }
    }

    protected void appendXml(final StringBuilder buf, final String name,
            final Object value) {
        if (value != null) {
            appendXml(buf, name, value.toString());
        }
    }

    protected void toString(final StringBuilder buf, final String name,
            final Resource<?> value) {
        if (value != null) {
            buf.append(", :").append(name).append(" => ");
            value.toString(buf);
        }
    }

    protected void toString(final StringBuilder buf, final String name,
            final ResourceCollection<?> value) {
        if (value != null) {
            buf.append(", :").append(name).append(" => [");
            boolean first = true;
            for (final Resource<?> resource : value) {
                if (first) {
                    first = false;
                }
                else {
                    buf.append(", ");
                }
                resource.toString(buf);
            }
            buf.append("]");
        }
    }

    protected void toString(final StringBuilder buf, final String name,
            final String value) {
        if (value != null) {
            buf.append(", :").append(name).append(" => ").append(value);
        }
    }

    protected void toString(final StringBuilder buf, final String name,
            final Object value) {
        if (value != null) {
            toString(buf, name, value.toString());
        }
    }

    protected Timestamp getTimestamp(final Element root, final String name) {
        final String value = getString(root, name);
        return value == null ? null : (value.matches("[0-9]*")
                ? new Timestamp(Long.parseLong(value))
                : new TimestampFactory(value).toTimestamp());
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

    protected boolean getBoolean(final Element root, final String name) {
        final String value = getString(root, name);
        return "true".equals(value);
    }

    protected String getString(final Element root, final String name) {
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

    public void setResourceNotification(
            final ResourceNotifications changeNotifications) {
        this.changeNotifications = changeNotifications;
    }

    public void addResourceChangeListener(
            final ResourceChangeListener<E> listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    public void removeResourceChangeListener(
            final ResourceChangeListener<E> listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    void fireResourceChangeEvents(final String message) {
        for (final ResourceChangeListener<E> listener : this.listeners) {
            listener.onChange((E) this);
        }
        if (this.changeNotifications != null) {
            this.changeNotifications.info(message + " "
                    + this.factory.storageName() + ": ", this);
            this.changeNotifications = null;
        }
    }

    @SuppressWarnings("unchecked")
    void fireResourceErrorEvents(final int status, final String statusText) {
        for (final ResourceChangeListener<E> listener : this.listeners) {
            listener.onError((E) this);
        }
        if (this.changeNotifications != null) {
            this.changeNotifications.error(status, statusText + " "
                    + this.factory.storageName() + ": ", this);
            this.changeNotifications = null;
        }

    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(getClass().getName()).append("(");
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

    protected abstract void appendXml(StringBuilder buf);

    protected abstract void toString(StringBuilder buf);

    public abstract String key();

    public abstract String display();

}
