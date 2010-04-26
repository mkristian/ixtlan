/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.RadioButton;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.Translatable;

public class TranslatableRadioButton extends RadioButton implements
        Translatable {

    private String                  code = null;

    private final GetTextController getTextController;

    public TranslatableRadioButton(final GetTextController getTextController,
            final String name, final String label) {
        super(name, label);
        this.getTextController = getTextController;
        this.getTextController.addTranslatable(this);
        setText(label);
        sinkEvents(Event.MOUSEEVENTS);
    }

    @Override
    public void setText(final String text) {
        if (this.getTextController != null) {
            this.code = text;
            super.setText(this.getTextController.get(this.code));
        }
    }

    @Override
    public void reset() {
        this.getTextController.reset(this);
    }

    @Override
    public String getCode() {
        return this.code;
    }

}
