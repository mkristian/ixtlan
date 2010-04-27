/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.users;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroupFactory;
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

public class UserScreen extends ResourceScreen<User> {

    private static class UserHeaders extends ResourceHeaderPanel<User> {

        public UserHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        public void reset(final User resource) {
            reset(resource, resource.updatedAt, null);
        }
    }

    public UserScreen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final UserFactory factory, final UserGroupFactory userGroupFactory,
            final GroupFactory groupFactory, final LocaleFactory localeFactory,
            final DomainFactory domainFactory, final Session session,
            final ResourceBindings<User> bindings,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                new ResourcePanel<User>(new UserHeaders(getTextController),
                        new UserFields(getTextController,
                                bindings,
                                groupFactory,
                                userGroupFactory,
                                localeFactory,
                                domainFactory,
                                session)),
                new ResourceCollectionPanel<User>(loadingNotice,
                        new ResourceCollectionNavigation<User>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<User>(session,
                                factory,
                                getTextController,
                                hyperlinkFactory)),
                new ResourceActionPanel<User>(getTextController,
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
