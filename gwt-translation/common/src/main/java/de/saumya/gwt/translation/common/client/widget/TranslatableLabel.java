/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.Translatable;

public class TranslatableLabel extends Label implements Translatable {

    private String        code = null;

    private final GetText getText;

    public TranslatableLabel(final GetText getText) {
        this(null, getText);
    }

    public TranslatableLabel(final String text, final GetText getText) {
        super();
        this.getText = getText;
        this.getText.addWidget(this);
        setText(text);
        sinkEvents(Event.MOUSEEVENTS);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        if (DOM.eventGetType(event) == Event.ONMOUSEUP
                && event.getButton() == NativeEvent.BUTTON_RIGHT
                && this.getText.isInTranslation()) {
            this.getText.popupTranslation(event, this);
            event.stopPropagation();
            event.preventDefault();
        }
        else if (DOM.eventGetType(event) == Event.ONMOUSEDOWN
                && this.getText.isInTranslation()) {
            event.stopPropagation();
            event.preventDefault();
        }
        else {
            super.onBrowserEvent(event);
        }
    }

    @Override
    public void setText(final String text) {
        this.code = text;
        super.setText(this.getText.get(this.code));
    }

    public void reset() {
        setText(this.code);
        if (this.getText.isInTranslation()) {
            addStyleDependentName("translatable");
        }
        else {
            removeStyleDependentName("translatable");
        }
    }

    public String getCode() {
        return this.code;
    }

}