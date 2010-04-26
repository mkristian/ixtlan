/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.groups;

import de.saumya.gwt.session.client.models.Group;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class GroupFields extends ResourceFields<Group> {

    public GroupFields(final GetTextController getTextController,
            final ResourceBindings<Group> bindings) {
        super(getTextController, bindings);
        add("name", new TextBoxBinding<Group>() {

            @Override
            public void pullFrom(final Group resource) {
                setText(resource.name);
            }

            @Override
            public void pushInto(final Group resource) {
                resource.name = getText();
            }
        }, true, 64);
    }
}
