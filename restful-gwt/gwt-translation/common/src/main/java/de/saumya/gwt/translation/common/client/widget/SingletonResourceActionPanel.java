/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.SingletonResource;
import de.saumya.gwt.persistence.client.SingletonResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;

public class SingletonResourceActionPanel<E extends SingletonResource<E>>
        extends AbstractResourceActionPanel<E> {
    protected final Button                   save;
    protected final Button                   edit;
    protected final Button                   reload;

    private final ButtonAction<E>            reloadHandler;
    private final ButtonAction<E>            editHandler;
    private final ButtonAction<E>            saveHandler;

    protected final Session                  session;

    // private final ResourceChangeListener<E> createdListener;
    private final AbstractResourceFactory<E> factory;
    private final HyperlinkFactory           hyperlinkFactory;

    private E                                resource;
    private boolean                          isReadOnly;

    @SuppressWarnings("unchecked")
    public SingletonResourceActionPanel(
            final GetTextController getTextController,
            final ResourceBindings<E> bindings, final Session session,
            final SingletonResourceFactory<E> factory,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(getTextController, bindings, session, factory);
        this.session = session;
        this.factory = factory;
        this.hyperlinkFactory = hyperlinkFactory;
        this.reloadHandler = new ButtonAction<E>(listeners.loaded) {

            @Override
            protected void action(final E resource) {
                resource.reload();
            }

        };

        this.editHandler = new ButtonAction<E>(null) {

            @Override
            protected void action(final E resource) {
                History.newItem(SingletonResourceActionPanel.this.pathFactory.editPath(0));
            }

        };
        this.saveHandler = new MutatingButtonAction<E>(listeners.updated,
                bindings) {

            @Override
            protected void action(final E resource) {
                resource.save();
            }

        };

        final ComplexPanel newCreate = new FlowPanel();
        newCreate.setStyleName("new-create");
        final ComplexPanel actionButtons = new FlowPanel();
        actionButtons.setStyleName("action-buttons");
        this.reload = button(actionButtons, "reload", this.reloadHandler);
        this.save = button(actionButtons, "save", this.saveHandler);
        this.edit = button(actionButtons, "edit", this.editHandler);

        final FlowPanel buttons = new FlowPanel();
        buttons.setStyleName("resource-buttons");
        buttons.add(newCreate);
        buttons.add(actionButtons);
        add(buttons);
    }

    protected ComplexPanel createGetByPanel() {
        return new GetByPanel(this.getTextController,
                this.factory,
                this.session,
                this.hyperlinkFactory);
    }

    protected ComplexPanel createSearchPanel() {
        return new SearchPanel(this.getTextController,
                this.factory,
                this.hyperlinkFactory);
    }

    // TODO move into abstract class
    @Override
    public void reset(final ResourceCollection<E> resources) {
        this.reload.setVisible(false);
        this.edit.setVisible(false);
        this.save.setVisible(false);
        setVisible(true);
    }

    @Override
    public void reset(final E resource) {
        this.resource = resource;

        this.reloadHandler.reset(resource);
        this.editHandler.reset(resource);
        this.saveHandler.reset(resource);

        // resource.addResourceChangeListener(this.createdListener);

        resetVisibility();

        setVisible(true);
    }

    private void resetVisibility() {
        if (this.resource != null) {
            // TODO this status check needs improvement
            this.reload.setVisible(!this.resource.isNew()
                    && !this.resource.isDeleted()
                    && this.session.isAllowed(Action.SHOW, this.resourceName));
            this.edit.setVisible(this.isReadOnly && !this.resource.isNew()
                    && !this.resource.isDeleted()
                    && !this.resource.isImmutable()
                    && this.session.isAllowed(Action.UPDATE, this.resourceName));
            this.save.setVisible(!this.isReadOnly && !this.resource.isNew()
                    && !this.resource.isDeleted()
                    && !this.resource.isImmutable()
                    && this.session.isAllowed(Action.UPDATE, this.resourceName));
        }
    }

    @Override
    public boolean isReadOnly() {
        return this.isReadOnly;
    }

    @Override
    public void setReadOnly(final boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
        resetVisibility();
    }
}