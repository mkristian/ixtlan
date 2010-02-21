package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Hyperlink;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.Translatable;

/**
 * @author kristian
 * @author bill
 */
public class TranslatableHyperlink extends Hyperlink implements Translatable,
        Locatable {

    private String                  code = null;
    private final String            path;

    private final GetTextController getText;

    private static class HyperlinkClickHandler implements ClickHandler {

        @Override
        public void onClick(final ClickEvent event) {
            if (((TranslatableHyperlink) event.getSource()).path.equals(History.getToken())) {
                History.fireCurrentHistoryState();
            }
        }
    };

    private static final HyperlinkClickHandler CLICK_HANDLER = new HyperlinkClickHandler();

    TranslatableHyperlink(final String text, final String path,
            final GetTextController getTextController) {
        this.getText = getTextController;
        this.getText.addTranslatable(this);
        this.path = path;
        setText(text);
        sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.ONCONTEXTMENU);
        setTargetHistoryToken("/DEFAULT" + path);
        addClickHandler(CLICK_HANDLER);
    }

    public void reset(final String locale) {
        setTargetHistoryToken("/" + locale + this.path);
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