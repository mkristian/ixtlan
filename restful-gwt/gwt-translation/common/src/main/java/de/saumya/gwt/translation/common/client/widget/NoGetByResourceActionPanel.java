/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.ComplexPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;

public class NoGetByResourceActionPanel<E extends Resource<E>> extends
        DefaultResourceActionPanel<E> {

    public NoGetByResourceActionPanel(final GetTextController getText,
            final ResourceBindings<E> bindings, final Session session,
            final ResourceFactory<E> factory,
            final ResourceNotifications notifications,
            final HyperlinkFactory hyperlinkFactory) {
        super(getText,
                bindings,
                session,
                factory,
                notifications,
                hyperlinkFactory);
    }

    @Override
    protected ComplexPanel createGetByPanel() {
        return null;
    }

}