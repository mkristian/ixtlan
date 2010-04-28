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
        this.name = getString(root, "name");
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        buf.append("(:name => ").append(this.name).append(")");
    }

    @Override
    public String display() {
        return this.name;
    }
}
