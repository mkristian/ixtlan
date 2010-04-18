/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.users;

import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.models.Group;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.ListBoxBinding;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class UserFields extends ResourceFields<User> {

    public UserFields(final GetTextController getTextController,
            final ResourceBindings<User> bindings, final GroupFactory factory) {
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
        final ListBoxBinding<User, Group> groups = new ListBoxBinding<User, Group>(true) {

            @Override
            public void pullFrom(final User resource) {
                selectAll(resource.groups);
            }

            @Override
            public void pushInto(final User resource) {
                resource.groups = getResources(factory);
            }
        };
        // TODO restrict groups to groups of current user
        factory.all(new ResourcesChangeListener<Group>() {

            @Override
            public void onLoaded(final ResourceCollection<Group> resources) {
                groups.reset(resources);
            }
        });
        add("groups", groups);
    }
}