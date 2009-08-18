/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.model.Translation;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.AttributePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

class PhraseScreen extends ResourceScreen<Phrase> {

    private final PhraseFactory     phraseFactory;

    private final TranslatableLabel loading;

    private final Label             defaultText;
    private final TranslatableLabel defaultLabel;

    private final Label             parentText;
    private final TranslatableLabel parentLabel;

    PhraseScreen(final GetTextController getText,
            final PhraseFactory phraseFactory) {
        super(getText, phraseFactory);
        this.phraseFactory = phraseFactory;
        this.loading = new TranslatableLabel("loading", getText);
        setVisible(false);
        this.defaultLabel = new TranslatableLabel("DEFAULT", getText);
        this.defaultText = new Label();
        this.parentLabel = new TranslatableLabel("DEFAULT", getText);
        this.parentText = new Label();
        add(this.defaultLabel);
        add(this.defaultText);
        add(this.parentLabel);
        add(this.parentText);
        add(new AttributePanel<Phrase>("current text", true, getText) {

            @Override
            protected String value(final Phrase resource) {
                return resource.currentText;
            }

        });
        add(new AttributePanel<Phrase>("next text", true, getText) {

            @Override
            protected String value(final Phrase resource) {
                return resource.text;
            }

        });
    }

    @SuppressWarnings("unchecked")
    private void setReadOnly(final boolean isReadOnly) {
        for (final Widget panel : getChildren()) {
            if (panel instanceof AttributePanel) {
                ((AttributePanel<Phrase>) panel).setReadOnly(isReadOnly);
            }
        }
    }

    @Override
    protected void reset(final Phrase resource) {
        super.reset(resource, resource.updatedAt, resource.updatedBy);
        setupTranslation(resource.defaultTranslation,
                         this.defaultLabel,
                         this.defaultText);
        setupTranslation(resource.parent, this.parentLabel, this.parentText);
        // parentLabel.setText(resource.)
    }

    private void setupTranslation(final Translation translation,
            final Label label, final Label text) {

        final boolean has = translation != null;
        label.setVisible(has);
        text.setVisible(has);
        if (has) {
            text.setText(translation.previousText + " => " + translation.text
                    + "(" + translation.approvedBy.name + ")");
        }
    }

    @Override
    public Screen<?> child(final String key) {
        return null;
        // throw new UnsupportedOperationException("phrase has no child");
    }

    @Override
    public void showAll() {
        throw new UnsupportedOperationException("phrase does not show all");
    }

    @Override
    public void showEdit(final String key) {
        setReadOnly(false);
        show(key);
    }

    @Override
    public void showNew() {
        reset(this.phraseFactory.newResource());
        PhraseScreen.this.loading.setVisible(false);
        setVisible(true);
    }

    @Override
    public void showRead(final String key) {
        setReadOnly(true);
        show(key);
    }

    private void show(final String key) {
        this.loading.setVisible(true);
        setVisible(true);
        final Phrase phrase = this.phraseFactory.get(key,
                                                     new ResourceChangeListener<Phrase>() {

                                                         @Override
                                                         public void onChange(
                                                                 final Phrase resource) {
                                                             reset(resource);
                                                             PhraseScreen.this.loading.setVisible(false);
                                                         }
                                                     });
        reset(phrase);
    }
}