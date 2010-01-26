/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.session.client.Notifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.model.Locale;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.TranslatableButton;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

public class SessionPanel extends VerticalPanel implements SessionScreen {

    private final Label         welcome;
    private final Label         userLabel;

    private final Button        logoutButton;

    private final Session       session;
    private final ListBox       localeBox;
    private final LocaleFactory localeFactory;
    private final GetText       getText;

    public SessionPanel(final GetTextController getTextController,
            final GetText getText, final Session session,
            final Notifications notifications, final LocaleFactory localeFactory) {
        setStyleName("session");
        this.session = session;
        this.localeFactory = localeFactory;
        this.getText = getText;
        final ComplexPanel header = new FlowPanel();
        header.setStyleName("session-header");
        this.welcome = new TranslatableLabel(getTextController);
        this.userLabel = new Label();
        this.logoutButton = new TranslatableButton(getTextController, "logout");

        this.localeBox = new ListBox();

        this.localeBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(final ChangeEvent event) {
                loadWordsForLocale();
            }
        });
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                if (session.getUser().getAllowedLocales().size() > 0) {
                    SessionPanel.this.localeBox.clear();

                    SessionPanel.this.localeBox.addItem("normal mode", "en");
                    for (final Locale locale : session.getUser()
                            .getAllowedLocales()) {
                        SessionPanel.this.localeBox.addItem(locale.code + " "
                                + "mode", locale.code);
                    }
                    SessionPanel.this.localeBox.setVisible(true);
                }
                else {
                    SessionPanel.this.localeBox.setVisible(false);
                }
            }

            @Override
            public void onTimeout() {
                SessionPanel.this.localeBox.clear();
            }

            @Override
            public void onLogout() {
                SessionPanel.this.localeBox.clear();
            }
        });

        final Button showNotifications = new Button("showNotifications");
        showNotifications.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                notifications.showAll();
            }
        });

        header.add(showNotifications);
        header.add(this.logoutButton);
        header.add(this.localeBox);
        header.add(this.userLabel);
        header.add(this.welcome);

        add(header);
    }

    @Override
    public ButtonBase logoutButton() {
        return this.logoutButton;
    }

    private void loadWordsForLocale() {
        final int selected = this.localeBox.getSelectedIndex();
        if (selected <= 0) {
            GWT.log("load default locale", null);
            this.getText.load(this.localeFactory.defaultLocale(), false);
        }
        else {
            Locale locale = null;
            for (final Locale l : this.session.getUser().getAllowedLocales()) {
                if (l.code.equals(this.localeBox.getValue(selected))) {
                    locale = l;
                    break;
                }
            }
            GWT.log("selected locale " + selected + " "
                    + this.localeBox.getValue(selected), null);
            this.getText.load(locale, true);
        }
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            this.welcome.setText("welcome");
            this.userLabel.setText("\u00a0" + this.session.getUser().name
                    + "\u00a0<" + this.session.getUser().email + ">");
            loadWordsForLocale();
        }
        super.setVisible(visible);
    }
}