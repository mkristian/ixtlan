package de.saumya.gwt.session.client;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBoxBase;

public interface LoginScreen {

    TextBoxBase usernameTextBox();
    
    TextBoxBase passwordTextBox();
    
    ButtonBase loginButton();
    
    Label message();
    
    void setVisible(boolean visible);
}
