/**
 * 
 */
package org.dhamma.client;


public interface ResourcesChangeListener<T extends Resource> {
	void onChange(Resources<T> resources, T resource);
}