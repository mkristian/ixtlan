/**
 * 
 */
package de.saumya.gwt.datamapper.client;

public interface ResourceChangeListener<E extends Resource<E>> {
    void onChange(E resource);
}