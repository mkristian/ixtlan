/**
 * 
 */
package org.dhamma.client;

import java.util.ArrayList;


import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

@SuppressWarnings("serial")
public class ResourceList<E extends Resource> extends ArrayList<Resource>{
//		implements List<Resource> {

	private final ResourceFactory factory;

	ResourceList(ResourceFactory factory) {
		this.factory = factory;
	}

	void fromXml(String xml) {
		Element root = XMLParser.parse(xml).getDocumentElement();
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Resource resource = factory.newResource();
			resource.fromXml((Element) list.item(i));
			add(resource);
		}
	}
}