/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.Phrase;
import de.saumya.gwt.translation.common.client.PhraseBook;
import de.saumya.gwt.translation.common.client.PhraseBookFactory;

public class PhraseBookScreen extends VerticalPanel implements Screen {

    private final PhraseBookFactory bookFactory;

    private final GetText           getText;

    private final TranslatableLabel loading;

    private final PhraseScreen      phrasePanel;

    private final VerticalPanel     phrasesPanel = new VerticalPanel();

    public PhraseBookScreen(final PhraseBookFactory bookFactory,
            final PhraseScreen phrasePanel, final GetText getText) {
        this.bookFactory = bookFactory;
        this.getText = getText;
        this.loading = new TranslatableLabel("loading", this.getText);
        this.phrasePanel = phrasePanel;

        this.loading.setVisible(false);
        this.phrasePanel.setVisible(false);
        this.phrasesPanel.setVisible(false);

        add(this.loading);
        add(this.phrasesPanel);
        add(this.phrasePanel);
    }

    public void showRead(final String localeCode) {
        this.loading.setVisible(true);
        this.phrasePanel.setVisible(false);
        this.phrasesPanel.setVisible(false);

        this.bookFactory.get(localeCode,
                             new ResourceChangeListener<PhraseBook>() {

                                 @Override
                                 public void onChange(final PhraseBook resource) {
                                     PhraseBookScreen.this.phrasesPanel.clear();
                                     for (final Phrase phrase : resource.phrases) {
                                         PhraseBookScreen.this.phrasesPanel.add(new Hyperlink(phrase.toString(),
                                                 "/phrases/" + resource.locale
                                                         + "/phrase/"
                                                         + phrase.id + "/edit"));
                                     }
                                     PhraseBookScreen.this.phrasesPanel.setVisible(true);
                                     PhraseBookScreen.this.loading.setVisible(false);
                                 }
                             });
    }

    @Override
    public Screen child(final String key) {
        this.loading.setVisible(false);
        this.phrasePanel.setVisible(false);
        this.phrasesPanel.setVisible(false);
        return this.phrasePanel;
    }

    @Override
    public void showAll() {
        throw new UnsupportedOperationException("phrasebook does not show list");
    }

    @Override
    public void showEdit(final String key) {
        throw new UnsupportedOperationException("phrase does edit");
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("phrase does crerate new");
    }

}