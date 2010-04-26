/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.groups;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.Group;
import de.saumya.gwt.session.client.models.GroupFactory;
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

public class GroupScreen extends ResourceScreen<Group> {

    private static class GroupHeaders extends ResourceHeaderPanel<Group> {

        public GroupHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        public void reset(final Group resource) {
            reset(resource, resource.createdAt, null);
        }
    }

    public GroupScreen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final GroupFactory factory, final Session session,
            final ResourceBindings<Group> bindings,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
                new ResourcePanel<Group>(new GroupHeaders(getTextController),
                        new GroupFields(getTextController, bindings)),
                new ResourceCollectionPanel<Group>(loadingNotice,
                        new ResourceCollectionNavigation<Group>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<Group>(session,
                                factory,
                                getTextController,
                                hyperlinkFactory)),
                new ResourceActionPanel<Group>(getTextController,
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
