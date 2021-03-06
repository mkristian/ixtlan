package de.saumya.gwt.session.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Event.NativePreviewEvent;

import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceChangeListenerAdapter;
import de.saumya.gwt.session.client.models.Configuration;
import de.saumya.gwt.session.client.models.ConfigurationFactory;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserGroup;

public class Session {

    public enum Action {
        CREATE, SHOW, INDEX, UPDATE, DESTROY
    }

    class SessionTimer extends Timer {

        int             timeout   = 5;
        private int     countDown = this.timeout;
        private boolean idle      = true;

        public SessionTimer() {
            Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

                public void onPreviewNativeEvent(final NativePreviewEvent event) {
                    SessionTimer.this.idle = false;
                }
            });
        }

        @Override
        public void run() {
            GWT.log("session idle " + this.idle + ", countDown "
                    + this.countDown, null);
            if (this.idle) {
                this.countDown--;
                if (this.countDown == 0) {
                    cancel();
                    GWT.log("session timeout "
                            + Session.this.authentication.user.login, null);
                    doLogout();
                    fireSessionTimeout();
                }
            }
            else {
                this.countDown = this.timeout * 6 - 4;
                this.idle = true;
            }
        }
    }

    // keep it package scope for testing
    Authentication                                           authentication = null;
    final SessionTimer                                       timer          = new SessionTimer();
    final AuthenticationFactory                              authenticationFactory;

    private final ConfigurationFactory                       configurationFactory;
    private final ResourceChangeListener<Configuration>      configurationListener;
    private final Map<String, Map<String, Collection<Role>>> permissions;
    private final ResourceChangeListener<Authentication>     authenticationListener;

    private final Set<SessionListener>                       listeners      = new HashSet<SessionListener>();

    public Session(final AuthenticationFactory authenticationFactory,
            final ConfigurationFactory configurationFactory) {
        this.authenticationFactory = authenticationFactory;
        this.configurationFactory = configurationFactory;
        this.configurationListener = new ResourceChangeListenerAdapter<Configuration>() {

            @Override
            public void onChange(final Configuration configuration) {
                Session.this.timer.timeout = configuration.sessionIdleTimeout;
                GWT.log("set timeout to " + configuration.sessionIdleTimeout
                        + " minutes", null);
            }
        };
        this.permissions = new HashMap<String, Map<String, Collection<Role>>>();
        this.authenticationListener = new ResourceChangeListener<Authentication>() {

            @Override
            public void onChange(final Authentication authentication) {
                if (authentication.isUptodate()) {
                    Session.this.permissions.clear();
                    for (final Permission permission : authentication.permissions) {
                        final Map<String, Collection<Role>> actions;
                        if (Session.this.permissions.containsKey(permission.resource)) {
                            actions = Session.this.permissions.get(permission.resource);
                        }
                        else {
                            actions = new HashMap<String, Collection<Role>>();
                            Session.this.permissions.put(permission.resource,
                                                         actions);
                        }
                        actions.put(permission.action, permission.roles);
                        GWT.log("added permission for '" + permission.resource
                                + "#" + permission.action + ": "
                                + permission.roles, null);
                    }
                    doLogin(authentication);
                    if (hasUser()) {
                        History.fireCurrentHistoryState();
                    }

                }
                else if (!authentication.isDeleted()) {
                    doAccessDenied();
                }
            }

            @Override
            public void onError(final int status, final String errorMessage,
                    final Authentication resource) {
                doAccessDenied();
            }
        };
        authenticationFactory.get(this.authenticationListener);
    }

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

    void fireLoggedOut() {
        for (final SessionListener listener : this.listeners) {
            listener.onLogout();
        }
    }

    void doLogin(final Authentication authentication) {
        this.authentication = authentication;
        this.timer.scheduleRepeating(10000);
        GWT.log("login of " + authentication.user.login, null);
        // load configuration and reset the timeout of the timer
        this.configurationFactory.get(this.configurationListener);
        fireSuccessfulLogin();
    }

    void doAccessDenied() {
        // if (this.authentication != null) {
        this.authentication = null;
        fireAccessDenied();
        // }
    }

    void doLogout() {
        // this.
        this.authentication.destroy();
        this.authentication = null;
    }

    void login(final String username, final String password) {
        final Authentication authentication = this.authenticationFactory.newResource(this.authenticationListener);
        authentication.login = username;
        authentication.password = password;
        authentication.save();
    }

    void logout() {
        this.timer.cancel();
        GWT.log("log out " + this.authentication.user.login, null);
        doLogout();
        fireLoggedOut();
    }

    public boolean isAllowed(final Action action, final String resourceName) {
        return isAllowed(action.toString().toLowerCase(), resourceName);
    }

    public boolean isAllowed(final String action, final String resourceName) {
        for (final UserGroup role : this.authentication.user.groups) {
            if (isAllowed(action, resourceName, role)) {
                return true;
            }
        }
        return false;
    }

    public boolean isAllowed(final Action action, final String resourceName,
            final Locale locale) {
        return isAllowed(action.toString().toLowerCase(), resourceName, locale);
    }

    public boolean isAllowed(final String action, final String resourceName,
            final Locale locale) {
        final String localeCode = locale.code;
        for (final UserGroup group : this.authentication.user.groups) {
            if (isAllowed(action, resourceName, group)) {
                for (final Locale l : group.locales) {
                    if (l.code.equals(localeCode)
                            || l.code.equals(Locale.ALL_CODE)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isAllowed(final String action, final String resourceName,
            final UserGroup group) {
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
}
