/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.sql.Timestamp;

import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.session.client.model.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public abstract class ResourceScreen<E extends Resource<E>> extends FlowPanel
        implements Screen<E> {

    private final PathFactory                  pathFactory;

    protected final ResourceFactory<E>         factory;

    protected final ResourceHeaderPanel        header;

    protected final AbstractResourceActionPanel<E>     actions;

    protected final ResourcePanel<E>           display;

    protected final ResourceCollectionPanel<E> displayAll;

    protected final Session                    session;

    protected final TranslatableLabel          loading;

    private PathFactory                        parentPathFactory;

    protected E                                resource;

    protected ResourceScreen(final GetTextController getText,
            final ResourceFactory<E> factory, final Session session,
            final ResourcePanel<E> display,
            final ResourceCollectionPanel<E> displayAll,
            final AbstractResourceActionPanel<E> actions) {
        setStyleName("screen");
        this.loading = new TranslatableLabel("loading ...", getText);
        this.loading.setStyleName("loading");
        this.header = new ResourceHeaderPanel(getText);
        this.actions = actions;
        this.display = display;
        this.displayAll = displayAll;
        this.factory = factory;
        this.session = session;

        add(this.loading);
        add(this.actions);
        add(this.header);
        add(this.display);
        if (displayAll != null) {
            add(this.displayAll);
            this.pathFactory = new PathFactory(factory.storagePluralName());
        }
        else {
            this.pathFactory = new PathFactory(factory.storageName());
        }
        this.parentPathFactory = this.pathFactory;
    }

    @Override
    public PathFactory getPathFactory() {
        return this.parentPathFactory;
    }

    @Override
    public void setup(final String parentPath) {
        this.parentPathFactory = parentPath == null
                ? this.pathFactory
                : this.pathFactory.newPathFactory(parentPath);
        this.actions.setup(getPathFactory());
        if (this.displayAll != null) {
            this.displayAll.setup(getPathFactory());
        }
    }

    /**
     * only the resource knows whether it has updated Timestamp and/or updatedBy
     * User. an implementation needs to forward the respective info to the
     * {@link ResourceScreen#reset(Resource, Timestamp, User)} using null where
     * the info does not exists
     * 
     * @param resource
     */
    abstract protected void reset(final E resource);

    protected final void reset(final E resource, final Timestamp updatedAt,
            final User updatedBy) {
        this.header.reset(resource.isNew() || resource.isDeleted()
                                  || !resource.isUptodate()
                                  ? null
                                  : resource.key(),
                          updatedAt,
                          updatedBy);
        this.actions.reset(resource, this.display.isReadOnly());
        this.display.reset(resource);

        if (this.displayAll != null) {
            this.displayAll.setVisible(false);
        }
        setVisible(true);
    }

    @Override
    public void showAll() {
        final ResourceCollection<E> resources = this.factory.all(new ResourcesChangeListener<E>() {

            @Override
            public void onChange(final ResourceCollection<E> resources, final E resource) {
            }

            @Override
            public void onLoaded(final ResourceCollection<E> resources) {
                reset(resources);
                ResourceScreen.this.loading.setVisible(false);
            }
        });
        // TODO should be set be onChange and onError. maybe make an application
        // wide notification widget
        // this.loading.setVisible(false);
        reset(resources);
    }

    protected void reset(final ResourceCollection<E> resources) {
        this.displayAll.reset(resources);
        this.actions.reset();
        this.header.setVisible(false);
        this.display.setVisible(false);
        this.loading.setVisible(false);
        setVisible(true);
    }

    @Override
    public void showEdit(final String key) {
        this.display.setReadOnly(false);
        show(key);
    }

    @Override
    public void showNew() {
        this.display.setReadOnly(false);
        this.displayAll.setVisible(false);
        reset(this.factory.newResource());
        this.loading.setVisible(false);
    }

    @Override
    public void showRead(final String key) {
        this.display.setReadOnly(true);
        show(key);
        this.actions.setVisible(false);
    }

    protected void showSingleton() {
        if (this.session.isAllowed(Action.UPDATE, this.factory.storageName())
                || this.session.isAllowed(Action.SHOW,
                                          this.factory.storagePluralName())) {

            this.display.setReadOnly(!this.session.isAllowed(Action.UPDATE,
                                                             this.factory.storagePluralName()));

            this.loading.setVisible(true);
            final E resource = this.factory.get(this.resourceChangeListener);
            reset(resource);
        }
        else {
            this.loading.setVisible(false);
            this.display.setVisible(false);
        }
    }

    ResourceChangeListener<E> resourceChangeListener = new ResourceChangeListener<E>() {

                                                         @Override
                                                         public void onChange(
                                                                 final E resource,
                                                                 final String message) {
                                                             reset(resource);
                                                             ResourceScreen.this.loading.setVisible(false);
                                                             // ResourceScreen.this.notifications.info("loaded: "
                                                             // +
                                                             // resource.display());

                                                         }

                                                         @Override
                                                         public void onError(
                                                                 final E resource,
                                                                 final int status,
                                                                 final String statusText) {
                                                             ResourceScreen.this.loading.setVisible(false);
                                                             // ResourceScreen.this.notifications.warn(status
                                                             // + ": "
                                                             // + statusText
                                                             // + ": "
                                                             // +
                                                             // resource.display());
                                                             reset(resource);
                                                         }
                                                     };

    protected void show(final String key) {
        this.loading.setVisible(true);
        final E resource = this.factory.get(key, this.resourceChangeListener);
        reset(resource);
    }
}