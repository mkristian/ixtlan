/**
 * 
 */
package com.example.client.views.<%= plural_name %>;

import com.example.client.models.<%= class_name %>;
import com.example.client.models.<%= class_name %>Factory;

import de.saumya.gwt.session.client.Notifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.DefaultResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

public class <%= class_name %>Screen extends ResourceScreen<<%= class_name %>> {

    public <%= class_name %>Screen(final GetTextController getTextController,
            final <%= class_name %>Factory factory, final Session session,
            final ResourceBindings<<%= class_name %>> bindings,
            final Notifications notifications) {
        super(getTextController,
                factory,
                session,
                new <%= class_name %>Panel(getTextController, bindings),
                new ResourceCollectionPanel<<%= class_name %>>(session, factory),
                new DefaultResourceActionPanel<<%= class_name %>>(getTextController,
                        bindings,
                        session,
                        factory));
    }

    @Override
    protected void reset(final <%= class_name %> resource) {
        reset(resource, <% if options[:skip_timestamps] %>null<% else %>resource.updatedAt<% end -%>, <% if options[:skip_modified_by] %>null<% else %>resource.updatedBy<% end -%>);
    }

    @Override
    public Screen<?> child(final String parentKey) {
        return null;
    }

}