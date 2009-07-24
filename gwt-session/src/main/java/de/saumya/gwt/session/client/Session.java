package de.saumya.gwt.session.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Event.NativePreviewEvent;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;

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
                    GWT.log("session timeout " + user.username, null);
                    fireSessionTimeout();
                }
            }
            else {
                countDown = timeout * 6 - 1;
                idle = true;
            }
        }
    }

    private boolean     loggedIn = false;
    private String      token    = null;
    private User        user     = null;
    private final Timer timer    = new SessionTimer();

    public Session() {
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
        GWT.log("log out " + user.username, null);
        fireLoggedOut();
    }

    boolean isAllowed(String action,
            Class<ResourceFactory<? extends Resource<?>>> factoryClass, Role role) {
        //TODO make this real
        if (loggedIn) {
            return true;
        }
        else {
            return false;
        }
    }

    public User user() {
        return user;
    }

    public boolean hasUser(){
        return loggedIn;
    }
    
    String sessionToken() {
        return token;
    }
}
