/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;

import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.Translatable;

public class TranslatableButton extends Button implements Translatable {

    private String        code = null;

    private final GetText getText;

    public TranslatableButton(final String text, final GetText getText) {
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