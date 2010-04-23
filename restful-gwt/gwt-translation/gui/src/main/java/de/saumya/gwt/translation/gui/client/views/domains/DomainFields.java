/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.domains;

import de.saumya.gwt.session.client.models.Domain;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class DomainFields extends ResourceFields<Domain> {

    public DomainFields(final GetTextController getTextController,
            final ResourceBindings<Domain> bindings) {
        super(getTextController, bindings);
        add("name", new TextBoxBinding<Domain>() {

            @Override
            public void pullFrom(final Domain resource) {
                setText(resource.name);
            }

            @Override
            public void pushInto(final Domain resource) {
                resource.name = getText();
            }
        }, true, 64);
    }
}