/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.Translatable;

public class TranslatableHyperlink extends Hyperlink implements Translatable {

    private String                  code = null;

    private final GetTextController getText;

    public TranslatableHyperlink(final String text,
            final GetTextController getText) {
        this.getText = getText;
        this.getText.addTranslatable(this);
        setText(text);
        sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK);
    }

    public TranslatableHyperlink(final String text, final String path,
            final GetTextController getText) {
        this(text, getText);
        setTargetHistoryToken(path);
    }

    @Override
    public void onBrowserEvent(final Event event) {
        if (DOM.eventGetType(event) == Event.ONMOUSEUP
                && event.getButton() == NativeEvent.BUTTON_RIGHT) {
            this.getText.show(event.getClientX(), event.getClientY(), this);
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