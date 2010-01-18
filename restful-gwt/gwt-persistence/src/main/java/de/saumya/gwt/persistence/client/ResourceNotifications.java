/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourceNotifications {

    void info(String message, Resource<?> resource);

    void error(int status, String message, Resource<?> resource);

}