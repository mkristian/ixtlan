/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class Permission extends Resource<Permission> {

    private final PermissionFactory factory;

    Permission(final Repository repository, final PermissionFactory factory) {
        super(repository, factory);
        this.factory = factory;
    }

    String          resourceName;
    String          action;
    Resources<Role> roles;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "resource_name", this.resourceName);
        append(buf, "action", this.action);
        if (this.roles != null) {
            this.roles.toXml(buf);
        }
    }

    @Override
    protected void fromXml(final Element root) {
        this.resourceName = getString(root, "resource_name");
        this.action = getString(root, "action");
        final Element child = getChildElement(root, "roles");
        if (child != null) {
            this.roles = this.factory.newRoleResources();
            this.roles.fromXml(child);
        }
    }

    @Override
    protected String key() {
        return this.resourceName;
    }

    @Override
    protected void toString(final StringBuffer buf) {
        buf.append(":resource_name => ").append(this.resourceName);
        buf.append(":action => ").append(this.action);
        if (this.roles != null) {
            buf.append(", :roles => ").append(this.roles);
        }
    }
}