/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import com.google.gwt.xml.client.Element;

public class ResourceWithID<E extends Resource<E>> extends Resource<E> {

    protected ResourceWithID(Repository repository, ResourceFactory<E> factory) {
        super(repository, factory);
    }

    public int id;

    protected String key() {
        return "" + id;
    }

    protected void appendXml(StringBuffer buf) {
        if (state != State.TO_BE_CREATED) {
            append(buf, "id", "" + id);
        }
    }

    protected void fromXml(Element root) {
        id = getInt(root, "id");
    }

    public void toString(StringBuffer buf) {
        buf.append("(:id => ").append(id);
    }
}