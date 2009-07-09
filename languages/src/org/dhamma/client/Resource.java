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
	private List<ResourceChangeListener<Resource>> listeners = new ArrayList<ResourceChangeListener<Resource>>();
	private final Repository repository;
	boolean isNew = true;
	final String storageName;

	Resource(Repository repository, ResourceFactory factory) {
		this.repository = repository;
		this.storageName = factory.storageName();
	}

	void save() {
		if (isNew) {
			repository.post(this, new ResourceRequestCallback(this));
		} else {
			repository.put(this, new ResourceRequestCallback(this));
		}
	}

	void destroy() {
		repository.delete(this, new ResourceRequestCallback(this));
	}

	void fromXml(String xml) {
		Document doc = XMLParser.parse(xml);
		fromXml(doc.getDocumentElement());
		isNew = false;
	}

	void addResourceChangeListener(ResourceChangeListener<Resource> listener) {
		this.listeners.add(listener);
	}

	void removeResourceChangeListener(ResourceChangeListener<Resource> listener) {
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

	abstract String toXml();
}