/**
 * 
 */
package de.saumya.gwt.persistence.client;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;

public class CountingResourceChangeListener<T extends Resource<T>> implements
        ResourceChangeListener<T> {

    private int count  = 0;

    private int errors = 0;

    @Override
    public void onChange(final T resource) {
        this.count++;
    }

    public int count() {
        return this.count;
    }

    public int errors() {
        return this.errors;
    }

    public void reset() {
        this.count = 0;
        this.errors = 0;
    }

    @Override
    public void onError(final T resource) {
        this.errors++;
    }
}