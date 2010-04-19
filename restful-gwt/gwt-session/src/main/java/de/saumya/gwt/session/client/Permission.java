/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceWithId;

class Permission extends ResourceWithId<Permission> {

    private final RoleFactory       roleFactory;
    private final PermissionFactory factory;

    Permission(final Repository repository, final PermissionFactory factory,
            final RoleFactory roleFactory, final int id) {
        super(repository, factory, id);
        this.roleFactory = roleFactory;
        this.factory = factory;
    }

    String                   resource;
    String                   action;
    ResourceCollection<Role> roles;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "resource", this.resource);
        appendXml(buf, "action", this.action);
        appendXml(buf, "roles", this.roles);
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = this.factory.nextId();
        this.resource = getString(root, "resource");
        this.action = getString(root, "action");
        this.roles = this.roleFactory.getChildResourceCollection(root, "roles");
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    public void toString(final StringBuilder buf) {
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