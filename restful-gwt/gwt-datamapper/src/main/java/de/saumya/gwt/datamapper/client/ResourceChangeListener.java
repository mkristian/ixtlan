/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public interface ResourceChangeListener<E extends Resource<E>> {
    void onChange(E resource);

    void onError(E resource, int status);
}