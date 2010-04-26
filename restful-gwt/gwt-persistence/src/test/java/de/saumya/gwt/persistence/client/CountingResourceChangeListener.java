/**
 *
 */
package de.saumya.gwt.persistence.client;

public class CountingResourceChangeListener<E extends AbstractResource<E>>
        extends ResourceChangeListenerAdapter<E> {

    private int count = 0;

    @Override
    public void onChange(final E resource) {
        this.count++;
    }

    public int count() {
        return this.count;
    }

    public void reset() {
        this.count = 0;
    }
}
