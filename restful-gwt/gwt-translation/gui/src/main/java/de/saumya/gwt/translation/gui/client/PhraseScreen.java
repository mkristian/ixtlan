/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

class PhraseScreen extends ResourceScreen<Phrase> {

    // private final PhraseActionPanel phraseActions;
    private final PhraseCollectionPanel phraseCollection;

    PhraseScreen(final GetTextController getTextController,
            final PhraseFactory phraseFactory,
            final ResourceBindings<Phrase> binding, final Session session,
            final ResourceNotifications notifications) {
        super(getTextController,
                phraseFactory,
                session,
                new PhrasePanel(getTextController, binding),
                new PhraseCollectionPanel(session, phraseFactory),
                new PhraseActionPanel(getTextController,
                        binding,
                        session,
                        phraseFactory,
                        notifications),
                notifications);
        // this.phraseActions = (PhraseActionPanel) this.actions;
        this.phraseCollection = (PhraseCollectionPanel) this.displayAll;
    }

    void showAll(final ResourceCollection<Phrase> resources) {
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
        // this.phraseActions.setLocale(key);
        this.phraseCollection.setLocale(key);
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("phrase does not show new");
    }

    // @Override
    // public void showAll() {
    // throw new UnsupportedOperationException("phrase does not show all");
    // }

}