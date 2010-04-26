/**
 *
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

public class Singleton extends SingletonResource<Singleton> {

    Singleton(final Repository repository, final SingletonFactory factory) {
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
    public void toString(final StringBuilder buf) {
        toString(buf, "name", this.name);
    }

    @Override
    public String display() {
        return this.name;
    }
}
