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

    protected Resource(Repository repository, ResourceFactory<E> factory) {
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
        switch (state) {
        case NEW:
        case TO_BE_CREATED:
            state = State.TO_BE_CREATED;
            repository.post(this, new ResourceRequestCallback(this));
            break;
        case UP_TO_DATE:
        case TO_BE_UPDATED:
        case TO_BE_DELETED:
            state = State.TO_BE_UPDATED;
            repository.put(this, new ResourceRequestCallback(this));
            break;
        default:
            throw new IllegalStateException("can not save with state " + state);
        }
    }

    public void destroy() {
        switch (state) {
        case UP_TO_DATE:
        case TO_BE_DELETED:
            state = State.TO_BE_DELETED;
            repository.delete(this, new ResourceRequestCallback(this));
            break;
        default:
            throw new IllegalStateException("can not delete with state "
                    + state);
        }
    }

    public void fromXml(String xml) {
        Document doc = XMLParser.parse(xml);
        fromXml(doc.getDocumentElement());
    }

    public String toXml() {
        StringBuffer buf = new StringBuffer();
        toXml(buf);
        return buf.toString();
    }

    public void toXml(StringBuffer buf) {
        buf.append("<").append(storageName).append(">");
        appendXml(buf);
        buf.append("</").append(storageName).append(">");
    }

    protected void append(StringBuffer buf, String name, Object value) {
        if (value != null) {
            append(buf, name, value.toString());
        }
    }

    protected void append(StringBuffer buf, String name, String value) {
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

    public void addResourceChangeListener(ResourceChangeListener<E> listener) {
        this.listeners.add(listener);
    }

    public void removeResourceChangeListener(ResourceChangeListener<E> listener) {
        this.listeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    void fireResourceChangeEvents() {
        for (ResourceChangeListener<E> listener : listeners) {
            listener.onChange((E) this);
        }
    }

    protected Timestamp getTimestamp(Element root, String name) {
        String value = getString(root, name);
        return value == null ? null : Timestamp.valueOf(value);
    }

    protected Date getDate(Element root, String name) {
        String value = getString(root, name);
        return value == null ? null : Date.valueOf(value);
    }

    protected Time getTime(Element root, String name) {
        String value = getString(root, name);
        return value == null ? null : Time.valueOf(value);
    }

    protected int getInt(Element root, String name) {
        String value = getString(root, name);
        return value == null ? null : Integer.parseInt(value);
    }

    protected String getString(Element root, String name) {
        NodeList list = root.getElementsByTagName(name);
        for (int i = 0; i < list.getLength(); i++) {
            Node node = list.item(i);
            if (node.getParentNode().equals(root)) {
                return node == null ? null : node.getFirstChild()
                        .getNodeValue();
            }
        }
        return null;
    }

    protected Element getChildElement(Element root, String name) {
        return (Element) root.getElementsByTagName(name).item(0);
    }

    @Override
    public String toString() {
        StringBuffer buf = new StringBuffer(getClass().getName()).append("(");
        toString(buf);

        return buf.append(")").toString();
    }

    protected abstract void fromXml(Element root);

    protected abstract String key();

    protected abstract void appendXml(StringBuffer buf);

    protected abstract void toString(StringBuffer buf);

}