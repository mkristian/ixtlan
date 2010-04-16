/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.sql.Timestamp;

import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.translation.common.client.route.HasPathFactory;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public abstract class AbstractResourceScreen<E extends Resource<E>> extends
        FlowPanel implements Screen<E>, ResourceResetable<E> {

    protected static final int                     RESOURCE = 0;

    protected final ResourceNotifications          notifications;

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

    protected final DeckPanel                      deck;
    protected final ResourceChangeListener<E>      resourceChangeListener;

    protected <ResourceWidget extends Widget & AllowReadOnly<E>> AbstractResourceScreen(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory, final Session session,
            final ResourceWidget display, final Widget displayCollection,
            final ResourceCollectionResetable<E> displayCollectionResetable,
            final HasPathFactory displayCollectionPathFactory,
            final AbstractResourceActionPanel<E> actions,
            final ResourceNotifications notifications,
            final HyperlinkFactory hyperlinkFactory) {
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
                AbstractResourceScreen.this.loading.setVisible(false);
            }
        };

        if (displayCollection != null) {
            this.pathFactory = hyperlinkFactory.newPathFactory(factory.storagePluralName());
        }
        else {
            this.pathFactory = hyperlinkFactory.newPathFactory(factory.storageName());
        }
        this.parentPathFactory = this.pathFactory;
    }

    @Override
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
     * {@link AbstractResourceScreen#reset(Resource, Timestamp, User)} using
     * null where the info does not exists
     * 
     * @param resource
     */
    @Override
    public final void reset(final E resource) {
        this.actions.reset(resource);
        this.actions.setReadOnly(this.displayAllowReadOnly.isReadOnly());

        this.displayAllowReadOnly.reset(resource);

        this.deck.showWidget(RESOURCE);
        this.loading.setVisible(false);
        setVisible(true);
    }

    // protected void show(final String key) {
    // final E resource = this.factory.get(key,
    // this.resourceChangeListener,
    // this.notifications);
    // reset(resource);
    // this.loading.setVisible(true);
    // }

    @Override
    public Screen<?> child(final String parentKey) {
        // that is the default !!!
        return null;
    }

}