/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.users;

import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.models.Group;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.gui.client.bindings.ListBoxBinding;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class UserFields extends ResourceFields<User> {

    public UserFields(final GetTextController getTextController,
            final ResourceBindings<User> bindings,
            final GroupFactory groupFactory, final LocaleFactory localeFactory) {
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
        final ListBoxBinding<User, Locale> preferredLanguage = new ListBoxBinding<User, Locale>(false) {

            @Override
            public void reset(final ResourceCollection<Locale> locales) {
                final ResourceCollection<Locale> l = localeFactory.newResources();
                l.addAll(locales);
                l.remove(0);
                l.remove(0);
                super.reset(l);
            }

            @Override
            public void pullFrom(final User resource) {
                setEnabled(getItemCount() > 0);
                select(resource.preferedLanguage);
            }

            @Override
            public void pushInto(final User resource) {
                resource.preferedLanguage = getResource();
            }
        };
        localeFactory.all(new ResourcesChangeListener<Locale>() {

            @Override
            public void onLoaded(final ResourceCollection<Locale> resources) {
                preferredLanguage.reset(resources);
            }
        });
        add("preferred language", preferredLanguage);
        final ListBoxBinding<User, Group> groups = new ListBoxBinding<User, Group>(true) {

            @Override
            public void pullFrom(final User resource) {
                selectAll(resource.groups);
            }

            @Override
            public void pushInto(final User resource) {
                resource.groups = getResources(groupFactory);
            }
        };
        // TODO restrict groups to groups of current user
        groupFactory.all(new ResourcesChangeListener<Group>() {

            @Override
            public void onLoaded(final ResourceCollection<Group> resources) {
                groups.reset(resources);
            }
        });
        add("groups", groups);
    }
}