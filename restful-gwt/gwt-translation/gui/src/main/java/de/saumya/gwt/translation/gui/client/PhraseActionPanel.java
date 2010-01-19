/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.widget.AbstractResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ButtonAction;
import de.saumya.gwt.translation.common.client.widget.GetByPanel;
import de.saumya.gwt.translation.common.client.widget.MutatingButtonAction;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.SearchPanel;

public class PhraseActionPanel extends AbstractResourceActionPanel<Phrase> {

    private static final String        APPROVE = "approve";

    protected final Button             save;
    protected final Button             edit;
    protected final Button             reload;
    protected final Button             approve;

    private final ButtonAction<Phrase> reloadHandler;
    private final ButtonAction<Phrase> editHandler;
    private final ButtonAction<Phrase> saveHandler;
    private final ButtonAction<Phrase> approveHandler;

    private final Session              session;

    public PhraseActionPanel(final GetTextController getTextController,
            final ResourceBindings<Phrase> binding, final Session session,
            final ResourceFactory<Phrase> factory,
            final ResourceNotifications changeNotification) {
        super(getTextController, binding, session, factory);

        setStyleName("action-panel");
        this.session = session;
        this.reloadHandler = new ButtonAction<Phrase>(changeNotification) {

            @Override
            protected void action(final Phrase resource) {
                resource.reload();
            }

        };

        this.editHandler = new ButtonAction<Phrase>(changeNotification) {

            @Override
            protected void action(final Phrase resource) {
                History.newItem(PhraseActionPanel.this.pathFactory.editPath(resource.key()));
            }

        };

        this.saveHandler = new MutatingButtonAction<Phrase>(changeNotification,
                binding) {

            @Override
            protected void action(final Phrase resource) {
                resource.save();
            }

        };
        this.approveHandler = new MutatingButtonAction<Phrase>(changeNotification,
                binding) {

            @Override
            protected void action(final Phrase resource) {
                resource.save(APPROVE);
            }

        };

        final ComplexPanel actionButtons = new FlowPanel();
        actionButtons.setStyleName("action-buttons");
        this.reload = button(actionButtons, "reload", this.reloadHandler);
        this.edit = button(actionButtons, "edit", this.editHandler);
        this.save = button(actionButtons, "save", this.saveHandler);
        this.approve = button(actionButtons, "approve", this.approveHandler);

        add(new SearchPanel(getTextController, factory));
        add(new GetByPanel(getTextController, factory, session));
        add(actionButtons);
    }

    @Override
    public final void reset(final Phrase resource, final boolean readOnly) {
        this.reloadHandler.reset(resource);
        this.editHandler.reset(resource);
        this.saveHandler.reset(resource);
        this.approveHandler.reset(resource);

        // TODO this status check needs improvement
        this.reload.setVisible(!resource.isNew() && !resource.isDeleted()
                && this.session.isAllowed(Action.SHOW, this.resourceName));
        this.edit.setVisible(readOnly && !resource.isNew()
                && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));
        this.save.setVisible(!readOnly && !resource.isNew()
                && !resource.isDeleted()
                && this.session.isAllowed(Action.UPDATE, this.resourceName));
        this.approve.setVisible(!readOnly && !resource.isNew()
                && !resource.isDeleted() && !resource.isApproved()
                && this.session.isAllowed(APPROVE, this.resourceName));

        doReset(resource);

        setVisible(true);
    }

    @Override
    public final void reset() {
        this.reload.setVisible(false);
        this.edit.setVisible(false);
        this.save.setVisible(false);
        this.approve.setVisible(false);

        doReset();

        setVisible(true);
    }
}