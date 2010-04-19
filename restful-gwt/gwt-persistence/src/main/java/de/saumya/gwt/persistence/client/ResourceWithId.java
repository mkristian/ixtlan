/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

public abstract class ResourceWithId<E extends Resource<E>> extends Resource<E> {

    protected ResourceWithId(final Repository repository,
            final ResourceFactory<E> factory, final int id) {
        super(repository, factory);
        this.id = id;
    }

    public int id;

    @Override
    public String key() {
        return "" + this.id;
    }

    @Override
    protected void appendXml(final StringBuilder buf) {
        if (this.state != State.TO_BE_CREATED) {
            appendXml(buf, "id", "" + this.id);
        }
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = getInt(root, "id");
    }

    @Override
    // TODO move to StringBuilder
    public void toString(final StringBuilder buf) {
        buf.append(":id => ").append(this.id);
    }
}