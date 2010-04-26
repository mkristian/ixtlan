/**
 *
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.AnonymousResource;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;

class Permission extends AnonymousResource<Permission> {

    private final RoleFactory roleFactory;

    Permission(final Repository repository, final PermissionFactory factory,
            final RoleFactory roleFactory) {
        super(repository, factory);
        this.roleFactory = roleFactory;
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
    protected void fromElement(final Element root) {
        this.resource = getString(root, "resource");
        this.action = getString(root, "action");
        this.roles = this.roleFactory.getChildResourceCollection(root, "roles");
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
