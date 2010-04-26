/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.AbstractResourceFactory;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceChangeListenerAdapter;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;

public class ResourceActionPanel<E extends Resource<E>> extends
        AbstractResourceActionPanel<E> {
    protected final Button                   fresh;
    protected final Button                   create;
    protected final Button                   save;
    protected final Button                   edit;
    protected final Button                   reload;
    protected final Button                   delete;

    private final ButtonAction<E>            newHandler;
    private final ButtonAction<E>            createHandler;
    private final ButtonAction<E>            reloadHandler;
    private final ButtonAction<E>            editHandler;
    private final ButtonAction<E>            saveHandler;
    private final ButtonAction<E>            destroyHandler;

    protected final Session                  session;

    private final ResourceChangeListener<E>  createdListener;
    private final AbstractResourceFactory<E> factory;
    private final HyperlinkFactory           hyperlinkFactory;

    private E                                resource;
    private boolean                          isReadOnly;

    @SuppressWarnings("unchecked")
    public ResourceActionPanel(final GetTextController getTextController,
            final ResourceBindings<E> bindings, final Session session,
            final AbstractResourceFactory<E> factory,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory, final boolean hasSearch,
            final boolean hasGetById) {
        super(getTextController, bindings, session, factory);
        this.session = session;
        this.factory = factory;
        this.hyperlinkFactory = hyperlinkFactory;

        // TODO remove this hack and display the right screen directly
        this.createdListener = new ResourceChangeListenerAdapter<E>() {

            @Override
            public void onChange(final E resource) {
                if (resource.isUptodate()
                        && History.getToken()
                                .equals(ResourceActionPanel.this.pathFactory.newPath())) {
                    History.newItem(ResourceActionPanel.this.pathFactory.editPath(((Resource<?>) resource).id));
                }
            }
        };
        this.newHandler = new ButtonAction<E>(null) {

            @Override
            protected void action(final E resource) {
                History.newItem(ResourceActionPanel.this.pathFactory.newPath());
            }

        };
        this.reloadHandler = new ButtonAction<E>(listeners.loaded) {

            @Override
            protected void action(final E resource) {
                resource.reload();
            }

        };

        this.editHandler = new ButtonAction<E>(null) {

            @Override
            protected void action(final E resource) {
                History.newItem(ResourceActionPanel.this.pathFactory.editPath(resource.id));
            }

        };

        this.createHandler = new MutatingButtonAction<E>(listeners.created,
                bindings) {

            @Override
            protected void action(final E resource) {
                resource.addResourceChangeListener(ResourceActionPanel.this.createdListener);
                resource.save();
            }
        };

        this.saveHandler = new MutatingButtonAction<E>(listeners.updated,
                bindings) {

            @Override
            protected void action(final E resource) {
                resource.save();
            }

        };
        this.destroyHandler = new ButtonAction<E>(listeners.deleted) {

            @Override
            protected void action(final E resource) {
                resource.destroy();
                History.newItem(ResourceActionPanel.this.pathFactory.showAllPath());
            }

        };

        final ComplexPanel search = hasSearch ? createSearchPanel() : null;

        final ComplexPanel getBy = hasGetById ? createGetByPanel() : null;

        final ComplexPanel newCreate = new FlowPanel();
        newCreate.setStyleName("new-create");
        this.fresh = button(newCreate, "new", this.newHandler);
        this.create = button(newCreate, "create", this.createHandler);
        final ComplexPanel actionButtons = new FlowPanel();
        actionButtons.setStyleName("action-buttons");
        this.reload = button(actionButtons, "reload", this.reloadHandler);
        this.save = button(actionButtons, "save", this.saveHandler);
        this.edit = button(actionButtons, "edit", this.editHandler);
        this.delete = button(actionButtons, "delete", this.destroyHandler);

        if (search != null) {
            add(search);
        }
        if (getBy != null) {
            add(getBy);
        }
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
        this.create.setVisible(false);
        this.reload.setVisible(false);
        this.edit.setVisible(false);
        this.save.setVisible(false);
        this.delete.setVisible(false);
        this.fresh.setVisible(this.session.isAllowed(Session.Action.CREATE,
                                                     this.resourceName));

        setVisible(true);
    }

    @Override
    public void reset(final E resource) {
        this.resource = resource;

        this.newHandler.reset(resource);
        this.reloadHandler.reset(resource);
        this.createHandler.reset(resource);
        this.editHandler.reset(resource);
        this.saveHandler.reset(resource);
        this.destroyHandler.reset(resource);

        resource.addResourceChangeListener(this.createdListener);

        resetVisibility();

        setVisible(true);
    }

    private void resetVisibility() {
        if (this.resource != null) {
            // TODO this status check needs improvement
            this.delete.setVisible(this.resource.id != 0
                    && !this.resource.isNew()
                    && !this.resource.isDeleted()
                    && this.session.isAllowed(Action.DESTROY, this.resourceName));

            this.reload.setVisible(!this.resource.isNew()
                    && !this.resource.isDeleted()
                    && this.session.isAllowed(Action.SHOW, this.resourceName));
            this.edit.setVisible(this.resource.id != 0 && this.isReadOnly
                    && !this.resource.isNew() && !this.resource.isDeleted()
                    && !this.resource.isImmutable()
                    && this.session.isAllowed(Action.UPDATE, this.resourceName));
            this.save.setVisible(!this.isReadOnly && !this.resource.isNew()
                    && !this.resource.isDeleted()
                    && !this.resource.isImmutable()
                    && this.session.isAllowed(Action.UPDATE, this.resourceName));
            this.create.setVisible(this.resource.isNew()
                    && this.session.isAllowed(Action.CREATE, this.resourceName));
            this.fresh.setVisible(!this.resource.isNew()
                    && this.session.isAllowed(Action.CREATE, this.resourceName));
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