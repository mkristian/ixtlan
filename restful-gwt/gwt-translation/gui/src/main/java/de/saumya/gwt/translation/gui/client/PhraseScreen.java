/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.persistence.client.Resources;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

class PhraseScreen extends ResourceScreen<Phrase> {

    private final PhraseActions         phraseActions;
    private final PhraseCollectionPanel phraseCollection;

    PhraseScreen(final GetTextController getTextController,
            final PhraseFactory phraseFactory,
            final ResourceMutator<Phrase> mutator, final Session session) {
        super(getTextController,
                phraseFactory,
                session,
                new PhrasePanel(getTextController, mutator),
                new PhraseCollectionPanel(session, phraseFactory),
                new PhraseActions(getTextController,
                        mutator,
                        session,
                        phraseFactory));
        this.phraseActions = (PhraseActions) this.actions;
        this.phraseCollection = (PhraseCollectionPanel) this.displayAll;
    }

    void showAll(final Resources<Phrase> resources) {
        reset(resources);
    }

    @Override
    protected void reset(final Phrase resource) {
        reset(resource, resource.updatedAt, resource.updatedBy);
    }

    @Override
    public Screen<?> child(final String key) {
        return null;
    }

    void setParentKey(final String key) {
        this.phraseActions.setLocale(key);
        this.phraseCollection.setLocale(key);
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("phrase does not show new");
    }

    @Override
    public void showAll() {
        throw new UnsupportedOperationException("phrase does not show all");
    }

}