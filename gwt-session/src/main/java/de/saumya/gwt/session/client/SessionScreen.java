package de.saumya.gwt.session.client;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Label;

public interface SessionScreen {
    
    ButtonBase logoutButton();
    
    Label welcome();
    
    void setVisible(boolean visible);

}
