package de.saumya.gwt.translation.common.client;

public interface Translatable {

    void reset();

    String getCode();

    void setText(String text);

    void addStyleName(String name);

    void removeStyleName(String name);
}
