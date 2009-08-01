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

public class Session {

    class SessionTimer extends Timer {

        private final int timeout   = 1;
        private int       countDown = timeout;
        private boolean   idle      = true;

        public SessionTimer() {
            Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

                public void onPreviewNativeEvent(NativePreviewEvent event) {
                    idle = false;
                }
            });
        }

        @Override
        public void run() {
            GWT.log("idle " + idle, null);
            GWT.log("countDown " + countDown, null);
            if (idle) {
                countDown--;
                if (countDown == 0) {
                    cancel();
                    GWT.log("session timeout " + user.login, null);
                    fireSessionTimeout();
                }
            }
            else {
                countDown = timeout * 6 - 1;
                idle = true;
            }
        }
    }

    private boolean            loggedIn = false;
    private String             token    = null;
    private User               user     = null;
    private final Timer        timer    = new SessionTimer();
    private final UserFactory  userFactory;
    private final RoleFactory  roleFactory;
    private final VenueFactory venueFactory;

    public Session(VenueFactory venueFactory, RoleFactory roleFactory,
            UserFactory userFactory) {
        this.venueFactory = venueFactory;
        this.roleFactory = roleFactory;
        this.userFactory = userFactory;
    }

    private final List<SessionListener> listeners = new ArrayList<SessionListener>();

    public void addSessionListern(SessionListener listener) {
        listeners.add(listener);
    }

    public void removeSessionListern(SessionListener listener) {
        listeners.remove(listener);
    }

    private void fireSessionTimeout() {
        for (SessionListener listener : listeners) {
            listener.onSessionTimeout();
        }
    }

    private void fireAccessDenied() {
        for (SessionListener listener : listeners) {
            listener.onAccessDenied();
        }
    }

    private void fireSuccessfulLogin() {
        for (SessionListener listener : listeners) {
            listener.onSuccessfulLogin();
        }
    }

    private void fireLoggedOut() {
        for (SessionListener listener : listeners) {
            listener.onLoggedOut();
        }
    }

    void login(String username, String password) {
        if ("dhamma".equals(username) && "mudita".equals(password)) {
            loggedIn = true;
            token = "blahblah";
            user = userFactory.newResource();
            user.name = "Dhamma";
            user.login = "dhamma";
            user.roles = roleFactory.newResources();
            Role role = roleFactory.newResource();
            role.name = "root";
            user.roles.add(role);

            Role vrole = roleFactory.newResource();
            vrole.name = "editor";
            Venue dvara = venueFactory.newResource();
            dvara.id = "dvara";
            vrole.venues.add(dvara);
            Venue pajotta = venueFactory.newResource();
            dvara.id = "pajotta";
            vrole.venues.add(pajotta);
            user.roles.add(vrole);

            timer.scheduleRepeating(10000);
            GWT.log("login of " + username, null);
            fireSuccessfulLogin();
        }
        else {
            loggedIn = false;
            token = null;
            user = null;
            fireAccessDenied();
        }
    }

    void logout() {
        loggedIn = false;
        token = null;
        timer.cancel();
        GWT.log("log out " + user.login, null);
        fireLoggedOut();
    }

    enum Action {
        SHOW, SHOW_ALL, CREATE, UPDATE, DESTROY
    }

    boolean isAllowed(Action action,
            ResourceFactory<? extends Resource<?>> factory, String role) {
        Role r = findAllowedRole(action, factory, role);
        return r != null && r.locales == null && r.venues == null;
    }

    boolean isAllowed(Action action,
            ResourceFactory<? extends Resource<?>> factory, String role,
            Locale locale) {
        Role r = findAllowedRole(action, factory, role);
        if (r != null && r.venues == null) {
            for (Locale l : r.locales) {
                if (l.code.equals(locale.code)) {
                    return true;
                }
            }
        }
        return false;
    }

    boolean isAllowed(Action action,
            ResourceFactory<? extends Resource<?>> factory, String role,
            Venue venue) {
        Role r = findAllowedRole(action, factory, role);
        if (r != null && r.locales == null) {
            for (Venue v : r.venues) {
                if (v.id.equals(venue.id)) {
                    return true;
                }
            }
        }
        return false;
    }

    Role findAllowedRole(Action action,
            ResourceFactory<? extends Resource<?>> factory, String role) {
        Map<String, Map<Action, Collection<Role>>> permissions = new HashMap<String, Map<Action, Collection<Role>>>();
        // TODO make this real
        if (loggedIn) {
            Map<Action, Collection<Role>> factorypermission = permissions.get(factory.storageName());
            if (factorypermission != null) {
                for (Role r : factorypermission.get(action)) {
                    if (r.name.equals(role)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    public User user() {
        return user;
    }

    public boolean hasUser() {
        return loggedIn;
    }

    String sessionToken() {
        return token;
    }
}
