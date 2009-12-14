/**
 * 
 */
package de.saumya.gwt.persistence.client;

public interface ResourceNotification {

    void info(String message, Resource<? extends Resource<?>> resource);

    void error(int status, String message,
            Resource<? extends Resource<?>> resource);

}