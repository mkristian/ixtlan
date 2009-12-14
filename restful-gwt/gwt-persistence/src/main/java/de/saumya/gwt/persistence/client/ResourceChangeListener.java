/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourceChangeListener<E extends Resource<E>> {
    void onChange(E resource, String message);

    void onError(E resource, int status, String statusText);
}