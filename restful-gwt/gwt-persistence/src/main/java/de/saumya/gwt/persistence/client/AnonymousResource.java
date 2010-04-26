/**
 *
 */
package de.saumya.gwt.persistence.client;

public abstract class AnonymousResource<E extends AnonymousResource<E>> extends
        AbstractResource<E> {

    final AnonymousResourceFactory<E> factory;

    protected AnonymousResource(final Repository repository,
            final AnonymousResourceFactory<E> factory) {
        super(repository, factory);
        this.factory = factory;
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("anonymous resources have no identifier which can be used for saving");
    }

    @Override
    public void reload() {
        throw new UnsupportedOperationException("anonymous resources have no identifier which can be used for reloading");
    }

    @Override
    protected void post() {
        throw new UnsupportedOperationException("anonymous resources have no identifier which can be used for creating");
    }

    @Override
    protected void put() {
        throw new UnsupportedOperationException("anonymous resources have no identifier which can be used for updating");
    }

    @Override
    protected void put(final String verb) {
        throw new UnsupportedOperationException("anonymous resources have no identifier which can be used for updating");
    }

    @Override
    protected void delete() {
        throw new UnsupportedOperationException("anonymous resources have no identifier which can be used for deleting");
    }

    @Override
    public void toXml(final StringBuilder buf) {
        buf.append("<").append(this.factory.storageName()).append(">");
        appendXml(buf);
        buf.append("</").append(this.factory.storageName()).append(">");
    }

    @Override
    public String toString() {
        final StringBuilder buf = new StringBuilder(getClass().getName()).append("(");
        toString(buf);
        return buf.append(")").toString();
    }
}
