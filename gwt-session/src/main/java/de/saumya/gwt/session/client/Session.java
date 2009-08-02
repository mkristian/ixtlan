package de.saumya.gwt.session.client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Event.NativePreviewEvent;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.datamapper.client.ResourcesChangeListener;

public class Session {

    class SessionTimer extends Timer {

        private final int timeout   = 1;
        private int       countDown = this.timeout;
        private boolean   idle      = true;

        public SessionTimer() {
            Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

                public void onPreviewNativeEvent(final NativePreviewEvent event) {
                    SessionTimer.this.idle = false;
                }
            });
        }

        @Override
        public void run() {
            GWT.log("idle " + this.idle, null);
            GWT.log("countDown " + this.countDown, null);
            if (this.idle) {
                this.countDown--;
                if (this.countDown == 0) {
                    cancel();
                    GWT.log("session timeout " + Session.this.user.login, null);
                    fireSessionTimeout();
                }
            }
            else {
                this.countDown = this.timeout * 6 - 1;
                this.idle = true;
            }
        }
    }

    private boolean                                          loggedIn = false;
    private String                                           token    = null;
    private User                                             user     = null;
    private final Timer                                      timer    = new SessionTimer();
    private final UserFactory                                userFactory;
    private final RoleFactory                                roleFactory;
    private final VenueFactory                               venueFactory;
    private final Map<String, Map<String, Collection<Role>>> permissions;

    public Session(final VenueFactory venueFactory,
            final PermissionFactory permissionFactory,
            final RoleFactory roleFactory, final UserFactory userFactory) {
        this.venueFactory = venueFactory;
        this.roleFactory = roleFactory;
        this.userFactory = userFactory;
        this.permissions = new HashMap<String, Map<String, Collection<Role>>>();
        permissionFactory.all(new ResourcesChangeListener<Permission>() {

            @Override
            public void onChange(final Resources<Permission> resources,
                    final Permission resource) {
                final Map<String, Collection<Role>> actions;
                if (Session.this.permissions.containsKey(resource.resourceName)) {
                    actions = Session.this.permissions.get(resource.resourceName);
                }
                else {
                    actions = new HashMap<String, Collection<Role>>();
                    Session.this.permissions.put(resource.resourceName, actions);
                }
                actions.put(resource.action, resource.roles);
                GWT.log("added permission for '" + resource.resourceName + "#"
                        + resource.action + ": " + resource.roles, null);
            }
        });
    }

    private final List<SessionListener> listeners = new ArrayList<SessionListener>();

    public void addSessionListern(final SessionListener listener) {
        this.listeners.add(listener);
    }

    public void removeSessionListern(final SessionListener listener) {
        this.listeners.remove(listener);
    }

    private void fireSessionTimeout() {
        for (final SessionListener listener : this.listeners) {
            listener.onSessionTimeout();
        }
    }

    private void fireAccessDenied() {
        for (final SessionListener listener : this.listeners) {
            listener.onAccessDenied();
        }
    }

    private void fireSuccessfulLogin() {
        for (final SessionListener listener : this.listeners) {
            listener.onSuccessfulLogin();
        }
    }

    private void fireLoggedOut() {
        for (final SessionListener listener : this.listeners) {
            listener.onLoggedOut();
        }
    }

    void login(final String username, final String password) {
        if ("dhamma".equals(username) && "mudita".equals(password)) {
            this.loggedIn = true;
            this.token = "blahblah";
            this.user = this.userFactory.newResource();
            this.user.name = "Dhamma";
            this.user.login = "dhamma";
            this.user.roles = this.roleFactory.newResources();
            final Role role = this.roleFactory.newResource();
            role.name = "root";
            this.user.roles.add(role);

            final Role vrole = this.roleFactory.newResource();
            vrole.name = "editor";
            final Venue dvara = this.venueFactory.newResource();
            dvara.id = "dvara";
            vrole.venues = this.venueFactory.newResources();
            vrole.venues.add(dvara);
            final Venue pajotta = this.venueFactory.newResource();
            dvara.id = "pajotta";
            vrole.venues.add(pajotta);
            this.user.roles.add(vrole);

            this.timer.scheduleRepeating(10000);
            GWT.log("login of " + username, null);
            fireSuccessfulLogin();
        }
        else {
            this.loggedIn = false;
            this.token = null;
            this.user = null;
            fireAccessDenied();
        }
    }

    void logout() {
        this.loggedIn = false;
        this.token = null;
        this.timer.cancel();
        GWT.log("log out " + this.user.login, null);
        fireLoggedOut();
    }

    public boolean isAllowed(final String action,
            final ResourceFactory<? extends Resource<?>> factory,
            final String role) {
        final Role r = findAllowedRole(action, factory, role);
        return r != null && r.locales == null && r.venues == null;
    }

    public boolean isAllowed(final String action,
            final ResourceFactory<? extends Resource<?>> factory,
            final String role, final Locale locale) {
        final Role r = findAllowedRole(action, factory, role);
        if (r != null && r.venues == null) {
            for (final Locale l : r.locales) {
                if (l.code.equals(locale.code)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isAllowed(final String action,
            final ResourceFactory<? extends Resource<?>> factory,
            final String role, final Venue venue) {
        final Role r = findAllowedRole(action, factory, role);
        if (r != null && r.locales == null) {
            for (final Venue v : r.venues) {
                if (v.id.equals(venue.id)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Role findAllowedRole(final String action,
            final ResourceFactory<? extends Resource<?>> factory,
            final String role) {
        // TODO make this real
        if (this.loggedIn) {
            final Map<String, Collection<Role>> permission = this.permissions.get(factory.storageName());
            if (permission != null) {
                for (final Role r : permission.get(action)) {
                    if (r.name.equals(role)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public User getUser() {
        return this.user;
    }

    public boolean hasUser() {
        return this.loggedIn;
    }

    public String sessionToken() {
        return this.token;
    }
}
