/**
 * 
 */
package de.saumya.gwt.courses.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.session.client.SessionScreen;

class SessionPanel extends HorizontalPanel implements SessionScreen {
    final Label  welcome      = new Label();
    final Button logoutButton;

    SessionPanel() {
        add(welcome);
        logoutButton = new Button("logout");
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