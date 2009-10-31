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
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

public class PhrasePanel extends ResourcePanel<Phrase> {

    private final TranslationLabel defaultTranslation;
    private final TranslationLabel parentTranslation;

    class TranslationLabel extends Label {

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

    PhrasePanel(final GetTextController getTextController,
            final ResourceMutator<Phrase> mutator) {
        super(getTextController, mutator);

        this.defaultTranslation = addTranslationLabel("Default Translation");
        this.parentTranslation = addTranslationLabel("Parent Translation");

        addTranslatableLabel("current text");
        add(new TextBoxMutator<Phrase>(mutator) {

            @Override
            public void pull(final Phrase resource) {
                setText(resource.currentText);
            }

            @Override
            public void push(final Phrase resource) {
                resource.currentText = getText();
            }

            @Override
            public void setEnabled(final boolean isEnabled) {
                super.setEnabled(false);
            }
        });

        addTranslatableLabel("next text");
        add(new TextBoxMutator<Phrase>(mutator) {

            @Override
            public void pull(final Phrase resource) {
                setText(resource.text);
            }

            @Override
            public void push(final Phrase resource) {
                resource.text = getText();
            }

        });

    }

    protected TranslationLabel addTranslationLabel(final String text) {
        final Label head = new TranslatableLabel(text, this.getTextController);
        add(head);
        final TranslationLabel label = new TranslationLabel(head);
        add(label);
        return label;
    }

    @Override
    protected void doReset(final Phrase resource) {
        this.defaultTranslation.reset(resource.defaultTranslation);
        this.parentTranslation.reset(resource.parentTranslation);
    }
}