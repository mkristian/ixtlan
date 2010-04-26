/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.locales;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
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

public class LocaleScreen extends ResourceScreen<Locale> {

    private static class LocaleHeaders extends ResourceHeaderPanel<Locale> {

        public LocaleHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        public void reset(final Locale resource) {
            reset(resource, resource.createdAt, null);
        }
    }

    public LocaleScreen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final LocaleFactory factory, final Session session,
            final ResourceBindings<Locale> bindings,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                new ResourcePanel<Locale>(new LocaleHeaders(getTextController),
                        new LocaleFields(getTextController, bindings)),
                new ResourceCollectionPanel<Locale>(loadingNotice,
                        new ResourceCollectionNavigation<Locale>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<Locale>(session,
                                factory,
                                getTextController,
                                hyperlinkFactory)),
                new ResourceActionPanel<Locale>(getTextController,
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
