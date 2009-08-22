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
            GWT.log("idle " + this.idle + ", countDown " + this.countDown, null);
            if (this.idle) {
                this.countDown--;
                if (this.countDown == 0) {
                    cancel();
                    GWT.log("session timeout "
                            + Session.this.authentication.user.login, null);
                    logout();
                    fireSessionTimeout();
                }
            }
            else {
                this.countDown = this.timeout * 6 - 1;
                this.idle = true;
            }
        }
    }

    private Authentication                                    authentication = null;
    private final Timer                                       timer          = new SessionTimer();
    private final AuthenticationFactory                       authenticationFactory;
    private final Map<String, Map<String, Collection<Group>>> permissions;

    public Session(final AuthenticationFactory authenticationFactory,
            final PermissionFactory permissionFactory) {
        this.authenticationFactory = authenticationFactory;
        this.permissions = new HashMap<String, Map<String, Collection<Group>>>();
        permissionFactory.all(new ResourcesChangeListener<Permission>() {

            @Override
            public void onChange(final Resources<Permission> resources,
                    final Permission resource) {
                final Map<String, Collection<Group>> actions;
                if (Session.this.permissions.containsKey(resource.resourceName)) {
                    actions = Session.this.permissions.get(resource.resourceName);
                }
                else {
                    actions = new HashMap<String, Collection<Group>>();
                    Session.this.permissions.put(resource.resourceName, actions);
                }
                actions.put(resource.action, resource.groups);
                GWT.log("added permission for '" + resource.resourceName + "#"
                        + resource.action + ": " + resource.groups, null);
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

    public enum Action {
        CREATE, RETRIEVE, RETRIEVE_ALL, UPDATE, DELETE
    }

    public boolean isAllowed(final Action action, final String resourceName) {
        return isAllowed(action.toString().toLowerCase(), resourceName);
    }

    public boolean isAllowed(final String action, final String resourceName) {
        for (final Role role : this.authentication.user.roles) {
            if (isAllowed(action, resourceName, role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowed(final Action action, final String resourceName,
            final String localeCode) {
        return isAllowed(action.toString().toLowerCase(),
                         resourceName,
                         localeCode);
    }

    public boolean isAllowed(final String action, final String resourceName,
            final String localeCode) {
        GWT.log(resourceName + "#" + action + " " + localeCode + "?", null);
        for (final Role role : this.authentication.user.roles) {
            if (isAllowed(action, resourceName, role)) {
                for (final Locale l : role.locales) {
                    if (l.code.equals(localeCode)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isAllowed(final String action, final String resourceName,
            final Role role) {
        if (this.authentication != null) {
            final Map<String, Collection<Group>> permission = this.permissions.get(resourceName);
            if (permission != null) {
                if (permission.containsKey(action)) {
                    for (final Group group : permission.get(action)) {
                        if (group.name.equals(role.name)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
