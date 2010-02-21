/**
 * 
 */
package de.saumya.gwt.translation.common.client;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.translation.common.client.model.Phrase;

public class GetTextController {

    private final GetText               getText;

    private final TranslationPopupPanel popup;

    public GetTextController(final GetText getText, final Session session) {
        this.getText = getText;
        this.popup = new TranslationPopupPanel(this, session);
    }

    public void load(final Locale locale, final boolean isInTranslation) {
        this.getText.load(locale, isInTranslation);
    }

    public void reset(final Phrase phrase) {
        this.getText.resetWord(phrase);
        this.getText.resetTranslatables();
    }

    public String get(final String code) {
        return this.getText.get(code);
    }

    public void show(final int clientX, final int clientY,
            final Translatable translatable) {
        if (this.getText.isInTranslation()) {
            this.popup.setup(this.getText.getPhrase(translatable.getCode()),
                             translatable);
            this.popup.setPopupPosition(clientX, clientY);
            this.popup.show();
        }
    }

    public void addTranslatable(final Translatable translatable) {
        this.getText.addTranslatable(translatable);
    }

    public void reset(final Translatable translatable) {
        translatable.setText(translatable.getCode());
        if (this.getText.isInTranslation()) {
            translatable.addStyleName("translatable");
            final Phrase phrase = this.getText.getPhrase(translatable.getCode());
            if (phrase.text != null
                    && !phrase.text.equals(phrase.parentTranslation.text)) {
                translatable.addStyleName("to_be_approved");
            }
            else if (phrase.parentTranslation != null) {
                translatable.removeStyleName("to_be_translated");
            }
        }
        else {
            final Phrase phrase = this.getText.getPhrase(translatable.getCode());
            if (phrase != null) {
                translatable.setText(phrase.currentText);
            }
            translatable.removeStyleName("translatable");
            translatable.removeStyleName("to_be_approved");
            translatable.removeStyleName("to_be_translated");
        }
    }
}