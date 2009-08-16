package de.saumya.gwt.translation.gui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ButtonBase;
import com.google.gwt.user.client.ui.Label;
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
import de.saumya.gwt.session.client.UserFactory;
import de.saumya.gwt.session.client.VenueFactory;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.WidgetTranslationPopupPanel;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.model.TranslationFactory;
import de.saumya.gwt.translation.common.client.model.WordBundleFactory;
import de.saumya.gwt.translation.common.client.model.WordFactory;
import de.saumya.gwt.translation.common.client.route.ScreenController;
import de.saumya.gwt.translation.common.client.widget.TranslatableHyperlink;

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

        final WidgetTranslationPopupPanel popupPanel = new WidgetTranslationPopupPanel();

        final GetText getText = new GetText(new WordBundleFactory(repository,
                wordFactory),
                wordFactory,
                bookFactory,
                phraseFactory,
                popupPanel);

        final Locale locale = localeFactory.newResource();
        locale.code = "en";
        getText.load(locale);

        final Session session = new Session(new AuthenticationFactory(repository,
                userFactory),
                permissionFactory);
        session.addSessionListern(new SessionListener() {

            @Override
            public void onSuccessfulLogin() {
            }

            @Override
            public void onSessionTimeout() {
                popupPanel.hide();
            }

            @Override
            public void onLoggedOut() {
            }

            @Override
            public void onAccessDenied() {
            }
        });

        final SessionPanel sessionPanel = new SessionPanel(getText,
                session,
                locale);

        final PhraseScreen phraseScreen = new PhraseScreen(getText,
                phraseFactory);

        final PhraseBookScreen phraseBookScreen = new PhraseBookScreen(bookFactory,
                phraseScreen,
                getText);

        final ScreenController screenController = new ScreenController(sessionPanel,
                getText,
                session);
        screenController.addScreen(phraseBookScreen,
                                   new TranslatableHyperlink("phrase_book",
                                           "/phrase_book/en",
                                           getText));

        new SessionController(session, loginPanel, sessionPanel);

        RootPanel.get().add(loginPanel);
        RootPanel.get().add(sessionPanel);
    }
}
