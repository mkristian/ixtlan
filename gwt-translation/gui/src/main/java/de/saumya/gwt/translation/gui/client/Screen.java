/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

public interface Screen {

    Screen child(String key);

    void showRead(String key);

    void showAll();

    void showEdit(String key);

    void showNew();
}