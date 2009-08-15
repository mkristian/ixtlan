/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class Permission extends Resource<Permission> {

    private final RoleFactory roleFactory;

    Permission(final Repository repository, final PermissionFactory factory,
            final RoleFactory roleFactory) {
        super(repository, factory);
        this.roleFactory = roleFactory;
    }

    String          resourceName;
    String          action;
    Resources<Role> roles;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "resource_name", this.resourceName);
        append(buf, "action", this.action);
        append(buf, "roles", this.roles);
    }

    @Override
    protected void fromXml(final Element root) {
        this.resourceName = getString(root, "resource_name");
        this.action = getString(root, "action");
        this.roles = this.roleFactory.getChildResources(root, "roles");
    }

    @Override
    public String key() {
        return null;
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