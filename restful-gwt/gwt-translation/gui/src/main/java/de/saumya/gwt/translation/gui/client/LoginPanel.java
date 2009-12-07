/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.session.client.LoginScreen;
import de.saumya.gwt.session.client.Notifications;
import de.saumya.gwt.session.client.PopupNotifications;

public class LoginPanel extends VerticalPanel implements LoginScreen {

    private final Notifications notifications = new PopupNotifications();
    private final TextBox       username      = new TextBox();
    private final TextBox       password      = new PasswordTextBox();
    private final Button        loginButton;

    public LoginPanel() {
        setStyleName("login");
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