package de.saumya.gwt.session.client;

import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Widget;

public interface SessionScreen {

    ButtonBase logoutButton();

    void setVisible(boolean visible);

    void add(Widget widget);
}
