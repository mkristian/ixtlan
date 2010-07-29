/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.audits;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.Audit;
import de.saumya.gwt.session.client.models.AuditFactory;
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

public class AuditScreen extends ResourceScreen<Audit> {

    private static class AuditHeaders extends ResourceHeaderPanel<Audit> {

        public AuditHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        public void reset(final Audit resource) {
            reset(null, null);
        }
    }

    public AuditScreen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final AuditFactory factory, final Session session,
            final ResourceBindings<Audit> bindings,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                new ResourcePanel<Audit>(new AuditHeaders(getTextController),
                        new AuditFields(getTextController, bindings)),
                new ResourceCollectionPanel<Audit>(loadingNotice,
                        new ResourceCollectionNavigation<Audit>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<Audit>(session,
                                factory,
                                getTextController,
                                hyperlinkFactory)),
                new ResourceActionPanel<Audit>(getTextController,
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
