/**
 * 
 */
package de.saumya.gwt.session.client;

public interface Notifications {

    void info(String message);

    void info(String[] messages);

    void warn(String message);

    void clear();

    void showAll();
}