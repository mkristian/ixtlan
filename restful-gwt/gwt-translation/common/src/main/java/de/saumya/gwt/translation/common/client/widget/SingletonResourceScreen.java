/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.SingletonResource;
import de.saumya.gwt.persistence.client.SingletonResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;

public class SingletonResourceScreen<E extends SingletonResource<E>> extends
        AbstractResourceScreen<E> {

    private final SingletonResourceFactory<E> factory;

    protected <ResourceWidget extends Widget & AllowReadOnly<E>> SingletonResourceScreen(
            final LoadingNotice loadingNotice,
            final SingletonResourceFactory<E> factory, final Session session,
            final ResourceWidget display,
            final AbstractResourceActionPanel<E> actions,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                display,
                null,
                null,
                null,
                actions,
                listeners,
                hyperlinkFactory);
        this.factory = factory;
    }

    @Override
    public void showAll(final Map<String, String> query) {
        // singletons act on urls which look like a collection of resources =>
        // no showNew, showRead, showEdit. only showAll
        showSingleton();
    }

    protected void showSingleton() {
        this.displayAllowReadOnly.setReadOnly(!this.session.isAllowed(Action.UPDATE,
                                                                      this.factory.storagePluralName()));
        this.loading.setVisible(true);
        final E resource = this.factory.get(this.resourceChangeListener);
        reset(resource);
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("singleton resource has no 'new'");
    }

    @Override
    public void showRead(final int key) {
        throw new UnsupportedOperationException("singleton resource has no 'show'");
    }

    @Override
    public void showEdit(final int key) {
        throw new UnsupportedOperationException("singleton resource has no 'edit'");
    }

}