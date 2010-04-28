package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

public class Domain extends Resource<Domain> {

    Domain(final Repository repository, final DomainFactory factory,
            final int id) {
        super(repository, factory, id);
    }

    public String    name;

    public Timestamp createdAt;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
        appendXml(buf, "created_at", this.createdAt);
    }

    @Override
    public void fromElement(final Element root) {
        this.name = getString(root, "name");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public void toString(final StringBuilder buf) {
        toString(buf, "name", this.name);
        toString(buf, "created_at", this.createdAt);
    }

    @Override
    public String display() {
        return this.name;
    }

}
