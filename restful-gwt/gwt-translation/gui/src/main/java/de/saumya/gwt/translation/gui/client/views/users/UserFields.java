/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.users;

import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class UserFields extends ResourceFields<User> {

    public UserFields(final GetTextController getTextController,
            final ResourceBindings<User> bindings) {
        super(getTextController, bindings);
        add("login", new TextBoxBinding<User>() {

            @Override
            public void pullFrom(final User resource) {
                setText(resource.login);
            }

            @Override
            public void pushInto(final User resource) {
                resource.login = getText();
            }
        }, true, 64);
        add("name", new TextBoxBinding<User>() {

            @Override
            public void pullFrom(final User resource) {
                setText(resource.name);
            }

            @Override
            public void pushInto(final User resource) {
                resource.name = getText();
            }
        }, true, 64);
        add("email", new TextBoxBinding<User>() {

            @Override
            public void pullFrom(final User resource) {
                setText(resource.email);
            }

            @Override
            public void pushInto(final User resource) {
                resource.email = getText();
            }
        }, true, 64);
        // add("language", new TextBoxBinding<User>() {
        //
        // @Override
        // public void pullFrom(final User resource) {
        // setText(resource.preferedLanguage);
        // }
        //
        // @Override
        // public void pushInto(final User resource) {
        // resource.preferedLanguage = getText();
        // }
        // }, true, 64);
    }
}