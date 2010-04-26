/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourceChangeListener<E extends AbstractResource<E>> {
    void onChange(E resource);

    void onError(int status, String errorMessage, E resource);
}