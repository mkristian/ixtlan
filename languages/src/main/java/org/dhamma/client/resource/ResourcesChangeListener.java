/**
 * 
 */
package org.dhamma.client.resource;

public interface ResourcesChangeListener<E extends Resource<E>> {
    void onChange(Resources<E> resources, E resource);
}