package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.Translatable;

/**
 * @author kristian
 * @author bill
 */
public class TranslatableLabel extends Label implements Translatable {

    private String                  code = null;

    private final GetTextController getText;

    public TranslatableLabel(final GetTextController getText) {
        this(null, getText);
    }

    public TranslatableLabel(final String text, final GetTextController getTextController) {
        super();
        this.getText = getTextController;
        this.getText.addTranslatable(this);
        setText(text);
        sinkEvents(Event.MOUSEEVENTS | Event.ONCONTEXTMENU);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        if (DOM.eventGetType(event) == Event.ONMOUSEUP
                && event.getButton() == NativeEvent.BUTTON_RIGHT) {
            this.getText.show(event.getClientX(), event.getClientY(), this);
        }
        else if (DOM.eventGetType(event) == Event.ONCONTEXTMENU) {
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

    @Override
    public void reset() {
        this.getText.reset(this);
    }

    @Override
    public String getCode() {
        return this.code;
    }

}