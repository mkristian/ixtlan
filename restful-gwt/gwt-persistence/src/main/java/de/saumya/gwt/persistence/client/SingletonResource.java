/**
 *
 */
package de.saumya.gwt.persistence.client;

public abstract class SingletonResource<E extends SingletonResource<E>> extends
        AbstractResource<E> {

    final SingletonResourceFactory<E> factory;

    protected SingletonResource(final Repository repository,
            final SingletonResourceFactory<E> factory) {
        super(repository, factory);
        this.factory = factory;
    }

    @Override
    public void reload() {
        this.factory.get();
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
    public void toXml(final StringBuilder buf) {
        buf.append("<").append(this.factory.storageName()).append(">");
        appendXml(buf);
        buf.append("</").append(this.factory.storageName()).append(">");
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder();
        toStringRoot("", buf);
        return buf.toString();
    }

    @Override
    public void toStringRoot(final String indent, final StringBuilder buf) {
        buf.append(indent).append(getClass().getName());
        toString(indent + INDENT, buf);
    }

    @Override
    public boolean equals(final Object other) {
        return other instanceof SingletonResource<?>;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
