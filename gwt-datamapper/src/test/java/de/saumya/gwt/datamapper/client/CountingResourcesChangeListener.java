/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public class CountingResourcesChangeListener<T extends Resource<T>>
        implements ResourcesChangeListener<T> {

    private int count = 0;

    @Override
    public void onChange(Resources<T> resources, T resource) {
        count++;
    }

    public int count() {
        return count;
    }

    public void reset() {
        count = 0;
    }
}