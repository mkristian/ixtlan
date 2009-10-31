package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.session.client.AuthenticationFactory;
import de.saumya.gwt.session.client.LoginScreen;
import de.saumya.gwt.session.client.Notifications;
import de.saumya.gwt.session.client.PermissionFactory;
import de.saumya.gwt.session.client.PopupNotifications;
import de.saumya.gwt.session.client.RoleFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionController;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.model.GroupFactory;
import de.saumya.gwt.session.client.model.Locale;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.session.client.model.UserFactory;
import de.saumya.gwt.session.client.model.VenueFactory;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.model.TranslationFactory;
import de.saumya.gwt.translation.common.client.model.WordBundleFactory;
import de.saumya.gwt.translation.common.client.model.WordFactory;

public class HTML implements EntryPoint {

    static class LoginPanel extends FlowPanel implements LoginScreen {

        private final Notifications notifications;
        private final TextBox       username = new TextBox();
        private final TextBox       password = new PasswordTextBox();
        private final Button        loginButton;

        public LoginPanel(final Notifications notifications) {
            this.notifications = notifications;
            setStyleName("login-control");
            add(new Label("username"));
            this.username.setTabIndex(1);
            add(this.username);
            add(new Label("password"));
            this.password.setTabIndex(2);
            add(this.password);
            this.loginButton = new Button("login");
            this.loginButton.setStyleName("login-control-button");
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

    static class SessionPanel extends FlowPanel implements SessionScreen {
        final Label  welcome = new Label();
        final Button logoutButton;

        public SessionPanel() {
            setStyleName("session-control");
            add(this.welcome);
            this.logoutButton = new Button("logout");
            add(this.logoutButton);
        }

        void add(final Button button) {
            button.setStyleName("session-control-button");
            super.add(button);
        }

        @Override
        public ButtonBase logoutButton() {
            return this.logoutButton;
        }

    }

    @Override
    public void onModuleLoad() {

        // setup component insert them in the constructor
        final Repository repository = new Repository();
        final WordFactory wordFactory = new WordFactory(repository);

        final VenueFactory venueFactory = new VenueFactory(repository);
        final LocaleFactory localeFactory = new LocaleFactory(repository);
        final RoleFactory roleFactory = new RoleFactory(repository);
        final GroupFactory groupFactory = new GroupFactory(repository,
                localeFactory,
                venueFactory);
        final PermissionFactory permissionFactory = new PermissionFactory(repository,
                roleFactory);
        final UserFactory userFactory = new UserFactory(repository,
                localeFactory,
                groupFactory);
        final Session session = new Session(repository,
                new AuthenticationFactory(repository, userFactory),
                permissionFactory);
        final TranslationFactory translationFactory = new TranslationFactory(repository,
                userFactory);
        final PhraseFactory phraseFactory = new PhraseFactory(repository,
                userFactory,
                translationFactory);

        final GetText getText = new GetText(new WordBundleFactory(repository,
                wordFactory), wordFactory, new PhraseBookFactory(repository,
                phraseFactory), phraseFactory);

        // load word lists

        // TODO something better than that. at least DEFAULT locale
        final Locale locale = localeFactory.newResource();
        locale.code = "en";
        getText.load(locale);

        // setup the views + controllers
        new TranslationsController(getText, session);

        final Notifications notifications = new PopupNotifications();
        final LoginPanel loginPanel = new LoginPanel(notifications);
        final SessionPanel sessionPanel = new SessionPanel();

        final Button approveAll = new Button("approveAll");
        approveAll.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Window.alert("not implemented yet");
            }
        });
        sessionPanel.add(approveAll);
        final Button showApprovals = new Button("showApprovals");
        showApprovals.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                Window.alert("not implemented yet");
            }
        });
        sessionPanel.add(showApprovals);
        final Button showNotifications = new Button("showNotifications");
        showNotifications.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                notifications.showAll();
            }
        });
        sessionPanel.add(showNotifications);
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                final boolean allowApprovals = session.isAllowed("approve",
                                                                 "phrase");
                approveAll.setVisible(allowApprovals);
                showApprovals.setVisible(allowApprovals);
                notifications.info(new String[] {
                        "right mouse click on text will open a popup with some context",
                        "certain emtpy tag (buttons, images) can only be translated with the right mouse click",
                        "left mouse click let you edit the text inplace",
                        "clicking outside the textbox/textfield saves the edit",
                        "pressing enter inside a textfield saves the edit" });
            }

        });
        new SessionController(session, loginPanel, sessionPanel);

        RootPanel.get().add(loginPanel);
        RootPanel.get().add(sessionPanel);
    }
}
