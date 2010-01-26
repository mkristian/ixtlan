/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionListing;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionNavigation;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

class PhraseScreen extends ResourceScreen<Phrase> {

    PhraseScreen(final GetTextController getTextController,
            final PhraseFactory phraseFactory,
            final ResourceBindings<Phrase> binding, final Session session,
            final ResourceNotifications notifications) {
        super(getTextController,
                phraseFactory,
                session,
                new PhrasePanel(getTextController, binding),
                new ResourceCollectionPanel<Phrase>(new ResourceCollectionNavigation<Phrase>(phraseFactory,
                        getTextController),
                        new ResourceCollectionListing<Phrase>(session,
                                phraseFactory,
                                getTextController)),
                new PhraseActionPanel(getTextController,
                        binding,
                        session,
                        phraseFactory,
                        notifications),
                notifications);
    }

    @Override
    protected void reset(final Phrase resource) {
        reset(resource, resource.updatedAt, resource.updatedBy);
    }

    @Override
    public Screen<?> child(final String key) {
        return null;
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("phrase does not show new");
    }

}