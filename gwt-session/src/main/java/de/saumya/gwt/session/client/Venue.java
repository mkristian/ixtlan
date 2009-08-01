package de.saumya.gwt.session.client;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class Venue extends Resource<Venue> {

    Venue(Repository repository, ResourceFactory<Venue> factory) {
        super(repository, factory);
    }
    
    public String id;

    public Timestamp createdAt;

    @Override
    protected void appendXml(StringBuffer buf) {
        append(buf, "id", id);      
        append(buf, "created_at", createdAt);
    }

    @Override
    public void fromXml(Element root) {
        id = getString(root, "id");
        createdAt = getTimestamp(root, "created_at");
    }

    @Override
    protected String key() {
        return id;
    }

    @Override
    protected void toString(StringBuffer buf) {
        buf.append("id => ").append(id);
        buf.append(", :created_at => ").append(createdAt);
    }

}
