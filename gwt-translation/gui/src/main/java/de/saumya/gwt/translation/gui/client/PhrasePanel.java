/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.Translation;
import de.saumya.gwt.translation.common.client.widget.AttributePanel;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

class PhrasePanel extends ResourcePanel<Phrase> {

    private final TranslationPanel defaultTranslation;
    private final TranslationPanel parentTranslation;

    class TranslationPanel extends HorizontalPanel {
        private final Label             text;
        private final TranslatableLabel label;

        TranslationPanel(final String label,
                final GetTextController getTextController) {
            this.label = new TranslatableLabel(label, getTextController);
            this.text = new Label();
            add(this.label);
            add(this.text);
        }

        void reset(final Translation translation) {
            final boolean has = translation != null;
            if (has) {
                GWT.log("trans:" + translation.toString(), null);
                this.text.setText(translation.previousText + " => "
                        + (translation.text == null ? "" : translation.text)
                        + "\u00a0(" + translation.approvedBy.name + ")");
            }
            this.label.setVisible(has);
            this.text.setVisible(has);
            setVisible(has);
        }
    }

    PhrasePanel(final GetTextController getText) {
        this.defaultTranslation = new TranslationPanel("DEFAULT", getText);
        this.parentTranslation = new TranslationPanel("parent", getText);

        add(this.defaultTranslation);
        add(this.parentTranslation);
        add(new AttributePanel<Phrase>("current text", getText) {

            @Override
            protected String value(final Phrase resource) {
                return resource.currentText;
            }

        });
        add(new AttributePanel<Phrase>("next text", getText) {

            @Override
            protected String value(final Phrase resource) {
                return resource.text;
            }

        });
    }

    @Override
    protected void doReset(final Phrase resource) {
        this.defaultTranslation.reset(resource.defaultTranslation);
        this.parentTranslation.reset(resource.parent);
        setVisible(true);
    }
}