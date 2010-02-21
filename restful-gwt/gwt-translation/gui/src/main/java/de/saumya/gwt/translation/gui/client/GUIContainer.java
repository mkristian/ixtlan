/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.Panel;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.session.client.AuthenticationFactory;
import de.saumya.gwt.session.client.PermissionFactory;
import de.saumya.gwt.session.client.PopupNotifications;
import de.saumya.gwt.session.client.RoleFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionController;
import de.saumya.gwt.session.client.models.Configuration;
import de.saumya.gwt.session.client.models.ConfigurationFactory;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.Group;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.model.TranslationFactory;
import de.saumya.gwt.translation.common.client.model.WordBundleFactory;
import de.saumya.gwt.translation.common.client.model.WordFactory;
import de.saumya.gwt.translation.common.client.route.ScreenController;
import de.saumya.gwt.translation.common.client.widget.HyperlinkFactory;
import de.saumya.gwt.translation.common.client.widget.LoadingNotice;
import de.saumya.gwt.translation.common.client.widget.LocaleController;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.gui.client.views.configuration.ConfigurationScreen;
import de.saumya.gwt.translation.gui.client.views.groups.GroupScreen;
import de.saumya.gwt.translation.gui.client.views.locales.LocaleScreen;
import de.saumya.gwt.translation.gui.client.views.phrases.PhraseScreen;
import de.saumya.gwt.translation.gui.client.views.session.DefaultLoginScreen;
import de.saumya.gwt.translation.gui.client.views.session.DefaultSessionScreen;
import de.saumya.gwt.translation.gui.client.views.users.UserScreen;

public class GUIContainer {
    public final Repository           repository           = new Repository();
    public final PopupNotifications   notifications        = new PopupNotifications();
    public final DomainFactory        venueFactory         = new DomainFactory(this.repository,
                                                                   this.notifications);
    public final LocaleFactory        localeFactory        = new LocaleFactory(this.repository,
                                                                   this.notifications);
    public final GroupFactory         groupFactory         = new GroupFactory(this.repository,
                                                                   this.notifications,
                                                                   this.localeFactory,
                                                                   this.venueFactory);
    public final RoleFactory          roleFactory          = new RoleFactory(this.repository,
                                                                   this.notifications);
    public final PermissionFactory    permissionFactory    = new PermissionFactory(this.repository,
                                                                   this.notifications,
                                                                   this.roleFactory);
    public final UserFactory          userFactory          = new UserFactory(this.repository,
                                                                   this.notifications,
                                                                   this.localeFactory,
                                                                   this.groupFactory);
    public final WordFactory          wordFactory          = new WordFactory(this.repository,
                                                                   this.notifications);
    public final TranslationFactory   translationFactory   = new TranslationFactory(this.repository,
                                                                   this.notifications,
                                                                   this.userFactory);
    public final PhraseFactory        phraseFactory        = new PhraseFactory(this.repository,
                                                                   this.notifications,
                                                                   this.userFactory,
                                                                   this.localeFactory,
                                                                   this.translationFactory);
    public final PhraseBookFactory    bookFactory          = new PhraseBookFactory(this.repository,
                                                                   this.notifications,
                                                                   this.phraseFactory);

    public final ConfigurationFactory configurationFactory = new ConfigurationFactory(this.repository,
                                                                   this.notifications,
                                                                   this.userFactory,
                                                                   this.localeFactory);

    public final Session              session              = new Session(this.repository,
                                                                   new AuthenticationFactory(this.repository,
                                                                           this.notifications,
                                                                           this.userFactory),
                                                                   this.permissionFactory,
                                                                   this.configurationFactory);

    public final GetText              getText              = new GetText(new WordBundleFactory(this.repository,
                                                                   this.notifications,
                                                                   this.wordFactory),
                                                                   this.wordFactory,
                                                                   this.bookFactory,
                                                                   this.phraseFactory,
                                                                   this.localeFactory);

    public final GetTextController    getTextController    = new GetTextController(this.getText,
                                                                   this.session);

    public final LoadingNotice        loadingNotice        = new LoadingNotice(this.getTextController);

    public final LocaleController     localeController     = new LocaleController(this.localeFactory);
    public final HyperlinkFactory     hyperlinkFactory     = new HyperlinkFactory(this.getTextController,
                                                                   this.localeController);
    public final DefaultSessionScreen sessionPanel         = new DefaultSessionScreen(this.getTextController,
                                                                   this.session,
                                                                   this.notifications,
                                                                   this.localeController);
    public final ScreenController     screenController     = new ScreenController(this.sessionPanel,
                                                                   this.getTextController,
                                                                   this.session,
                                                                   this.localeFactory,
                                                                   this.hyperlinkFactory);
    public final DefaultLoginScreen   loginPanel           = new DefaultLoginScreen(this.notifications);
    public final PhraseScreen         phraseScreen         = new PhraseScreen(this.loadingNotice,
                                                                   this.getTextController,
                                                                   this.phraseFactory,
                                                                   new ResourceBindings<Phrase>(),
                                                                   this.session,
                                                                   this.notifications,
                                                                   this.hyperlinkFactory,
                                                                   this.localeController);
    public final ConfigurationScreen  configurationScreen  = new ConfigurationScreen(this.loadingNotice,
                                                                   this.configurationFactory,
                                                                   new ResourceBindings<Configuration>(),
                                                                   this.getTextController,
                                                                   this.session,
                                                                   this.notifications,
                                                                   this.hyperlinkFactory);
    public final UserScreen           userScreen           = new UserScreen(this.loadingNotice,
                                                                   this.getTextController,
                                                                   this.userFactory,
                                                                   this.session,
                                                                   new ResourceBindings<User>(),
                                                                   this.notifications,
                                                                   this.hyperlinkFactory);
    public final GroupScreen          groupScreen          = new GroupScreen(this.loadingNotice,
                                                                   this.getTextController,
                                                                   this.groupFactory,
                                                                   this.session,
                                                                   new ResourceBindings<Group>(),
                                                                   this.notifications,
                                                                   this.hyperlinkFactory);
    public final LocaleScreen         localeScreen         = new LocaleScreen(this.loadingNotice,
                                                                   this.getTextController,
                                                                   this.localeFactory,
                                                                   this.session,
                                                                   new ResourceBindings<Locale>(),
                                                                   this.notifications,
                                                                   this.hyperlinkFactory);

    public GUIContainer(final Panel panel) {
        // add the screens to the screen controller which hangs them into a
        // tab-panel
        this.screenController.addScreen(this.configurationScreen,
                                        "configurations");
        this.screenController.addScreen(this.phraseScreen, "phrases");
        this.screenController.addScreen(this.userScreen, "users");
        this.screenController.addScreen(this.groupScreen, "groups");
        this.screenController.addScreen(this.localeScreen, "locales");

        // activate the session controller
        new SessionController(this.session, this.loginPanel, this.sessionPanel);

        // add the components
        // TODO use DeckPanel instead
        panel.add(this.loginPanel);
        panel.add(this.sessionPanel);
    }
}