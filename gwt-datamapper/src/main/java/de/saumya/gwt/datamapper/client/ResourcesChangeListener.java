/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public interface ResourcesChangeListener<E extends Resource<E>> {
    void onChange(Resources<E> resources, E resource);

    void onLoaded(Resources<E> resources);
}