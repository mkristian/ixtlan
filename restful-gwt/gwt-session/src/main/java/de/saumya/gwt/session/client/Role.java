/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

class Role extends Resource<Role> {

    protected Role(final Repository repository, final RoleFactory factory) {
        super(repository, factory, null);
    }

    String name;

    @Override
    public String key() {
        return this.name;
    }

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
    }

    @Override
    protected void fromXml(final Element root) {
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