/**
 * 
 */
package de.saumya.gwt.translation.gui.client.views.phrases;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;

import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.widget.ButtonAction;
import de.saumya.gwt.translation.common.client.widget.DefaultResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.MutatingButtonAction;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;

class PhraseActions extends DefaultResourceActionPanel<Phrase> {

    private final Button               approve;

    private final ButtonAction<Phrase> approveHandler;

    private String                     locale;

    public PhraseActions(final GetTextController getTextController,
            final ResourceBindings<Phrase> bindings, final Session session,
            final PhraseFactory factory,
            final ResourceNotifications changeNotification) {
        super(getTextController, bindings, session, factory, changeNotification);
        this.approveHandler = new MutatingButtonAction<Phrase>(changeNotification,
                bindings) {

            @Override
            protected void action(final Phrase resource) {
                resource.addResourceChangeListener(new ResourceChangeListener<Phrase>() {

                    @Override
                    public void onError(final Phrase resource) {
                    }

                    @Override
                    public void onChange(final Phrase resource) {
                        getTextController.reset(resource);
                    }
                });
                resource.save("approve");
            }
        };
        this.approve = button(this, "approve", this.approveHandler);
    }

    void setLocale(final String locale) {
        this.locale = locale;
    }

    @Override
    public void reset(final ResourceCollection<Phrase> resources) {
        this.approve.setVisible(false);
        super.reset(resources);
    }

    @Override
    public void reset(final Phrase phrase) {
        this.approveHandler.reset(phrase);
        this.approve.setVisible(!phrase.isNew()
                && !phrase.isDeleted()
                && !phrase.isApproved()
                && this.session.isAllowed("approve",
                                          this.resourceName,
                                          this.locale));
        this.save.setVisible(!phrase.isNew()
                && !phrase.isDeleted()
                && this.session.isAllowed(Action.UPDATE,
                                          this.resourceName,
                                          this.locale));
        super.reset(phrase);

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