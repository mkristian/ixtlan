/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.widget.LoadingNotice;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionListing;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionNavigation;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceHeaderPanel;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

class PhraseScreen extends ResourceScreen<Phrase> {

    static class PhraseHeaders extends ResourceHeaderPanel<Phrase> {

        public PhraseHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        @Override
        public void reset(final Phrase resource) {
            reset(resource, resource.updatedAt, resource.updatedBy);
        }

    }

    PhraseScreen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final PhraseFactory phraseFactory,
            final ResourceBindings<Phrase> binding, final Session session,
            final ResourceNotifications notifications) {
        super(loadingNotice,
                phraseFactory,
                session,
                new ResourcePanel<Phrase>(new PhraseHeaders(getTextController),
                        new PhraseFields(getTextController, binding)),
                new ResourceCollectionPanel<Phrase>(loadingNotice,
                        new ResourceCollectionNavigation<Phrase>(loadingNotice,
                                phraseFactory,
                                getTextController),
                        new ResourceCollectionListing<Phrase>(session,
                                phraseFactory,
                                getTextController)),
                new PhraseActions(getTextController,
                        binding,
                        session,
                        phraseFactory,
                        notifications),
                notifications);
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("phrase does not show new");
    }

}