/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

public class SessionController {
    private final LoginScreen   loginScreen;
    private final SessionScreen sessionSession;

    public SessionController(final Session session, final LoginScreen loginScreen,
            final SessionScreen sessionSession) {
        this.loginScreen = loginScreen;
        this.sessionSession = sessionSession;
        
        session.addSessionListern(new SessionListener() {

            @Override
            public void onSuccessfulLogin() {
                showSessionScreen("welcome "
                        + session.getUser().name
                        + (session.getUser().email == null ? "" : "<"
                                + session.getUser().email + ">"));
            }

            @Override
            public void onSessionTimeout() {
                showLoginScreen("session timeout");
            }

            @Override
            public void onLoggedOut() {
                showLoginScreen("logged out");
            }

            @Override
            public void onAccessDenied() {
                showLoginScreen("access denied");
            }

        });
        loginScreen.loginButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                session.login(loginScreen.usernameTextBox().getText(),
                        loginScreen.passwordTextBox().getText());
            }
        });
        KeyUpHandler handler = new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    session.login(loginScreen.usernameTextBox().getText(),
                            loginScreen.passwordTextBox().getText());
                }
            }
        };
        loginScreen.usernameTextBox().addKeyUpHandler(handler);
        loginScreen.passwordTextBox().addKeyUpHandler(handler);
        loginScreen.loginButton().addKeyUpHandler(handler);

        sessionSession.logoutButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                session.logout();
            }
        });
        sessionSession.logoutButton().addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    session.logout();
                }
            }
        });
        loginScreen.setVisible(true);
        sessionSession.setVisible(false);
    }

    public void showSessionScreen(String welcome) {
        sessionSession.welcome().setText(welcome);
        sessionSession.setVisible(true);
        loginScreen.setVisible(false);
    }

    public void showLoginScreen(String msg) {
        loginScreen.passwordTextBox().setText("");
        loginScreen.message().setText(msg);
        loginScreen.setVisible(true);
        sessionSession.setVisible(false);
        loginScreen.usernameTextBox().setSelectionRange(0,
        loginScreen.usernameTextBox().getText().length());
        loginScreen.usernameTextBox().setFocus(true);
    }

}