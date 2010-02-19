/**
 * 
 */
package de.saumya.gwt.translation.gui.client.views.session;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.session.client.LoginScreen;
import de.saumya.gwt.session.client.Notifications;

public class LoginPanel extends VerticalPanel implements LoginScreen {

    private final Notifications notifications;
    private final TextBox       username = new TextBox();
    private final TextBox       password = new PasswordTextBox();
    private final Button        loginButton;

    public LoginPanel(final Notifications notifications) {
        setStyleName("login");
        this.notifications = notifications;
        add(new Label("username"));
        this.username.setTabIndex(1);
        add(this.username);
        add(new Label("password"));
        this.password.setTabIndex(2);
        add(this.password);
        this.loginButton = new Button("login");
        this.loginButton.setTabIndex(3);
        add(this.loginButton);
    }

    @Override
    public ButtonBase loginButton() {
        return this.loginButton;
    }

    @Override
    public Notifications notifications() {
        return this.notifications;
    }

    @Override
    public TextBoxBase passwordTextBox() {
        return this.password;
    }

    @Override
    public TextBoxBase usernameTextBox() {
        return this.username;
    }

}