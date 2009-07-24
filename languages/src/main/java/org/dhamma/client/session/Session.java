package org.dhamma.client.session;

import java.util.ArrayList;
import java.util.List;

import org.dhamma.client.resource.Resource;
import org.dhamma.client.resource.ResourceFactory;

import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Event.NativePreviewEvent;

public class Session {

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

    class SessionTimer extends Timer {

        private final int timeout   = 5;
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
            if (idle) {
                countDown--;
                if (countDown == 0) {
                    cancel();
                    fireSessionTimeout();
                }
            }
            else {
                countDown = timeout;
                idle = true;
            }
        }
    }

    private boolean loggedIn = false;
    private String  token    = null;
    private User    user     = null;

    public Session() {
        Timer timer = new SessionTimer();
        timer.schedule(60000);
    }

    void login(String username, String password) {
        if ("dhamma".equals(username) && "mudita".equals(password)) {
            loggedIn = true;
            token = "blahblah";
            user = new User();
            user.name = "Dhamma";
            user.username = "dhamma";
            user.roles = new ArrayList<Role>();
            Role role = new Role();
            role.name = "root";
            user.roles.add(role);
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
        fireLoggedOut();
    }

    boolean isAllowed(String action,
            ResourceFactory<? extends Resource<?>> factory, Role role) {
        if (loggedIn) {
            return true;
        }
        else {
            return false;
        }
    }

    User user() {
        return user;
    }

    String sessionToken() {
        return token;
    }
}
