/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseBook;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

public class PhraseBookScreen extends ResourceScreen<PhraseBook> {

    private final PhraseBookFactory bookFactory;

    private final PhraseScreen      phraseScreen;

    private final VerticalPanel     phrasesPanel = new VerticalPanel();

    public PhraseBookScreen(final PhraseBookFactory bookFactory,
            final PhraseScreen phrasePanel, final GetTextController getText,
            final Session session) {
        super(getText,
                bookFactory,
                session,
                new ResourcePanel<PhraseBook>(),
                new ResourceCollectionPanel<PhraseBook>(session));
        this.bookFactory = bookFactory;
        this.phraseScreen = phrasePanel;

        this.phraseScreen.setVisible(false);
        this.phrasesPanel.setVisible(false);

        add(this.phrasesPanel);
        add(this.phraseScreen);
    }

    @Override
    protected void show(final String localeCode) {
        this.phraseScreen.setVisible(false);
        this.phrasesPanel.setVisible(false);

        this.bookFactory.get(localeCode,
                             new ResourceChangeListener<PhraseBook>() {

                                 @Override
                                 public void onChange(final PhraseBook resource) {
                                     PhraseBookScreen.this.phrasesPanel.clear();
                                     final HyperlinkFactory factory = new HyperlinkFactory(PhraseBookScreen.this.phraseScreen.getPathFactory());
                                     for (final Phrase phrase : resource.phrases) {
                                         GWT.log(phrase.toString(), null);
                                         PhraseBookScreen.this.phrasesPanel.add(factory.editResourceHyperklink(phrase));
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
    public void showEdit(final String key) {
        throw new UnsupportedOperationException("phrasebookscreen has no 'edit'");
    }

    @Override
    protected void reset(final PhraseBook resource) {
        reset(resource, null, null);
    }

}