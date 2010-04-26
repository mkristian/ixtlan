/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.locales;

import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class LocaleFields extends ResourceFields<Locale> {

    public LocaleFields(final GetTextController getTextController,
            final ResourceBindings<Locale> bindings) {
        super(getTextController, bindings);
        add("code", new TextBoxBinding<Locale>() {

            @Override
            public void pullFrom(final Locale resource) {
                setText(resource.code);
            }

            @Override
            public void pushInto(final Locale resource) {
                resource.code = getText();
            }
        }, true, 64);
    }
}
