/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

public class Group extends Resource<Group> {

    private final UserFactory userFactory;

    protected Group(final Repository repository, final GroupFactory factory,
            final int id, final UserFactory userFactory) {
        super(repository, factory, id);
        this.userFactory = userFactory;
    }

    public String    name;

    public Timestamp createdAt;

    public User      createdBy;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
        appendXml(buf, "created_at", this.createdAt);
        appendXml(buf, "created_by", this.createdBy);
    }

    @Override
    protected void fromElement(final Element root) {
        this.name = getString(root, "name");
        this.createdAt = getTimestamp(root, "created_at");
        this.createdBy = this.userFactory.getChildResource(root,
                                                           "created_by",
                                                           this.createdBy);
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "name", this.name);
        toString(indent, buf, "created_at", this.createdAt);
        toString(indent, buf, "created_by", this.createdBy);
    }

    @Override
    public String display() {
        return this.name;
    }

    public boolean isRoot() {
        return "root".equals(this.name);
    }

    public boolean isLocalesAdmin() {
        return "locales".equals(this.name);
    }
}
