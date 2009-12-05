/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.model.Locale;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.TranslatableButton;
import de.saumya.gwt.translation.common.client.widget.TranslatableLabel;

public class SessionPanel extends VerticalPanel implements SessionScreen {

    private final Label   welcome;
    private final Label   userLabel;

    private final Button  logoutButton;

    private final Session session;

    public SessionPanel(final GetTextController getTextController,
            final GetText getText, final Session session,
            final Locale defaultLocale) {
        setStyleName("session");
        this.session = session;
        final ComplexPanel header = new FlowPanel();
        header.setStyleName("session-header");
        this.welcome = new TranslatableLabel(getTextController);
        this.userLabel = new Label();
        this.logoutButton = new TranslatableButton("logout", getTextController);

        final ListBox localeBox = new ListBox();

        localeBox.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(final ChangeEvent event) {
                final int selected = localeBox.getSelectedIndex();
                GWT.log("selected " + selected + " "
                        + localeBox.getValue(selected), null);
                if (selected <= 0) {
                    getText.load(defaultLocale, false);
                }
                else {
                    Locale locale = null;
                    for (final Locale l : session.getUser().getAllowedLocales()) {
                        if (l.code.equals(localeBox.getValue(selected))) {
                            locale = l;
                            break;
                        }
                    }
                    getText.load(locale, true);
                }
            }
        });
        getText.load(defaultLocale);
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                if (session.getUser().getAllowedLocales().size() > 0) {
                    localeBox.clear();

                    localeBox.addItem("normal mode", "en");
                    for (final Locale locale : session.getUser()
                            .getAllowedLocales()) {
                        localeBox.addItem(locale.code + " " + "mode",
                                          locale.code);
                    }
                    localeBox.setVisible(true);
                }
                else {
                    localeBox.setVisible(false);
                }
            }

            @Override
            public void onTimeout() {
                localeBox.clear();
            }

            @Override
            public void onLogout() {
                localeBox.clear();
            }
        });

        header.add(this.logoutButton);
        header.add(localeBox);
        header.add(this.userLabel);
        header.add(this.welcome);

        add(header);
    }

    @Override
    public ButtonBase logoutButton() {
        return this.logoutButton;
    }

    @Override
    public void setVisible(final boolean visible) {
        if (visible) {
            this.welcome.setText("welcome");
            this.userLabel.setText("\u00a0" + this.session.getUser().name + "<"
                    + this.session.getUser().email + ">");
        }
        super.setVisible(visible);
    }
}