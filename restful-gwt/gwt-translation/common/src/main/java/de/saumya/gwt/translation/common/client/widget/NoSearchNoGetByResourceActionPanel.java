/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.ComplexPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;

public class NoSearchNoGetByResourceActionPanel<E extends Resource<E>> extends
        DefaultResourceActionPanel<E> {

    public NoSearchNoGetByResourceActionPanel(final GetTextController getText,
            final ResourceBindings<E> bindings, final Session session,
            final ResourceFactory<E> factory) {
        super(getText, bindings, session, factory);
    }

    @Override
    protected ComplexPanel createSearchPanel() {
        return null;
    }

    @Override
    protected ComplexPanel createGetByPanel() {
        return null;
    }

}