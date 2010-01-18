/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourcesChangeListener<E extends Resource<E>> {
    void onChange(ResourceCollection<E> resources, E resource);

    void onLoaded(ResourceCollection<E> resources);
}