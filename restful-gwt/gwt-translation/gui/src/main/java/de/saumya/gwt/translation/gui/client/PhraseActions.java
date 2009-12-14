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
import de.saumya.gwt.translation.common.client.widget.ButtonHandler;
import de.saumya.gwt.translation.common.client.widget.ResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;

class PhraseActions extends ResourceActionPanel<Phrase> {

    private final Button                approve;

    private final ButtonHandler<Phrase> approveHandler;

    private String                      locale;

    public PhraseActions(final GetTextController getText,
            final ResourceMutator<Phrase> mutator, final Session session,
            final PhraseFactory factory) {
        super(getText, mutator, session, factory);
        this.approveHandler = new ButtonHandler<Phrase>(mutator) {

            @Override
            protected void action(final Phrase resource) {
                // TODO implement save("approve")
                resource.save();
            }
        };
        this.approve = button(this, "approve", this.approveHandler);
    }

    void setLocale(final String locale) {
        this.locale = locale;
    }

    @Override
    protected void doReset() {
        this.approve.setVisible(false);
    }

    @Override
    protected void doReset(final Phrase phrase) {
        this.approveHandler.reset(phrase);
        this.approve.setVisible(!phrase.isNew()
                && !phrase.isDeleted()
                && this.session.isAllowed("approve",
                                          this.resourceName,
                                          this.locale));
        this.save.setVisible(!phrase.isNew()
                && !phrase.isDeleted()
                && this.session.isAllowed(Action.UPDATE,
                                          this.resourceName,
                                          this.locale));

        GWT.log("actions: "
                + this.locale
                + " "
                + this.resourceName
                + " "
                + this.session.isAllowed("approve",
                                         this.resourceName,
                                         this.locale)
                + " "
                + this.session.isAllowed(Action.UPDATE,
                                         this.resourceName,
                                         this.locale), null);
    }
}