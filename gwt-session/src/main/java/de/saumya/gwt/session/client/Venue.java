package de.saumya.gwt.session.client;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class Venue extends Resource<Venue> {

    Venue(final Repository repository, final ResourceFactory<Venue> factory) {
        super(repository, factory);
    }

    public String    id;

    public Timestamp createdAt;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "id", this.id);
        append(buf, "created_at", this.createdAt);
    }

    @Override
    public void fromXml(final Element root) {
        this.id = getString(root, "id");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public String key() {
        return this.id;
    }

    @Override
    protected void toString(final StringBuffer buf) {
        buf.append("id => ").append(this.id);
        buf.append(", :created_at => ").append(this.createdAt);
    }

    @Override
    public String display() {
        return this.id;
    }

}
