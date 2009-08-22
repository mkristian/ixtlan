package de.saumya.gwt.session.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Repository;

public class SessionTest implements EntryPoint {

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
        final Label  welcome = new Label();
        final Button logoutButton;

        SessionPanel() {
            add(this.welcome);
            this.logoutButton = new Button("logout");
            add(this.logoutButton);
        }

        @Override
        public ButtonBase logoutButton() {
            return this.logoutButton;
        }

    }

    @Override
    public void onModuleLoad() {
        final LoginPanel loginPanel = new LoginPanel();
        final SessionPanel sessionPanel = new SessionPanel();
        final Repository repository = new Repository();
        final LocaleFactory localeFactory = new LocaleFactory(repository);
        final GroupFactory groupFactory = new GroupFactory(repository);
        final RoleFactory roleFactory = new RoleFactory(repository,
                localeFactory,
                new VenueFactory(repository));
        final UserFactory userFactory = new UserFactory(repository,
                localeFactory,
                roleFactory);
        new SessionController(new Session(new AuthenticationFactory(repository,
                userFactory), new PermissionFactory(repository, groupFactory)),
                loginPanel,
                sessionPanel);

        RootPanel.get().add(loginPanel);
        RootPanel.get().add(sessionPanel);
    }
}
