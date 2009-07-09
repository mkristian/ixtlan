/**
 * 
 */
package org.dhamma.client;


public interface ResourceChangeListener<T extends Resource> {
	void onChange(T resource);
}