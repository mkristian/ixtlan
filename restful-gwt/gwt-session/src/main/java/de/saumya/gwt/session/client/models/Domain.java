package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

public class Domain extends Resource<Domain> {

    Domain(final Repository repository, final ResourceFactory<Domain> factory) {
        this(repository, factory, null);
    }

    Domain(final Repository repository, final ResourceFactory<Domain> factory,
            final String id) {
        super(repository, factory);
        this.id = id;
    }

    public String    id;

    public Timestamp createdAt;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "id", this.id);
        appendXml(buf, "created_at", this.createdAt);
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
    protected void toString(final StringBuilder buf) {
        toString(buf, "id", this.id);
        toString(buf, "created_at", this.createdAt);
    }

    @Override
    public String display() {
        return this.id;
    }

}
