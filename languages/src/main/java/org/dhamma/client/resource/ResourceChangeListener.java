/**
 * 
 */
package org.dhamma.client.resource;

public interface ResourceChangeListener<E extends Resource<E>> {
    void onChange(E resource);
}