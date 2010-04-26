/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.domains;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.Domain;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.HyperlinkFactory;
import de.saumya.gwt.translation.common.client.widget.LoadingNotice;
import de.saumya.gwt.translation.common.client.widget.NotificationListeners;
import de.saumya.gwt.translation.common.client.widget.ResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionListing;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionNavigation;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceHeaderPanel;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

public class DomainScreen extends ResourceScreen<Domain> {

    private static class DomainHeaders extends ResourceHeaderPanel<Domain> {

        public DomainHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        public void reset(final Domain resource) {
            reset(resource, resource.createdAt, null);
        }
    }

    public DomainScreen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final DomainFactory factory, final Session session,
            final ResourceBindings<Domain> bindings,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                new ResourcePanel<Domain>(new DomainHeaders(getTextController),
                        new DomainFields(getTextController, bindings)),
                new ResourceCollectionPanel<Domain>(loadingNotice,
                        new ResourceCollectionNavigation<Domain>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<Domain>(session,
                                factory,
                                getTextController,
                                hyperlinkFactory)),
                new ResourceActionPanel<Domain>(getTextController,
                        bindings,
                        session,
                        factory,
                        listeners,
                        hyperlinkFactory,
                        true,
                        false),
                listeners,
                hyperlinkFactory);
    }

}
