/**
 *
 */
package <%= package %>.views.<%= plural_name %>;

import <%= package %>.models.<%= class_name %>;
import <%= package %>.models.<%= class_name %>Factory;

import de.saumya.gwt.session.client.Session;
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

public class <%= class_name %>Screen extends ResourceScreen<<%= class_name %>> {

    private static class <%= class_name %>Headers extends ResourceHeaderPanel<<%= class_name %>> {

        public <%= class_name %>Headers(final GetTextController getTextController) {
            super(getTextController);
        }

        public void reset(final <%= class_name %> resource) {
            reset(resource, <% if options[:skip_timestamps] %>null<% else %>resource.updatedAt<% end -%>, <% if options[:skip_modified_by] %>null<% else %>resource.updatedBy<% end -%>);
        }
    }

    public <%= class_name %>Screen(final LoadingNotice loadingNotice,
            final GetTextController getTextController,
            final <%= class_name %>Factory factory, final Session session,
            final ResourceBindings<<%= class_name %>> bindings,
            final NotificationListeners listeners,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                factory,
                session,
               new ResourcePanel<<%= class_name %>>(new <%= class_name %>Headers(getTextController),
                       new <%= class_name %>Fields(getTextController, bindings)),
                new ResourceCollectionPanel<<%= class_name %>>(loadingNotice,
                        new ResourceCollectionNavigation<<%= class_name %>>(loadingNotice,
                                factory,
                                getTextController),
                        new ResourceCollectionListing<<%= class_name %>>(session,
                                factory,
                                getTextController,
                                hyperlinkFactory)),
                new ResourceActionPanel<<%= class_name %>>(getTextController,
                        bindings,
                        session,
                        factory,
                        listeners,
                        hyperlinkFactory,
                        true,
                        true),
                listeners,
                hyperlinkFactory);
    }

}
