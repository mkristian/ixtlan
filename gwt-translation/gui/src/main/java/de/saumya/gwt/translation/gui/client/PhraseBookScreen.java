/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseBook;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

public class PhraseBookScreen extends ResourceScreen<PhraseBook> {

    static class PhraseBookPanel extends ResourcePanel<PhraseBook> {

        private final PhraseScreen phraseScreen;

        PhraseBookPanel(final PhraseScreen phraseScreen) {
            this.phraseScreen = phraseScreen;
            add(phraseScreen);
        }

        @Override
        protected void doReset(final PhraseBook resource) {
            this.phraseScreen.showAll(resource.phrases);
        }
    }

    private final PhraseScreen phraseScreen;

    public PhraseBookScreen(final PhraseBookFactory bookFactory,
            final PhraseScreen phraseScreen,
            final GetTextController getTextController, final Session session) {
        super(getTextController,
                bookFactory,
                session,
                new PhraseBookPanel(phraseScreen),
                new PhraseBookCollectionPanel(session,
                        bookFactory,
                        getTextController));
        this.phraseScreen = phraseScreen;
    }

    @Override
    public Screen<Phrase> child(final String key) {
        this.phraseScreen.setParentKey(key);
        return this.phraseScreen;
    }

    @Override
    public void showRead(final String key) {
        this.phraseScreen.setParentKey(key);
        this.phraseScreen.setup(getPathFactory().showPath(key));
        show(key);
        this.header.setVisible(false);
        this.actions.setVisible(false);
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