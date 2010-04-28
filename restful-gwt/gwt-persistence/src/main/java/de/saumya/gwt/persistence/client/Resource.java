/**
 *
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.xml.client.Element;

public abstract class Resource<E extends Resource<E>> extends
        AbstractResource<E> {

    final ResourceFactory<E> factory;

    protected Resource(final Repository repository,
            final ResourceFactory<E> factory, final int id) {
        super(repository, factory);
        this.factory = factory;
        this.id = id;
    }

    public int id;

    @Override
    public void reload() {
        this.factory.get(this.id);
    }

    @Override
    protected void post() {
        this.repository.post(this, new ResourceRequestCallback<E>(this,
                this.factory));
    }

    @Override
    protected void put() {
        this.repository.put(this, new ResourceRequestCallback<E>(this,
                this.factory));
    }

    @Override
    protected void put(final String verb) {
        this.repository.put(this, verb, new ResourceRequestCallback<E>(this,
                this.factory));
    }

    @Override
    protected void delete() {
        this.repository.delete(this, new ResourceRequestCallback<E>(this,
                this.factory));
    }

    @Override
    void fromRootElement(final Element root) {
        final int id = getInt(root, "id");
        if (this.state != State.TO_BE_CREATED && this.id != id) {
            throw new IllegalStateException("loaded id:\n" + root
                    + "\ndoes not match the id of this resource:\n" + this,
                    null);
        }
        this.id = id;
        fromElement(root);
    }

    @Override
    public void toXml(final StringBuilder buf) {
        buf.append("<").append(this.factory.storageName()).append(">");
        if (this.state != State.TO_BE_CREATED && this.id != 0) {
            appendXml(buf, "id", this.id);
        }
        appendXml(buf);
        buf.append("</").append(this.factory.storageName()).append(">");
    }

    @Override
    protected void appendXml(final StringBuilder buf, final String name,
            final Resource<?> value) {
        if (value != null) {
            buf.append("<").append(name).append(">");
            value.appendXml(buf, "id", value.id);
            value.appendXml(buf);
            buf.append("</").append(name).append(">");
        }
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        toStringRoot("", buf);
        return buf.toString();
    }

    @Override
    protected void toStringRoot(final String indent, final StringBuilder buf) {
        buf.append(indent).append(getClass().getName());
        final String newindent = indent + INDENT;
        toString(newindent, buf, "id", this.id);
        toString(newindent, buf);
    }

    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Resource<?>)) {
            return false;
        }
        return this.id == ((Resource<?>) other).id;
    }

    @Override
    public int hashCode() {
        return this.id;
    }
}
