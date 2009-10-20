/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public class CountingResourceChangeListener<T extends Resource<T>>
        implements ResourceChangeListener<T> {

    private int count = 0;

    @Override
    public void onChange(T resource) {
        count++;
    }

    public int count() {
        return count;
    }

    public void reset() {
        count = 0;
    }
}