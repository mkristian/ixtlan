package de.saumya.gwt.session.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SessionTest implements EntryPoint {

    @Override
    public void onModuleLoad() {
        final Panel loginScreen = new VerticalPanel();
        final Label message = new Label();
        loginScreen.add(message);
        loginScreen.add(new Label("username"));
        final TextBox username = new TextBox();
        username.setTabIndex(1);
        loginScreen.add(username);
        loginScreen.add(new Label("password"));
        final TextBox password = new PasswordTextBox();
        password.setTabIndex(2);
        loginScreen.add(password);
        Button loginButton = new Button("login");
        loginButton.setTabIndex(3);
        loginScreen.add(loginButton);

        final Panel sessionScreen = new HorizontalPanel();
        final Label welcome = new Label();
        sessionScreen.add(welcome);
        Button logoutButton = new Button("logout");
        sessionScreen.add(logoutButton);

        final Session session = new Session();
        session.addSessionListern(new SessionListener() {

            @Override
            public void onSuccessfulLogin() {
                welcome.setText("welcome " + session.user().name
                        + (session.user().email == null ? "" : "<" + session.user().email + ">"));
                RootPanel.get().clear();
                RootPanel.get().add(sessionScreen);
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

            private void showLoginScreen(String msg) {
                password.setText("");
                message.setText(msg);
                RootPanel.get().clear();
                RootPanel.get().add(loginScreen);
                username.setSelectionRange(0, username.getText().length());
                username.setFocus(true);
            }
        });
        loginButton.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                session.login(username.getText(), password.getText());
            }
        });
        KeyUpHandler handler = new KeyUpHandler() {
            
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if(event.getNativeKeyCode() == 13){
                    session.login(username.getText(), password.getText());                    
                }
            }
        };
        username.addKeyUpHandler(handler);
        password.addKeyUpHandler(handler);
        loginButton.addKeyUpHandler(handler);
        logoutButton.addClickHandler(new ClickHandler() {
            
            @Override
            public void onClick(ClickEvent event) {
                session.logout();
            }
        });
        logoutButton.addKeyUpHandler(new KeyUpHandler() {
            
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if(event.getNativeKeyCode() == 13){
                    session.logout();                    
                }
            }
        });
        RootPanel.get().add(loginScreen);
    }

}
