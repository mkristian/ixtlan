package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.session.client.AuthenticationFactory;
import de.saumya.gwt.session.client.Locale;
import de.saumya.gwt.session.client.LocaleFactory;
import de.saumya.gwt.session.client.LoginScreen;
import de.saumya.gwt.session.client.PermissionFactory;
import de.saumya.gwt.session.client.RoleFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionController;
import de.saumya.gwt.session.client.SessionListener;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.UserFactory;
import de.saumya.gwt.session.client.VenueFactory;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.PhraseFactory;
import de.saumya.gwt.translation.common.client.TranslationFactory;
import de.saumya.gwt.translation.common.client.WordBundleFactory;
import de.saumya.gwt.translation.common.client.WordFactory;

public class GUI implements EntryPoint {

    static class LoginPanel extends VerticalPanel implements LoginScreen {

        private final Label   message  = new Label();
        private final TextBox username = new TextBox();
        private final TextBox password = new PasswordTextBox();
        private final Button  loginButton;

        public LoginPanel() {
            add(this.message);
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
        public Label message() {
            return this.message;
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

    static class SessionPanel extends HorizontalPanel implements SessionScreen {

        private final Label   welcome;
        private final Label   userLabel;

        private final Button  logoutButton;

        private final Session session;

        SessionPanel(final GetText getText, final Session session,
                final Locale defaultLocale) {
            this.session = session;
            this.welcome = new TranslatableLabel(getText);
            this.userLabel = new Label();
            this.logoutButton = new TranslatableButton("logout", getText);

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
                        for (final Locale l : session.getUser()
                                .getAllowedLocales()) {
                            if (l.code.equals(localeBox.getValue(selected))) {
                                locale = l;
                                break;
                            }
                        }
                        getText.load(locale, true);
                    }
                }
            });

            session.addSessionListern(new SessionListener() {

                @Override
                public void onSuccessfulLogin() {
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
                public void onSessionTimeout() {
                }

                @Override
                public void onLoggedOut() {
                    localeBox.clear();
                }

                @Override
                public void onAccessDenied() {
                }
            });

            add(this.welcome);
            add(this.userLabel);
            add(localeBox);
            add(this.logoutButton);
        }

        @Override
        public ButtonBase logoutButton() {
            return this.logoutButton;
        }

        @Override
        public void setVisible(final boolean visible) {
            if (visible) {
                this.welcome.setText("welcome");
                this.userLabel.setText("\u00a0" + this.session.getUser().name
                        + "<" + this.session.getUser().email + ">");
            }
            super.setVisible(visible);
        }
    }

    @Override
    public void onModuleLoad() {
        final LoginPanel loginPanel = new LoginPanel();
        final Repository repository = new Repository();
        final VenueFactory venueFactory = new VenueFactory(repository);
        final LocaleFactory localeFactory = new LocaleFactory(repository);
        final RoleFactory roleFactory = new RoleFactory(repository,
                localeFactory,
                venueFactory);
        final PermissionFactory permissionFactory = new PermissionFactory(repository,
                roleFactory);
        final UserFactory userFactory = new UserFactory(repository,
                localeFactory,
                roleFactory);

        final WordFactory wordFactory = new WordFactory(repository);
        final TranslationFactory translationFactory = new TranslationFactory(repository,
                userFactory);
        final PhraseFactory phraseFactory = new PhraseFactory(repository,
                userFactory,
                translationFactory);
        final PhraseBookFactory bookFactory = new PhraseBookFactory(repository,
                phraseFactory);
        final GetText getText = new GetText(new WordBundleFactory(repository,
                wordFactory), wordFactory, bookFactory, phraseFactory);

        final Locale locale = localeFactory.newResource();
        locale.code = "en";
        getText.load(locale);

        final Session session = new Session(new AuthenticationFactory(repository,
                userFactory),
                permissionFactory);
        final SessionPanel sessionPanel = new SessionPanel(getText,
                session,
                locale);

        new SessionController(session, loginPanel, sessionPanel);

        RootPanel.get().add(loginPanel);
        RootPanel.get().add(sessionPanel);
    }
}
