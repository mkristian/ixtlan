package de.saumya.gwt.session.client;

import com.google.gwt.user.client.ui.ButtonBase;

public interface SessionScreen {

    ButtonBase logoutButton();

    void setVisible(boolean visible);

}
