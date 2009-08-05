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
                    GWT.log("session timeout "
                            + Session.this.authentication.user.login, null);
                    fireSessionTimeout();
                }
            }
            else {
                this.countDown = this.timeout * 6 - 1;
                this.idle = true;
            }
        }
    }

    private Authentication                                   authentication = null;
    private final Timer                                      timer          = new SessionTimer();
    private final AuthenticationFactory                      authenticationFactory;
    private final Map<String, Map<String, Collection<Role>>> permissions;

    public Session(final AuthenticationFactory authenticationFactory,
            final PermissionFactory permissionFactory) {
        this.authenticationFactory = authenticationFactory;
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

            @Override
            public void onLoaded(final Resources<Permission> resources) {
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

    private void doLogin(final Authentication authentication) {
        this.authentication = authentication;
        this.timer.scheduleRepeating(10000);
        GWT.log("login of " + authentication.user.login, null);
        fireSuccessfulLogin();
    }

    private void doAccessDenied() {
        this.authentication = null;
        fireAccessDenied();
    }

    void login(final String username, final String password) {
        if ("mudita".equals(password)) {
            this.authenticationFactory.all(new ResourcesChangeListener<Authentication>() {

                @Override
                public void onChange(final Resources<Authentication> resources,
                        final Authentication resource) {
                    System.out.println(resource);
                    if (resource.user.login.equals(username)) {
                        doLogin(resource);
                    }
                }

                @Override
                public void onLoaded(final Resources<Authentication> resources) {
                    if (!hasUser()) {
                        doAccessDenied();
                    }
                }
            });
        }
        else {
            doAccessDenied();
        }
    }

    void logout() {
        this.timer.cancel();
        GWT.log("log out " + this.authentication.user.login, null);
        this.authentication.destroy();
        this.authentication = null;
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
        if (this.authentication != null) {
            final Map<String, Collection<Role>> permission = this.permissions.get(factory.storageName());
            if (permission != null) {
                if (permission.containsKey(action)) {
                    for (final Role r : permission.get(action)) {
                        if (r.name.equals(role)) {
                            return r;
                        }
                    }
                }
            }
        }
        return null;
    }

    public User getUser() {
        return this.authentication != null ? this.authentication.user : null;
    }

    public boolean hasUser() {
        return this.authentication != null;
    }

    public String sessionToken() {
        return this.authentication != null ? this.authentication.token : null;
    }
}
