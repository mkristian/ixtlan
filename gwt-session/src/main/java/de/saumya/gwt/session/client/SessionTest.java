package de.saumya.gwt.session.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SessionTest implements EntryPoint {

    static class LoginPanel extends VerticalPanel implements LoginScreen {

        private final Label   message     = new Label();
        private final TextBox username    = new TextBox();
        private final TextBox password    = new PasswordTextBox();
        private final Button  loginButton = new Button("login");

        public LoginPanel() {
            add(message);
            add(new Label("username"));
            username.setTabIndex(1);
            add(username);
            add(new Label("password"));
            password.setTabIndex(2);
            add(password);
            loginButton.setTabIndex(3);
            add(loginButton);
        }

        @Override
        public ButtonBase loginButton() {
            return loginButton;
        }

        @Override
        public Label message() {
            return message;
        }

        @Override
        public TextBoxBase passwordTextBox() {
            return password;
        }

        @Override
        public TextBoxBase usernameTextBox() {
            return username;
        }

    }

    static class SessionPanel extends HorizontalPanel implements SessionScreen {
        final Label  welcome      = new Label();
        final Button logoutButton = new Button("logout");

        SessionPanel() {
            add(welcome);
            add(logoutButton);
        }

        @Override
        public ButtonBase logoutButton() {
            return logoutButton;
        }

        @Override
        public Label welcome() {
            return welcome;
        }

    }

    @Override
    public void onModuleLoad() {
        final LoginPanel loginPanel = new LoginPanel();
        final SessionPanel sessionPanel = new SessionPanel();

        new SessionController(loginPanel, sessionPanel);

        RootPanel.get().add(loginPanel);
        RootPanel.get().add(sessionPanel);
    }

}