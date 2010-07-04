/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.audits;

import de.saumya.gwt.session.client.models.Audit;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class AuditFields extends ResourceFields<Audit> {

    public AuditFields(final GetTextController getTextController,
            final ResourceBindings<Audit> bindings) {
        super(getTextController, bindings);
        add("date", new TextBoxBinding<Audit>() {

            @Override
            public void pullFrom(final Audit resource) {
                setText(resource.date);
            }

            @Override
            public void pushInto(final Audit resource) {
                resource.date = getText();
            }
        }, true, 64);
        add("login", new TextBoxBinding<Audit>() {

            @Override
            public void pullFrom(final Audit resource) {
                setText(resource.login);
            }

            @Override
            public void pushInto(final Audit resource) {
                resource.login = getText();
            }
        }, true, 64);
        add("message", new TextBoxBinding<Audit>() {

            @Override
            public void pullFrom(final Audit resource) {
                setText(resource.message);
            }

            @Override
            public void pushInto(final Audit resource) {
                resource.message = getText();
            }
        }, true, 64);
    }
}
