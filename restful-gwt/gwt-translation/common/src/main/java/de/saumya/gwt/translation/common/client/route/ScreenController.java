/**
 *
 */
package de.saumya.gwt.translation.common.client.route;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.TabBar;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.session.client.Notifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.HyperlinkFactory;
import de.saumya.gwt.translation.common.client.widget.TranslatableHyperlink;

public class ScreenController {

    private final TabPanel          tabPanel   = new TabPanel();

    private final GetTextController getTextController;

    private final ScreenDispatcher  dispatcher = new ScreenDispatcher();

    private final List<String>      names      = new ArrayList<String>();

    private final LocaleFactory     localeFactory;

    private final HyperlinkFactory  hyperlinkFactory;

    private final Session           session;

    private final Notifications     notifications;

    private String                  defaultName;

    public ScreenController(final SessionScreen panel,
            final GetTextController getTextController, final Session session,
            final LocaleFactory localeFactory,
            final HyperlinkFactory hyperlinkFactory,
            final Notifications notifications) {
        this.notifications = notifications;
        this.session = session;
        this.getTextController = getTextController;
        this.localeFactory = localeFactory;
        this.hyperlinkFactory = hyperlinkFactory;
        panel.add(this.tabPanel);
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                if (event.getValue().length() > 0 && session.hasUser()) {
                    GWT.log("dispatch history change: " + event.getValue(),
                            null);
                    dispatch(new ScreenPath(event.getValue()));
                }
            }

        });
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                final TabBar bar = ScreenController.this.tabPanel.getTabBar();
                for (int i = 0; i < bar.getTabCount(); i++) {
                    final String name = ScreenController.this.names.get(i);
                    // TODO better permissions check !?!?
                    bar.setTabEnabled(i, session.isAllowed(Action.INDEX, name)
                            || session.isAllowed(Action.SHOW, name)
                            || session.isAllowed(Action.UPDATE, name));
                }
                // final String pathValue = History.getToken().length() == 0
                // ? "/"
                // : History.getToken();
                dispatch(new ScreenPath(History.getToken()));
            }

        });
    }

    public void dispatchDefault() {
        if (History.getToken() == null || History.getToken().length() == 0) {
            History.newItem("/DEFAULT/" + this.defaultName);
        }
    }

    private void dispatch(final ScreenPath path) {
        if (path.controllerName != null) {
            GWT.log("dispatch permissions: "
                    + path.action.name().toLowerCase()
                    + " on "
                    + path.controllerName
                    + " is allowed: "
                    + this.session.isAllowed(path.action.name().toLowerCase(),
                                             path.controllerName), null);
            if (this.session.isAllowed(path.action.name().toLowerCase(),
                                       path.controllerName)) {
                this.tabPanel.selectTab(this.names.indexOf(path.controllerName));
                this.dispatcher.dispatch(path);
            }
            else {
                if (!path.controllerName.equals(this.defaultName)) {
                    this.notifications.warn("requested page does not exists");
                }
                if (this.defaultName != null) {
                    this.tabPanel.selectTab(this.names.indexOf(this.defaultName));
                    this.dispatcher.dispatch(new ScreenPath("/" + path.locale
                            + "/" + this.defaultName));
                }
                else {
                    // select no tab !!!
                    this.tabPanel.selectTab(-1);
                }
            }
        }
        switchLocale(path.locale);
    }

    public void addDefaultScreen(final Screen<?> screen, final String name) {
        addDefaultScreen(screen,
                         this.hyperlinkFactory.newTranslatableHyperlink(name,
                                                                        "/"
                                                                                + name));
    }

    public void addDefaultScreen(final Screen<?> screen,
            final TranslatableHyperlink link) {
        this.defaultName = link.getCode();
        addScreen(screen, link);
    }

    public void addScreen(final Screen<?> screen, final String name) {
        addScreen(screen,
                  this.hyperlinkFactory.newTranslatableHyperlink(name, "/"
                          + name));
    }

    public void addScreen(final Screen<?> screen,
            final TranslatableHyperlink link) {
        final int index = this.names.indexOf(link.getCode());
        if (index < 0) {
            this.tabPanel.add((Widget) screen, link);
            this.names.add(link.getCode());
        }
        else {
            this.tabPanel.remove(index);
            this.tabPanel.insert((Widget) screen, link, index);
        }
        this.dispatcher.register(link.getCode(), screen);
    }

    private void switchLocale(String localeCode) {
        if (localeCode == null || localeCode.length() == 0) {
            localeCode = Locale.DEFAULT_CODE;
        }
        boolean isInTranslation = false;
        if (localeCode.startsWith("_")) {
            localeCode = localeCode.substring(1);
            isInTranslation = true;
        }
        final Locale locale = this.localeFactory.first(localeCode);
        this.getTextController.load(locale, isInTranslation);
    }
}
