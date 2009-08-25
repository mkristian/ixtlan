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
import de.saumya.gwt.session.client.model.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public abstract class ResourceScreen<E extends Resource<E>> extends
        VerticalPanel implements Screen<E> {

    private final PathFactory                  pathFactory;

    private final ResourceFactory<E>           factory;

    protected final ResourceHeaderPanel        header;

    protected final ResourceActionPanel<E>     actions;

    protected final ResourcePanel<E>           display;

    protected final ResourceCollectionPanel<E> displayAll;

    protected final TranslatableLabel          loading;

    private PathFactory                        parentPathFactory;

    protected E                                resource;

    protected ResourceScreen(final GetTextController getText,
            final ResourceFactory<E> factory, final Session session,
            final ResourcePanel<E> display,
            final ResourceCollectionPanel<E> displayAll,
            final ResourceActionPanel<E> actions) {
        this.loading = new TranslatableLabel("loading", getText);
        this.header = new ResourceHeaderPanel(getText);
        this.actions = actions;
        this.display = display;
        this.displayAll = displayAll;
        this.factory = factory;

        add(this.loading);
        add(this.actions);
        add(this.header);
        add(this.display);
        add(this.displayAll);

        this.pathFactory = new PathFactory(factory.storageName());
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
        this.displayAll.setup(getPathFactory());
    }

    abstract protected void reset(final E resource);

    protected final void reset(final E resource, final Timestamp updatedAt,
            final User updatedBy) {
        if (!resource.isNew()) {
            this.header.reset(resource.key(), updatedAt, updatedBy);
        }
        this.actions.reset(resource);
        this.display.reset(resource);

        this.displayAll.setVisible(false);
        setVisible(true);
    }

    @Override
    public void showAll() {
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
        // this.loading.setVisible(false);
        reset(resources);
    }

    protected void reset(final Resources<E> resources) {
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

    protected void show(final String key) {
        this.loading.setVisible(true);
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
}