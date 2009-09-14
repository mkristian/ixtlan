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

    public SessionController(final Session session,
            final LoginScreen loginScreen, final SessionScreen sessionSession) {
        this.loginScreen = loginScreen;
        this.sessionSession = sessionSession;

        session.addSessionListern(new SessionListener() {

            @Override
            public void onLogin() {
                showSessionScreen();
            }

            @Override
            public void onTimeout() {
                showLoginScreen("session timeout", false);
            }

            @Override
            public void onLogout() {
                showLoginScreen("logged out", true);
            }

            @Override
            public void onAccessDenied() {
                showLoginScreen("access denied", false);
            }

        });
        loginScreen.loginButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                session.login(loginScreen.usernameTextBox().getText(),
                              loginScreen.passwordTextBox().getText());
            }
        });
        final KeyUpHandler handler = new KeyUpHandler() {

            @Override
            public void onKeyUp(final KeyUpEvent event) {
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
            public void onClick(final ClickEvent event) {
                session.logout();
            }
        });
        sessionSession.logoutButton().addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    session.logout();
                }
            }
        });
        loginScreen.setVisible(true);
        sessionSession.setVisible(false);
    }

    public void showSessionScreen() {
        this.sessionSession.setVisible(true);
        this.loginScreen.setVisible(false);
    }

    public void showLoginScreen(final String msg, final boolean isInfo) {
        this.loginScreen.passwordTextBox().setText("");
        if (isInfo) {
            this.loginScreen.notifications().info(msg);
        }
        else {
            this.loginScreen.notifications().warn(msg);
        }
        this.loginScreen.setVisible(true);
        this.sessionSession.setVisible(false);
        this.loginScreen.usernameTextBox()
                .setSelectionRange(0,
                                   this.loginScreen.usernameTextBox()
                                           .getText()
                                           .length());
        this.loginScreen.usernameTextBox().setFocus(true);
    }

}