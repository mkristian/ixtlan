/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public class ResourceScreen<E extends Resource<E>> extends
        AbstractResourceScreen<E> implements ResourceCollectionResetable<E> {

    // protected final ResourceNotifications notifications;
    //
    // protected final ResourceFactory<E> factory;
    //
    // protected final AbstractResourceActionPanel<E> actions;
    //
    // protected final Widget display;
    // protected final AllowReadOnly<E> displayAllowReadOnly;
    //
    // protected final Widget displayCollection;
    // protected final ResourceCollectionResetable<E>
    // displayCollectionResetable;
    // protected final HasPathFactory displayCollectionPathFactory;
    //
    // protected final Session session;
    //
    // protected final Widget loading;
    //
    // private PathFactory parentPathFactory;
    // private final PathFactory pathFactory;
    //
    // private final DeckPanel deck;
    // protected final ResourceChangeListener<E> resourceChangeListener;

    protected <S extends Widget & AllowReadOnly<E>, T extends Widget & ResourceCollectionResetable<E> & HasPathFactory> ResourceScreen(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory, final Session session,
            final S display, final T displayCollection,
            final AbstractResourceActionPanel<E> actions,
            final ResourceNotifications notifications,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                display,
                displayCollection,
                displayCollection,
                displayCollection,
                actions,
                notifications,
                hyperlinkFactory);
    }

    // private <ResourceWidget extends Widget & AllowReadOnly<E>>
    // ResourceScreen(
    // final LoadingNotice loadingNotice,
    // final ResourceFactory<E> factory, final Session session,
    // final ResourceWidget display, final Widget displayCollection,
    // final ResourceCollectionResetable<E> displayCollectionResetable,
    // final HasPathFactory displayCollectionPathFactory,
    // final AbstractResourceActionPanel<E> actions,
    // final ResourceNotifications notifications,
    // final HyperlinkFactory hyperlinkFactory) {
    // setStyleName("resource-screen");
    // this.loading = loadingNotice;
    //
    // this.actions = actions;
    //
    // // TODO from a javascript point of view casting might be better then
    // // multiple references to the same object
    // this.display = display;
    // this.displayAllowReadOnly = display;
    //
    // // TODO from a javascript point of view casting might be better then
    // // multiple references to the same object
    // this.displayCollection = displayCollection;
    // this.displayCollectionResetable = displayCollectionResetable;
    // this.displayCollectionPathFactory = displayCollectionPathFactory;
    //
    // this.factory = factory;
    // this.session = session;
    // this.notifications = notifications;
    //
    // this.deck = new DeckPanel();
    // this.deck.add(display);
    // if (displayCollection != null) {
    // this.deck.add(displayCollection);
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
    // if (displayCollection != null) {
    // this.pathFactory =
    // hyperlinkFactory.newPathFactory(factory.storagePluralName());
    // }
    // else {
    // this.pathFactory =
    // hyperlinkFactory.newPathFactory(factory.storageName());
    // }
    // this.parentPathFactory = this.pathFactory;
    // }
    //
    // @Override
    // public PathFactory getPathFactory() {
    // return this.parentPathFactory;
    // }
    //
    // @Override
    // public void setPathFactory(final PathFactory pathFactory) {
    // this.parentPathFactory = pathFactory;
    // this.actions.setPathFactory(getPathFactory());
    // if (this.displayCollectionPathFactory != null) {
    // this.displayCollectionPathFactory.setPathFactory(getPathFactory());
    // }
    //
    // }
    //
    // @Override
    // public void setupPathFactory(final String parentPath) {
    // setPathFactory(parentPath == null
    // ? this.pathFactory
    // : this.pathFactory.newPathFactory(parentPath));
    // }

    // /**
    // * only the resource knows whether it has updated Timestamp and/or
    // updatedBy
    // * User. an implementation needs to forward the respective info to the
    // * {@link ResourceScreen#reset(Resource, Timestamp, User)} using null
    // where
    // * the info does not exists
    // *
    // * @param resource
    // */
    // @Override
    // public final void reset(final E resource) {
    // this.actions.reset(resource);
    // this.actions.setReadOnly(this.displayAllowReadOnly.isReadOnly());
    //
    // this.displayAllowReadOnly.reset(resource);
    //
    // this.deck.showWidget(RESOURCE);
    // this.loading.setVisible(false);
    // setVisible(true);
    // }

    @Override
    public void reset(final ResourceCollection<E> resources) {
        this.displayCollectionResetable.reset(resources);
        this.actions.reset(resources);
        this.deck.showWidget(RESOURCES);
        this.loading.setVisible(false);
        setVisible(true);
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
        final E resource = this.factory.get(key,
                                            this.resourceChangeListener,
                                            this.notifications);
        reset(resource);
        this.loading.setVisible(true);
    }

    @Override
    public void showAll(final Map<String, String> query) {
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
        this.loading.setVisible(true);
    }

    @Override
    public Screen<?> child(final String parentKey) {
        // that is the default !!!
        return null;
    }

}