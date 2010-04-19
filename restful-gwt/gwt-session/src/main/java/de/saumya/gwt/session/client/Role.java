/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithId;

class Role extends ResourceWithId<Role> {

    private final RoleFactory factory;

    protected Role(final Repository repository, final RoleFactory factory,
            final int id) {
        super(repository, factory, id);
        this.factory = factory;
    }

    String name;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = this.factory.nextId();
        this.name = getString(root, "name");
    }

    @Override
    public void toString(final StringBuilder buf) {
        buf.append(":name => ").append(this.name);
    }

    @Override
    public String display() {
        return this.name;
    }
}