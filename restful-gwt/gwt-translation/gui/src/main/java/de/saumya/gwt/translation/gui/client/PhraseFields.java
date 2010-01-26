/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.Translation;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

public class PhraseFields extends ResourceFields<Phrase> {

    private final TranslationLabel defaultTranslation;
    private final TranslationLabel parentTranslation;

    private static class TranslationLabel extends Label {

        private final Widget widget;

        TranslationLabel(final Widget widget) {
            this.widget = widget;
        }

        void reset(final Translation translation) {
            final boolean has = translation != null;
            if (has) {
                GWT.log("trans:" + translation.toString(), null);
                setText(translation.previousText + " => "
                        + (translation.text == null ? "" : translation.text)
                        + "\u00a0(" + translation.approvedBy.name + ")");
            }
            this.widget.setVisible(has);
            setVisible(has);
        }
    }

    PhraseFields(final GetTextController getTextController,
            final ResourceBindings<Phrase> bindings) {
        super(getTextController, bindings);

        this.defaultTranslation = addTranslationLabel("Default Translation");
        this.parentTranslation = addTranslationLabel("Parent Translation");

        add("current text", new TextBoxBinding<Phrase>() {

            @Override
            public void pullFrom(final Phrase resource) {
                setText(resource.currentText);
            }

            @Override
            public void pushInto(final Phrase resource) {
                resource.currentText = getText();
            }

            @Override
            public void setEnabled(final boolean isEnabled) {
                super.setEnabled(false);
            }
        });

        add("next text", new TextBoxBinding<Phrase>() {

            @Override
            public void pullFrom(final Phrase resource) {
                setText(resource.text);
            }

            @Override
            public void pushInto(final Phrase resource) {
                resource.text = getText();
            }

        });

    }

    protected TranslationLabel addTranslationLabel(final String text) {
        final Label head = new TranslatableLabel(this.getTextController, text);
        add(head);
        final TranslationLabel label = new TranslationLabel(head);
        add(label);
        return label;
    }

    @Override
    public void reset(final Phrase resource) {
        this.defaultTranslation.reset(resource.defaultTranslation);
        this.parentTranslation.reset(resource.parentTranslation);
        super.reset(resource);
    }
}