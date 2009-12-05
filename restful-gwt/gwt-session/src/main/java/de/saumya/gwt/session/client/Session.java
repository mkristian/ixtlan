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

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.Resources;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;
import de.saumya.gwt.session.client.model.Group;
import de.saumya.gwt.session.client.model.Locale;
import de.saumya.gwt.session.client.model.User;

public class Session {

    class SessionTimer extends Timer {

        private final int timeout   = 5;
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

    private Authentication                                   authentication = null;
    private final Timer                                      timer          = new SessionTimer();
    private final AuthenticationFactory                      authenticationFactory;
    private final Map<String, Map<String, Collection<Role>>> permissions;
    private final Repository                                 repository;

    public Session(final Repository repository,
            final AuthenticationFactory authenticationFactory,
            final PermissionFactory permissionFactory) {
        this.repository = repository;
        this.authenticationFactory = authenticationFactory;
        this.permissions = new HashMap<String, Map<String, Collection<Role>>>();
        permissionFactory.all(new ResourcesChangeListener<Permission>() {

            @Override
            public void onChange(final Resources<Permission> resources,
                    final Permission resource) {
                final Map<String, Collection<Role>> actions;
                if (Session.this.permissions.containsKey(resource.resource)) {
                    actions = Session.this.permissions.get(resource.resource);
                }
                else {
                    actions = new HashMap<String, Collection<Role>>();
                    Session.this.permissions.put(resource.resource, actions);
                }
                actions.put(resource.action, resource.roles);
                GWT.log("added permission for '" + resource.resource + "#"
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
            listener.onTimeout();
        }
    }

    private void fireAccessDenied() {
        for (final SessionListener listener : this.listeners) {
            listener.onAccessDenied();
        }
    }

    private void fireSuccessfulLogin() {
        for (final SessionListener listener : this.listeners) {
            listener.onLogin();
        }
    }

    private void fireLoggedOut() {
        for (final SessionListener listener : this.listeners) {
            listener.onLogout();
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
        final Authentication authentication = this.authenticationFactory.newResource();
        authentication.login = username;
        authentication.password = password;
        authentication.addResourceChangeListener(new ResourceChangeListener<Authentication>() {

            @Override
            public void onChange(final Authentication resource) {
                if (resource.user.login.equals(username)) {
                    if (resource.isUptodate()) {
                        doLogin(resource);
                        GWT.log(resource.toString(), null);
                        Session.this.repository.setAuthenticationToken(resource.token);
                    }
                }
                else {
                    doAccessDenied();
                }
            }

            @Override
            public void onError(final Authentication resource, final int status) {
                if (status < 500) {
                    doAccessDenied();
                }
                else {
                    // TODO better something like doSomethingWentWrong()
                    doAccessDenied();
                }
            }
        });
        authentication.save();
    }

    void logout() {
        this.timer.cancel();
        GWT.log("log out " + this.authentication.user.login, null);
        this.authentication.destroy();
        this.authentication = null;
        fireLoggedOut();
    }

    public enum Action {
        CREATE, SHOW, INDEX, UPDATE, DESTROY
    }

    public boolean isAllowed(final Action action, final String resourceName) {
        return isAllowed(action.toString().toLowerCase(), resourceName);
    }

    public boolean isAllowed(final String action, final String resourceName) {
        for (final Group role : this.authentication.user.groups) {
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
        for (final Group role : this.authentication.user.groups) {
            GWT.log(role.toString(), null);
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
            final Group group) {
        if (this.authentication != null) {
            final Map<String, Collection<Role>> permission = this.permissions.get(resourceName);
            if (permission != null) {
                if (permission.containsKey(action)) {
                    for (final Role role : permission.get(action)) {
                        if (role.name.equals(group.name)) {
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
