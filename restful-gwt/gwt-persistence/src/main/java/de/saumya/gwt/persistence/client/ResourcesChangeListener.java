/**
 *
 */
package de.saumya.gwt.persistence.client;

public interface ResourcesChangeListener<E extends AbstractResource<E>> {
    void onLoaded(ResourceCollection<E> resources);
}
