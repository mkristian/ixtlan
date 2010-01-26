/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.sql.Timestamp;
import java.util.Map;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.session.client.model.User;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public class ResourceScreen<E extends Resource<E>> extends FlowPanel implements
        Screen<E>, ResourceResetable<E>, ResourceCollectionResetable<E> {

    private static final int                       RESOURCE            = 0;
    private static final int                       RESOURCE_COLLECTION = 1;

    private final ResourceNotifications            notifications;

    protected final ResourceFactory<E>             factory;

    protected final AbstractResourceActionPanel<E> actions;

    protected final Widget                         display;
    protected final AllowReadOnly<E>               displayAllowReadOnly;

    protected final Widget                         displayCollection;
    protected final ResourceCollectionResetable<E> displayCollectionResetable;
    protected final HasPathFactory                 displayCollectionPathFactory;

    protected final Session                        session;

    protected final Widget                         loading;

    private PathFactory                            parentPathFactory;
    private final PathFactory                      pathFactory;

    private final DeckPanel                        deck;
    private final ResourceChangeListener<E>        resourceChangeListener;

    protected <ResourceWidget extends Widget & AllowReadOnly<E>> ResourceScreen(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory, final Session session,
            final ResourceWidget display,
            final AbstractResourceActionPanel<E> actions,
            final ResourceNotifications notifications) {
        this(loadingNotice,
                factory,
                session,
                display,
                null,
                null,
                null,
                actions,
                notifications);
    }

    protected <S extends Widget & AllowReadOnly<E>, T extends Widget & ResourceCollectionResetable<E> & HasPathFactory> ResourceScreen(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory, final Session session,
            final S display, final T displayCollection,
            final AbstractResourceActionPanel<E> actions,
            final ResourceNotifications notifications) {
        this(loadingNotice,
                factory,
                session,
                display,
                displayCollection,
                displayCollection,
                displayCollection,
                actions,
                notifications);
    }

    private <ResourceWidget extends Widget & AllowReadOnly<E>> ResourceScreen(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory, final Session session,
            final ResourceWidget display, final Widget displayCollection,
            final ResourceCollectionResetable<E> displayCollectionResetable,
            final HasPathFactory displayCollectionPathFactory,
            final AbstractResourceActionPanel<E> actions,
            final ResourceNotifications notifications) {
        setStyleName("resource-screen");
        this.loading = loadingNotice;

        this.actions = actions;

        // TODO from a javascript point of view casting might be better then
        // multiple references to the same object
        this.display = display;
        this.displayAllowReadOnly = display;

        // TODO from a javascript point of view casting might be better then
        // multiple references to the same object
        this.displayCollection = displayCollection;
        this.displayCollectionResetable = displayCollectionResetable;
        this.displayCollectionPathFactory = displayCollectionPathFactory;

        this.factory = factory;
        this.session = session;
        this.notifications = notifications;

        this.deck = new DeckPanel();
        this.deck.add(display);
        if (displayCollection != null) {
            this.deck.add(displayCollection);
        }

        add(this.loading);
        add(this.actions);
        add(this.deck);

        this.resourceChangeListener = new ResourceChangeListener<E>() {

            @Override
            public void onChange(final E resource) {
                reset(resource);
            }

            @Override
            public void onError(final E resource) {
                reset(resource);
                ResourceScreen.this.loading.setVisible(false);
            }
        };

        if (displayCollection != null) {
            this.pathFactory = new PathFactory(factory.storagePluralName());
        }
        else {
            this.pathFactory = new PathFactory(factory.storageName());
        }
        this.parentPathFactory = this.pathFactory;
    }

    // protected ResourceScreen(final GetTextController getTextController,
    // final ResourceFactory<E> factory, final Session session,
    // final ResourceFields<E> displayFields,
    // final ResourceCollectionPanel<E> displayAll,
    // final AbstractResourceActionPanel<E> actions,
    // final ResourceNotifications notifications) {
    // setStyleName("screen");
    // this.loading = new TranslatableLabel(getTextController, "loading ...");
    // this.loading.setStyleName("loading");
    //
    // final ResourceHeaderPanel<E> header = new
    // ResourceHeaderPanel<E>(getTextController) {
    //
    // @Override
    // protected void reset(final E resource) {
    // // TODO Auto-generated method stub
    //
    // }
    //
    // };
    // this.actions = actions;
    // final ResourcePanel<E> display = new ResourcePanel<E>(header,
    // displayFields);
    //
    // this.display = display;
    // this.displayAllowReadOnly = display;
    //
    // this.displayAll = displayAll;
    // this.displayAllResetable = displayAll;
    // this.displayAllPathFactory = displayAll;
    //
    // this.factory = factory;
    // this.session = session;
    // this.notifications = notifications;
    //
    // this.deck = new DeckPanel();
    // this.deck.add(display);
    // if (displayAll != null) {
    // this.deck.add(displayAll);
    // }
    //
    // add(this.loading);
    // add(this.actions);
    // add(this.deck);
    //
    // this.resourceChangeListener = new ResourceChangeListener<E>() {
    //
    // @Override
    // public void onChange(final E resource) {
    // reset(resource);
    // }
    //
    // @Override
    // public void onError(final E resource) {
    // reset(resource);
    // ResourceScreen.this.loading.setVisible(false);
    // }
    // };
    //
    // if (displayAll != null) {
    // this.pathFactory = new PathFactory(factory.storagePluralName());
    // }
    // else {
    // this.pathFactory = new PathFactory(factory.storageName());
    // }
    // this.parentPathFactory = this.pathFactory;
    // }

    @Override
    // TODO make protected - seems not to be used outside class hierarchy!!!
    public PathFactory getPathFactory() {
        return this.parentPathFactory;
    }

    public void setPathFactory(final PathFactory pathFactory) {
        this.parentPathFactory = pathFactory;
        this.actions.setPathFactory(getPathFactory());
        if (this.displayCollectionPathFactory != null) {
            this.displayCollectionPathFactory.setPathFactory(getPathFactory());
        }

    }

    @Override
    public void setupPathFactory(final String parentPath) {
        setPathFactory(parentPath == null
                ? this.pathFactory
                : this.pathFactory.newPathFactory(parentPath));
    }

    /**
     * only the resource knows whether it has updated Timestamp and/or updatedBy
     * User. an implementation needs to forward the respective info to the
     * {@link ResourceScreen#reset(Resource, Timestamp, User)} using null where
     * the info does not exists
     * 
     * @param resource
     */
    // abstract protected void reset(final E resource);

    // @SuppressWarnings("unchecked")
    @Override
    public final void reset(final E resource) {
        // if (((ResourcePanel<E>) this.display).header != null) {
        // ((ResourcePanel<E>) this.display).header.reset(resource.isNew()
        // || resource.isDeleted() || !resource.isUptodate()
        // ? null
        // : resource.key(), updatedAt, updatedBy);
        // }
        this.actions.reset(resource);
        this.actions.setReadOnly(this.displayAllowReadOnly.isReadOnly());

        this.displayAllowReadOnly.reset(resource);

        this.deck.showWidget(RESOURCE);
        this.loading.setVisible(!resource.isUptodate());
        setVisible(true);
    }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        this.displayCollectionResetable.reset(resources);
        this.actions.reset(resources);
        this.deck.showWidget(RESOURCE_COLLECTION);
        this.loading.setVisible(false);
        setVisible(true);
    }

    // TODO make a SingletonResourceScreen see also ConfigurationScreen
    protected void showSingleton() {
        if (this.session.isAllowed(Action.UPDATE,
                                   this.factory.storagePluralName())
                || this.session.isAllowed(Action.SHOW,
                                          this.factory.storagePluralName())) {

            this.displayAllowReadOnly.setReadOnly(!this.session.isAllowed(Action.UPDATE,
                                                                          this.factory.storagePluralName()));
            this.loading.setVisible(true);
            final E resource = this.factory.get(this.resourceChangeListener,
                                                this.notifications);
            reset(resource);
        }
        else {
            this.loading.setVisible(false);
            this.display.setVisible(false);
        }
    }

    @Override
    public void showNew() {
        this.loading.setVisible(true);
        this.displayAllowReadOnly.setReadOnly(false);
        reset(this.factory.newResource());
    }

    @Override
    public void showRead(final String key) {
        this.displayAllowReadOnly.setReadOnly(true);
        show(key);
    }

    @Override
    public void showEdit(final String key) {
        this.displayAllowReadOnly.setReadOnly(false);
        show(key);
    }

    protected void show(final String key) {
        this.loading.setVisible(true);
        final E resource = this.factory.get(key,
                                            this.resourceChangeListener,
                                            this.notifications);
        reset(resource);
    }

    @Override
    public void showAll(final Map<String, String> query) {
        this.loading.setVisible(true);
        final ResourceCollection<E> resources = this.factory.all(query,
                                                                 new ResourcesChangeListener<E>() {

                                                                     @Override
                                                                     public void onLoaded(
                                                                             final ResourceCollection<E> resources) {
                                                                         reset(resources);
                                                                         ResourceScreen.this.loading.setVisible(false);
                                                                     }
                                                                 });
        // TODO should be set be onChange and onError. maybe make an application
        // wide notification widget
        reset(resources);
    }

    @Override
    public Screen<?> child(final String parentKey) {
        // TODO Auto-generated method stub
        return null;
    }

}