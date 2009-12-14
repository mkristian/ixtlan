/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

public abstract class ResourceWithID<E extends Resource<E>> extends Resource<E> {

    protected ResourceWithID(final Repository repository,
            final ResourceFactory<E> factory,
            final ResourceChangeListener<E> listener) {
        super(repository, factory, listener);
    }

    public int id;

    @Override
    public String key() {
        return "" + this.id;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        if (this.state != State.TO_BE_CREATED) {
            append(buf, "id", "" + this.id);
        }
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = getInt(root, "id");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":id => ").append(this.id);
    }
}