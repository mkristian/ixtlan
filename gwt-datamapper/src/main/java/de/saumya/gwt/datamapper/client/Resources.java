/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import java.util.ArrayList;
import java.util.List;

import de.saumya.gwt.datamapper.client.Resource.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.NodeList;
import com.google.gwt.xml.client.XMLParser;

@SuppressWarnings("serial")
public class Resources<E extends Resource<E>> extends ArrayList<E> {

    private List<ResourcesChangeListener<E>> listeners = new ArrayList<ResourcesChangeListener<E>>();

    private final ResourceFactory<E>         factory;

    // private final ResourceChangeListener<E> resourceChangeListener = new
    // ResourceChangeListener<E>(){
    //
    // @Override
    // public void onChange(E resource) {
    // fireResourcesChangeEvents(resource);
    // }
    //		
    // };

    public Resources(ResourceFactory<E> factory) {
        this.factory = factory;
    }

    public void fromXml(String xml) {
        GWT.log(xml, null);
        fromXml(XMLParser.parse(xml).getDocumentElement());
    }        
     
    public void fromXml(Element root) {            
        NodeList list = root.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            E resource = factory.newResource();
            resource.fromXml((Element) list.item(i));
            resource.state = State.UP_TO_DATE;
            add(resource);
            fireResourcesChangeEvents(resource);

            // resource.addResourceChangeListener(resourceChangeListener);
        }
    }

    public String toXml(){
        StringBuffer buf = new StringBuffer();
        toXml(buf);
        return buf.toString();
    }        
    
    public void toXml(StringBuffer buf){
        buf.append("<").append(factory.storagePluralName()).append(">");
        for(E rsrc: this){
            rsrc.toXml(buf);
        }
        buf.append("</").append(factory.storagePluralName()).append(">");
    }
    
    public void addResourcesChangeListener(ResourcesChangeListener<E> listener) {
        this.listeners.add(listener);
    }

    public void removeResourcesChangeListener(ResourcesChangeListener<E> listener) {
        this.listeners.remove(listener);
    }

    private void fireResourcesChangeEvents(E resource) {
        for (ResourcesChangeListener<E> listener : listeners) {
            listener.onChange(this, resource);
        }
    }

}