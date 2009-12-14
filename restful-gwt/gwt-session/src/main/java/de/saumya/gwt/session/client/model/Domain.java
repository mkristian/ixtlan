package de.saumya.gwt.session.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

public class Domain extends Resource<Domain> {

    Domain(final Repository repository, final ResourceFactory<Domain> factory) {
        super(repository, factory, null);
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
