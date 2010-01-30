/**
 * 
 */
package de.saumya.gwt.translation.gui.client.views.users;

import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.model.User;
import de.saumya.gwt.session.client.model.UserFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.DefaultResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.LoadingNotice;
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
            final UserFactory factory, final Session session,
            final ResourceBindings<User> bindings,
            final ResourceNotifications notifications) {
        super(loadingNotice,
                factory,
                session,
                new ResourcePanel<User>(new UserHeaders(getTextController),
                        new UserFields(getTextController, bindings)),
                new ResourceCollectionPanel<User>(loadingNotice,
                        new ResourceCollectionNavigation<User>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<User>(session,
                                factory,
                                getTextController)),
                new DefaultResourceActionPanel<User>(getTextController,
                        bindings,
                        session,
                        factory,
                        notifications),
                notifications);
    }

}