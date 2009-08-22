/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ButtonHandler;
import de.saumya.gwt.translation.common.client.widget.ResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

class PhraseScreen extends ResourceScreen<Phrase> {

    static class PhraseActions extends ResourceActionPanel<Phrase> {

        private final Button                approve;

        private final ButtonHandler<Phrase> approveHandler = new ButtonHandler<Phrase>() {

                                                               @Override
                                                               protected void action(
                                                                       final Phrase resource) {
                                                                   resource.save();
                                                                   // TODO
                                                                   // implement
                                                                   // save("approve")
                                                               }
                                                           };

        public PhraseActions(final GetTextController getText,
                final Session session, final PhraseFactory factory) {
            super(getText, session, factory);
            this.approve = button("approve", this.approveHandler);
        }

        @Override
        protected void doReset(final Phrase phrase, final String locale) {
            this.approveHandler.reset(phrase);
            this.approve.setVisible(!phrase.isNew()
                    && !phrase.isDeleted()
                    && this.session.isAllowed("approve",
                                              this.resourceName,
                                              locale));
            this.save.setVisible(!phrase.isNew()
                    && !phrase.isDeleted()
                    && this.session.isAllowed(Action.UPDATE,
                                              this.resourceName,
                                              locale));

        }
    }

    PhraseScreen(final GetTextController getText,
            final PhraseFactory phraseFactory, final Session session) {
        super(getText,
                phraseFactory,
                session,
                new PhrasePanel(getText),
                new ResourceCollectionPanel<Phrase>(session),
                new PhraseActions(getText, session, phraseFactory));
    }

    @Override
    protected void reset(final Phrase resource) {
        GWT.log("reset:" + resource, null);
        reset(resource, resource.updatedAt, resource.updatedBy);
    }

    @Override
    public void setupPathFactory(final PathFactory parentPathFactory,
            final String locale) {
        // TODO maybe it is not good to use know that the key is the locale at
        // that part of the code
        super.setupPathFactory(parentPathFactory, locale);
        this.locale = locale;
    }

    @Override
    public Screen<?> child(final String key) {
        return null;
    }

    @Override
    public void showAll() {
        throw new UnsupportedOperationException("phrase does not show all");
    }

}