/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourcesChangeListener<E extends Resource<E>> {
    void onLoaded(ResourceCollection<E> resources);
}