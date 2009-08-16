/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseBook;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

public class PhraseBookScreen extends ResourceScreen<PhraseBook> {

    private final PhraseBookFactory bookFactory;

    private final TranslatableLabel loading;

    private final PhraseScreen      phraseScreen;

    private final VerticalPanel     phrasesPanel = new VerticalPanel();

    public PhraseBookScreen(final PhraseBookFactory bookFactory,
            final PhraseScreen phrasePanel, final GetText getText) {
        super(getText, new PathFactory(bookFactory.storageName()));
        this.bookFactory = bookFactory;
        this.loading = new TranslatableLabel("loading", getText);
        this.phraseScreen = phrasePanel;

        this.loading.setVisible(false);
        this.phraseScreen.setVisible(false);
        this.phrasesPanel.setVisible(false);

        add(this.loading);
        add(this.phrasesPanel);
        add(this.phraseScreen);
    }

    public void showRead(final String localeCode) {
        this.loading.setVisible(true);
        this.phraseScreen.setVisible(false);
        this.phrasesPanel.setVisible(false);

        this.bookFactory.get(localeCode,
                             new ResourceChangeListener<PhraseBook>() {

                                 @Override
                                 public void onChange(final PhraseBook resource) {
                                     PhraseBookScreen.this.phrasesPanel.clear();
                                     // PhraseBookScreen.this.phraseScreen.setupPathFactory(getPathFactory(),
                                     // resource.key());

                                     for (final Phrase phrase : resource.phrases) {
                                         PhraseBookScreen.this.phrasesPanel.add(new Hyperlink(phrase.toString(),
                                                 PhraseBookScreen.this.phraseScreen.getPathFactory()
                                                         .editPath(phrase.key())));
                                     }
                                     PhraseBookScreen.this.phrasesPanel.setVisible(true);
                                     PhraseBookScreen.this.loading.setVisible(false);
                                 }
                             });
    }

    @Override
    public Screen<Phrase> child(final String key) {
        this.loading.setVisible(false);
        this.phraseScreen.setVisible(false);
        this.phrasesPanel.setVisible(false);
        this.phraseScreen.setupPathFactory(getPathFactory(), key);
        GWT.log("setup child " + key, null);
        return this.phraseScreen;
    }

    @Override
    public void showAll() {
        throw new UnsupportedOperationException("phrasebookscreen does not show all");
    }

    @Override
    public void showEdit(final String key) {
        throw new UnsupportedOperationException("phrasebookscreen has no 'edit'");
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("phrasebookscreen has no 'new'");
    }

    @Override
    protected void reset(final PhraseBook resource) {
        // TODO Auto-generated method stub

    }

}