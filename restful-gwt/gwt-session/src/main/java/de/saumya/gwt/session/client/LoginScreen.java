package de.saumya.gwt.session.client;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.TextBoxBase;

public interface LoginScreen {

    TextBoxBase usernameTextBox();

    TextBoxBase passwordTextBox();

    ButtonBase loginButton();

    // TODO should be just an component and not expose by the loginscreen
    Notifications notifications();

    void setVisible(boolean visible);
}
