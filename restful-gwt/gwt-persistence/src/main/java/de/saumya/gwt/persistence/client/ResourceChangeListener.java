/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourceChangeListener<E extends Resource<E>> {
    void onChange(E resource);

    void onError(E resource);
}