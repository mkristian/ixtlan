/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.Resources;

class Permission extends Resource<Permission> {

    private final RoleFactory roleFactory;

    Permission(final Repository repository, final PermissionFactory factory,
            final RoleFactory roleFactory) {
        super(repository, factory);
        this.roleFactory = roleFactory;
    }

    String          resource;
    String          action;
    Resources<Role> roles;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "resource", this.resource);
        append(buf, "action", this.action);
        append(buf, "roles", this.roles);
    }

    @Override
    protected void fromXml(final Element root) {
        this.resource = getString(root, "resource");
        this.action = getString(root, "action");
        this.roles = this.roleFactory.getChildResources(root, "roles");
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    protected void toString(final StringBuffer buf) {
        buf.append(":resource => ").append(this.resource);
        buf.append(":action => ").append(this.action);
        if (this.roles != null) {
            buf.append(", :roles => ").append(this.roles);
        }
    }

    @Override
    public String display() {
        return new StringBuffer(this.resource).append("#")
                .append(this.action)
                .toString();
    }
}