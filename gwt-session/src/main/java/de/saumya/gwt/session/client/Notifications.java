/**
 * 
 */
package de.saumya.gwt.session.client;

public interface Notifications {

    void info(String message);

    void warn(String message);

    void clear();

    void showAll();

}