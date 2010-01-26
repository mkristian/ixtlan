/**
 * 
 */
package de.saumya.gwt.persistence.client;

public class CountingResourcesChangeListener<T extends Resource<T>> implements
        ResourcesChangeListener<T> {

    private int count = 0;

    // @Override
    // public void onChange(final ResourceCollection<T> resources, final T
    // resource) {
    // this.count++;
    // }

    public int count() {
        return this.count;
    }

    public void reset() {
        this.count = 0;
    }

    @Override
    public void onLoaded(final ResourceCollection<T> resources) {
        this.count = resources.size();
    }
}