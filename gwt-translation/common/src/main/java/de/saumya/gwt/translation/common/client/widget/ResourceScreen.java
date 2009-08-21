/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.sql.Timestamp;

import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.datamapper.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public abstract class ResourceScreen<E extends Resource<E>> extends
        VerticalPanel implements Screen<E> {

    private final ResourceHeaderPanel          header;

    private final PathFactory                  pathFactory;

    private final ResourceFactory<E>           factory;

    protected final ResourceActionPanel<E>     actions;

    protected final ResourcePanel<E>           display;

    protected final ResourceCollectionPanel<E> displayAll;

    protected final TranslatableLabel          loading;

    private PathFactory                        parentPathFactory;

    protected E                                resource;

    protected String                           locale;

    protected ResourceScreen(final GetTextController getText,
            final ResourceFactory<E> factory, final Session session,
            final ResourcePanel<E> display,
            final ResourceCollectionPanel<E> displayAll) {
        this(getText, factory, session, display, displayAll, null);
    }

    protected ResourceScreen(final GetTextController getText,
            final ResourceFactory<E> factory, final Session session,
            final ResourcePanel<E> display,
            final ResourceCollectionPanel<E> displayAll,
            final ResourceActionPanel<E> actions) {
        this.loading = new TranslatableLabel("loading", getText);
        this.header = new ResourceHeaderPanel(getText);
        this.actions = actions == null ? new ResourceActionPanel<E>(getText,
                session,
                factory) : actions;
        this.display = display;
        this.displayAll = displayAll;
        this.factory = factory;

        add(this.loading);
        add(this.actions);
        add(this.header);
        add(this.display);

        this.pathFactory = new PathFactory(factory.storageName());
        this.parentPathFactory = this.pathFactory;
    }

    @Override
    public PathFactory getPathFactory() {
        return this.parentPathFactory;
    }

    @Override
    public void setupPathFactory(final PathFactory parentPathFactory,
            final String key) {
        this.parentPathFactory = parentPathFactory == null
                ? this.pathFactory
                : this.pathFactory.newPathFactory(parentPathFactory.showPath(key));
    }

    abstract protected void reset(final E resource);

    protected final void reset(final E resource, final Timestamp updatedAt,
            final User updatedBy) {
        this.header.reset(resource.key(), updatedAt, updatedBy);
        this.actions.reset(resource, this.locale);
        this.display.reset(resource);
        this.header.setVisible(true);
        this.actions.setVisible(true);
        this.display.setVisible(true);
        setVisible(true);
    }

    @Override
    public void showAll() {
        this.actions.reset(this.pathFactory);
        final Resources<E> resources = this.factory.all(new ResourcesChangeListener<E>() {

            @Override
            public void onChange(final Resources<E> resources, final E resource) {
            }

            @Override
            public void onLoaded(final Resources<E> resources) {
                reset(resources);
                ResourceScreen.this.loading.setVisible(false);
            }
        });
        // TODO should be set be onChange and onError. maybe make an application
        // wide notification widget
        this.loading.setVisible(false);
        reset(resources);
    }

    private void reset(final Resources<E> resources) {
        this.displayAll.reset(resources,
                              this.pathFactory,
                              this.factory.storageName());
        this.header.setVisible(false);
        this.actions.setVisible(true);
        this.display.setVisible(false);
        setVisible(true);
    }

    @Override
    public void showEdit(final String key) {
        setReadOnly(false);
        show(key);
    }

    @Override
    public void showNew() {
        reset(this.factory.newResource());
        this.loading.setVisible(false);
    }

    @Override
    public void showRead(final String key) {
        setReadOnly(true);
        show(key);
    }

    protected void show(final String key) {
        this.loading.setVisible(true);
        this.display.setVisible(true);
        final E resource = this.factory.get(key,
                                            new ResourceChangeListener<E>() {

                                                @Override
                                                public void onChange(
                                                        final E resource) {
                                                    reset(resource);
                                                    ResourceScreen.this.loading.setVisible(false);
                                                }
                                            });
        // TODO should be set be onChange and onError. maybe make an application
        // wide notification widget
        this.loading.setVisible(false);
        reset(resource);
    }

    private void setReadOnly(final boolean isReadOnly) {
        this.display.setReadOnly(isReadOnly);
    }
}