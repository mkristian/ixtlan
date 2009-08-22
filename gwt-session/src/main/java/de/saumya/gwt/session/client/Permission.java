/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class Permission extends Resource<Permission> {

    private final GroupFactory groupFactory;

    Permission(final Repository repository, final PermissionFactory factory,
            final GroupFactory groupFactory) {
        super(repository, factory);
        this.groupFactory = groupFactory;
    }

    String           resourceName;
    String           action;
    Resources<Group> groups;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "resource_name", this.resourceName);
        append(buf, "action", this.action);
        append(buf, "groups", this.groups);
    }

    @Override
    protected void fromXml(final Element root) {
        this.resourceName = getString(root, "resource_name");
        this.action = getString(root, "action");
        this.groups = this.groupFactory.getChildResources(root, "groups");
    }

    @Override
    public String key() {
        return null;
    }

    @Override
    protected void toString(final StringBuffer buf) {
        buf.append(":resource_name => ").append(this.resourceName);
        buf.append(":action => ").append(this.action);
        if (this.groups != null) {
            buf.append(", :groups => ").append(this.groups);
        }
    }

    @Override
    public String display() {
        return new StringBuffer(this.resourceName).append("#")
                .append(this.action)
                .toString();
    }
}