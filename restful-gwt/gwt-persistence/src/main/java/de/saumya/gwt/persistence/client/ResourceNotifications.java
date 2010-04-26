/**
 *
 */
package de.saumya.gwt.persistence.client;

public interface ResourceNotifications {

    void info(String message, AbstractResource<?> resource);

    void error(int status, String message, AbstractResource<?> resource);

}
