/**
 *
 */
package de.saumya.gwt.translation.gui.client.views.users;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.session.client.models.Domain;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.Group;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserGroup;
import de.saumya.gwt.session.client.models.UserGroupFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceCollectionResetable;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;
import de.saumya.gwt.translation.gui.client.bindings.ListBoxBinding;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class UserFields extends ResourceFields<User> {

    public UserFields(final GetTextController getTextController,
            final ResourceBindings<User> bindings,
            final GroupFactory groupFactory,
            final UserGroupFactory userGroupFactory,
            final LocaleFactory localeFactory,
            final DomainFactory domainFactory, final Session session) {
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
                super.reset(localeFactory.realLocales());
            }

            @Override
            public void pullFrom(final User resource) {
                setEnabled(isEnabled() && getItemCount() > 0);
                select(resource.preferedLanguage);
            }

            @Override
            public void pushInto(final User resource) {
                resource.preferedLanguage = getResource();
            }
        };
        add("preferred language", preferredLanguage);
        final GroupLocaleDomainWidget groupsWidget = new GroupLocaleDomainWidget(session,
                localeFactory,
                domainFactory);

        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                if (session.getUser().isRoot()) {
                    userGroupFactory.all(new ResourcesChangeListener<UserGroup>() {

                        @Override
                        public void onLoaded(
                                final ResourceCollection<UserGroup> resources) {
                            groupsWidget.reset(resources);
                        }
                    });
                }
                else {
                    groupsWidget.reset(session.getUser().groups);
                }
            }
        });
        add("groups", groupsWidget);
        localeFactory.realLocales(new ResourcesChangeListener<Locale>() {

            @Override
            public void onLoaded(final ResourceCollection<Locale> resources) {
                preferredLanguage.reset(resources);
                groupsWidget.resetLocales(resources);
            }
        });
        domainFactory.realDomains(new ResourcesChangeListener<Domain>() {

            @Override
            public void onLoaded(final ResourceCollection<Domain> resources) {
                groupsWidget.resetDomains(resources);
            }
        });
        groupFactory.all(new ResourcesChangeListener<Group>() {

            @Override
            public void onLoaded(final ResourceCollection<Group> resources) {
                if (session.hasUser()) {
                    if (session.getUser().isRoot()) {
                        userGroupFactory.all(new ResourcesChangeListener<UserGroup>() {

                            @Override
                            public void onLoaded(
                                    final ResourceCollection<UserGroup> resources) {
                                groupsWidget.reset(resources);
                            }
                        });
                    }
                    else {
                        groupsWidget.reset(session.getUser().groups);
                    }
                }
            }

        });
    }

    static class GroupLocaleDomainWidget extends VerticalPanel implements
            Binding<User>, ResourceCollectionResetable<UserGroup> {

        private final Map<Integer, GroupWidget> map = new HashMap<Integer, GroupWidget>();

        private User                            user;

        private final Session                   session;

        private boolean                         isEnabled;

        private final LocaleFactory             localeFactory;
        private final DomainFactory             domainFactory;

        GroupLocaleDomainWidget(final Session session,
                final LocaleFactory localeFactory,
                final DomainFactory domainFactory) {
            this.session = session;
            this.localeFactory = localeFactory;
            this.domainFactory = domainFactory;
        }

        @Override
        public void pullFrom(final User resource) {
            this.user = resource;
            for (final GroupWidget widget : this.map.values()) {
                widget.checkbox.setValue(false);
                // unset all locales in list
                for (int i = 0; i < widget.localeList.getItemCount(); i++) {
                    widget.localeList.setItemSelected(i, false);
                }
                // unset all domains in list
                for (int i = 0; i < widget.domainList.getItemCount(); i++) {
                    widget.domainList.setItemSelected(i, false);
                }
            }
            if (resource != null && resource.groups != null) {
                for (final UserGroup group : resource.groups) {
                    final GroupWidget widget = this.map.get(group.id);
                    if (widget != null) {
                        widget.checkbox.setValue(true);
                        // reset locales list
                        for (int i = 0; i < widget.localeList.getItemCount(); i++) {
                            final boolean selected = group.locales.contains(widget.localeMap.get(widget.localeList.getValue(i)));
                            widget.localeList.setItemSelected(i, selected);
                        }
                        // reset domains list
                        for (int i = 0; i < widget.domainList.getItemCount(); i++) {
                            final boolean selected = group.domains.contains(widget.domainMap.get(widget.domainList.getValue(i)));
                            widget.domainList.setItemSelected(i, selected);
                        }
                    }
                }
            }
        }

        @Override
        public void pushInto(final User resource) {
            resource.groups.clear();
            for (final GroupWidget widget : this.map.values()) {
                if (widget.checkbox.getValue()) {
                    resource.groups.add(widget.group);
                    // set locales list in user's group
                    for (int i = 0; i < widget.localeList.getItemCount(); i++) {
                        if (widget.localeList.isItemSelected(i)) {
                            widget.group.locales.add(widget.getLocale(i));
                        }
                        else {
                            widget.group.locales.remove(widget.getLocale(i));
                        }
                    }
                    // set domains list in user's group
                    for (int i = 0; i < widget.domainList.getItemCount(); i++) {
                        if (widget.domainList.isItemSelected(i)) {
                            widget.group.domains.add(widget.getDomain(i));
                        }
                        else {
                            widget.group.domains.remove(widget.getDomain(i));
                        }
                    }
                }
            }
        }

        @Override
        public void reset(final ResourceCollection<UserGroup> resources) {
            clear();
            this.map.clear();
            for (final UserGroup resource : resources) {
                // allowed locales
                final Collection<Locale> locales;
                if (this.session.getUser().isRoot()
                        || this.session.getUser().isLocalesAdmin()) {
                    locales = this.localeFactory.realLocales();
                }
                else {
                    locales = resource.locales;
                }
                // allowed domains
                final Collection<Domain> domains;
                if (this.session.getUser().isRoot()
                        || this.session.getUser().isDomainsAdmin()) {
                    domains = this.domainFactory.realDomains();
                }
                else {
                    domains = resource.domains;
                }
                // add new group widget with allowed locales/domains
                final GroupWidget widget = new GroupWidget(resource,
                        locales,
                        domains);
                this.map.put(resource.id, widget);
                add(widget);
            }
            pullFrom(this.user);
            setEnabled(this.isEnabled);
        }

        private void resetLocales(final Collection<Locale> resources) {
            if (this.session.hasUser()
                    && (this.session.getUser().isRoot() || this.session.getUser()
                            .isLocalesAdmin())) {
                for (final GroupWidget widget : this.map.values()) {
                    widget.resetLocales(resources);
                }
            }
        }

        private void resetDomains(final Collection<Domain> resources) {
            if (this.session.hasUser()
                    && (this.session.getUser().isRoot() || this.session.getUser()
                            .isDomainsAdmin())) {
                for (final GroupWidget widget : this.map.values()) {
                    widget.resetDomains(resources);
                }
            }
        }

        static class GroupWidget extends FlowPanel {
            final UserGroup     group;
            final CheckBox      checkbox;
            final ListBox       localeList;
            Map<String, Locale> localeMap = new HashMap<String, Locale>();
            final ListBox       domainList;
            Map<String, Domain> domainMap = new HashMap<String, Domain>();

            GroupWidget(final UserGroup group,
                    final Collection<Locale> locales,
                    final Collection<Domain> domains) {
                this.group = group;
                this.checkbox = new CheckBox(group.name);
                add(this.checkbox);

                this.localeList = new ListBox(true);
                this.domainList = new ListBox(true);
                if (!group.isRoot()) {
                    add(this.localeList);
                    add(this.domainList);
                }
                resetLocales(locales);
                resetDomains(domains);
            }

            private Locale getLocale(final int index) {
                return this.localeMap.get(this.localeList.getValue(index));
            }

            private Domain getDomain(final int index) {
                return this.domainMap.get(this.domainList.getValue(index));
            }

            private void resetLocales(final Collection<Locale> locales) {
                this.localeList.clear();
                if (locales != null) {
                    for (final Locale locale : locales) {
                        this.localeMap.put(locale.id + "", locale);
                        this.localeList.addItem(locale.code, locale.id + "");
                    }
                    this.localeList.setVisible(this.localeList.getItemCount() != 0);
                }
                else {
                    this.localeList.setVisible(false);
                }
            }

            private void resetDomains(final Collection<Domain> domains) {
                this.domainList.clear();
                if (domains != null) {
                    for (final Domain domain : domains) {
                        this.domainMap.put(domain.id + "", domain);
                        this.domainList.addItem(domain.name, domain.id + "");
                    }
                    this.domainList.setVisible(this.domainList.getItemCount() != 0);
                }
                else {
                    this.domainList.setVisible(false);
                }
            }

            private void setEnabled(final boolean isEnabled) {
                this.checkbox.setEnabled(isEnabled);
                this.localeList.setEnabled(isEnabled);
                this.domainList.setEnabled(isEnabled);
            }
        }

        @Override
        public void setEnabled(final boolean isEnabled) {
            this.isEnabled = isEnabled;
            for (final GroupWidget widget : this.map.values()) {
                widget.setEnabled(isEnabled);
            }
        }
    }
}
