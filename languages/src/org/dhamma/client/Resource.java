/**
 * 
 */
package org.dhamma.client;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Node;
import com.google.gwt.xml.client.XMLParser;

public abstract class Resource {
	
	private final List<ResourceChangeListener<Resource>> listeners = new ArrayList<ResourceChangeListener<Resource>>();
	private final Repository repository;

	enum State {
		NEW, TO_BE_CREATED, TO_BE_UPDATED, UP_TO_DATE, TO_BE_DELETED, DELETED, TO_BE_LOADED
	}

	State state = State.NEW;

	final String storageName;

	Resource(Repository repository, ResourceFactory<?> factory) {
		this.repository = repository;
		this.storageName = factory.storageName();
	}

	void save() {
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

	void destroy() {
		switch (state) {
		case UP_TO_DATE:
		case TO_BE_DELETED:
			state = State.TO_BE_DELETED;
			repository.delete(this, new ResourceRequestCallback(this));
			break;
		default:
			throw new IllegalStateException("can not delete with state " + state);
		}
	}

	void fromXml(String xml) {
		Document doc = XMLParser.parse(xml);
		fromXml(doc.getDocumentElement());
	}

	String toXml() {
		StringBuffer buf = new StringBuffer();
		buf.append("<").append(storageName).append(">");
		appendXml(buf);
		buf.append("</").append(storageName).append(">");
		return buf.toString();
	}

	void append(StringBuffer buf, String name, String value) {
		if (value != null) {
			buf.append("<").append(name).append(">").append(value).append("</")
					.append(name).append(">");
		}
	}

	void addResourceChangeListener(ResourceChangeListener <Resource> listener) {
		this.listeners.add(listener);
	}

	void removeResourceChangeListener(ResourceChangeListener<? extends Resource> listener) {
		this.listeners.remove(listener);
	}

	void fireResourceChangeEvents() {
		for (ResourceChangeListener<Resource> listener : listeners) {
			listener.onChange(this);
		}
	}

	protected Timestamp getTimestamp(Element root, String name) {
		String value = getString(root, name);
		return value == null ? null : Timestamp.valueOf(value);
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
		Node node = root.getElementsByTagName(name).item(0);
		return node == null ? null : node.getFirstChild().getNodeValue();
	}

	abstract void fromXml(Element root);

	abstract String key();

	abstract void appendXml(StringBuffer buf);
}