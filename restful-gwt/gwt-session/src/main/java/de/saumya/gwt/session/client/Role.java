/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.AnonymousResource;
import de.saumya.gwt.persistence.client.Repository;

class Role extends AnonymousResource<Role> {

    protected Role(final Repository repository, final RoleFactory factory) {
        super(repository, factory);
    }

    String name;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "name", this.name);
    }

    @Override
    protected void fromElement(final Element root) {
        System.err.println("===+++" + root + " " + getString(root, "name"));
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