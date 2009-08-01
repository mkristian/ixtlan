package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.gettext.client.GetText;
import de.saumya.gwt.gettext.client.WordFactory;
import de.saumya.gwt.session.client.LocaleFactory;
import de.saumya.gwt.session.client.LoginScreen;
import de.saumya.gwt.session.client.RoleFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionController;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.UserFactory;
import de.saumya.gwt.session.client.VenueFactory;

public class HTML implements EntryPoint {

    static class LoginPanel extends VerticalPanel implements LoginScreen {

        private final Label   message  = new Label();
        private final TextBox username = new TextBox();
        private final TextBox password = new PasswordTextBox();
        private final Button  loginButton;

        public LoginPanel() {
            add(message);
            add(new Label("username"));
            username.setTabIndex(1);
            add(username);
            add(new Label("password"));
            password.setTabIndex(2);
            add(password);
            loginButton = new Button("login");
            loginButton.setTabIndex(3);
            add(loginButton);
        }

        @Override
        public ButtonBase loginButton() {
            return loginButton;
        }

        @Override
        public Label message() {
            return message;
        }

        @Override
        public TextBoxBase passwordTextBox() {
            return password;
        }

        @Override
        public TextBoxBase usernameTextBox() {
            return username;
        }
    }

    static class SessionPanel extends HorizontalPanel implements SessionScreen {
        final Label  welcome = new Label();
        final Button logoutButton;

        public SessionPanel() {
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

    @Override
    public void onModuleLoad() {
        final Repository repository = new Repository();
        final WordFactory wordFactory = new WordFactory(repository);

        final GetText getText = new GetText(wordFactory);
        getText.load();

        final VenueFactory venueFactory = new VenueFactory(repository);
        final LocaleFactory localeFactory = new LocaleFactory(repository);
        final RoleFactory roleFactory = new RoleFactory(repository,
                localeFactory,
                venueFactory);
        final UserFactory userFactory = new UserFactory(repository,
                localeFactory,
                roleFactory);
        final Session session = new Session(venueFactory,
                                            roleFactory,
                                            userFactory);

        new TranslationsController(getText,
                new TranslationsPopupPanel(getText),
                session);

        final LoginPanel loginPanel = new LoginPanel();
        final SessionPanel sessionPanel = new SessionPanel();

        new SessionController(session, loginPanel, sessionPanel);

        DialogBox popup = new DialogBox();
        popup.setText("control");
        Panel panel = new VerticalPanel();
        panel.add(loginPanel);
        panel.add(sessionPanel);
        popup.add(panel);
        popup.setVisible(true);
        popup.show();
    }

}
