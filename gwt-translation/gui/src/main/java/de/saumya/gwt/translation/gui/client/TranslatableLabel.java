/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

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
        this.getText.addWidget(this, this);
        setText(text);
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