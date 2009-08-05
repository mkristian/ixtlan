/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public class CountingResourcesChangeListener<T extends Resource<T>> implements
        ResourcesChangeListener<T> {

    private int count = 0;

    @Override
    public void onChange(final Resources<T> resources, final T resource) {
        this.count++;
    }

    public int count() {
        return this.count;
    }

    public void reset() {
        this.count = 0;
    }

    @Override
    public void onLoaded(final Resources<T> resources) {
    }
}