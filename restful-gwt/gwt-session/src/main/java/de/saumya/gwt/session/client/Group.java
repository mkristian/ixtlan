/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;

class Group extends Resource<Group> {

    protected Group(final Repository repository, final GroupFactory factory) {
        super(repository, factory);
    }

    String name;

    @Override
    public String key() {
        return this.name;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "name", this.name);
    }

    @Override
    protected void fromXml(final Element root) {
        this.name = getString(root, "name");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":name => ").append(this.name);
    }

    @Override
    public String display() {
        return this.name;
    }
}