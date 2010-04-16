/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.util.Map;

import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.Session.Action;

public class SingletonResourceScreen<E extends Resource<E>> extends
        AbstractResourceScreen<E> {
    protected <ResourceWidget extends Widget & AllowReadOnly<E>> SingletonResourceScreen(
            final LoadingNotice loadingNotice,
            final ResourceFactory<E> factory, final Session session,
            final ResourceWidget display,
            final AbstractResourceActionPanel<E> actions,
            final ResourceNotifications notifications,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                display,
                null,
                null,
                null,
                actions,
                notifications,
                hyperlinkFactory);
    }

    @Override
    public void showAll(final Map<String, String> query) {
        // singletons act on urls which look like a collection of resources =>
        // no showNew, showRead, showEdit. only showAll
        showSingleton();
    }

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
        throw new UnsupportedOperationException("singleton resource has no 'new'");
    }

    @Override
    public void showRead(final String key) {
        throw new UnsupportedOperationException("singleton resource has no 'show'");
    }

    @Override
    public void showEdit(final String key) {
        throw new UnsupportedOperationException("singleton resource has no 'edit'");
    }

}