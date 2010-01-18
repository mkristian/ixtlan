/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.ComplexPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;

public class NoSearchResourceActionPanel<E extends Resource<E>> extends
        DefaultResourceActionPanel<E> {

    public NoSearchResourceActionPanel(final GetTextController getText,
            final ResourceBindings<E> bindings, final Session session,
            final ResourceFactory<E> factory,
            final ResourceNotifications changeNotification) {
        super(getText, bindings, session, factory, changeNotification);
    }

    @Override
    protected ComplexPanel createSearchPanel() {
        return null;
    }

}