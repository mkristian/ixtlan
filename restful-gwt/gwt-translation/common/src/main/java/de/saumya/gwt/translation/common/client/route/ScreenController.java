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

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.session.client.SessionScreen;
import de.saumya.gwt.session.client.Session.Action;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.TranslatableHyperlink;

public class ScreenController {

    private final TabPanel          bodyPanel  = new TabPanel();

    private final GetTextController getText;

    private final ScreenDispatcher  dispatcher = new ScreenDispatcher();

    private final List<String>      names      = new ArrayList<String>();

    public ScreenController(final SessionScreen panel,
            final GetTextController getText, final Session session) {
        this.getText = getText;
        panel.add(this.bodyPanel);
        History.addValueChangeHandler(new ValueChangeHandler<String>() {

            @Override
            public void onValueChange(final ValueChangeEvent<String> event) {
                if (event.getValue().length() > 0 && session.hasUser()) {
                    GWT.log("dispatch: " + event.getValue(), null);
                    final ScreenPath path = new ScreenPath(event.getValue());

                    ScreenController.this.bodyPanel.selectTab(ScreenController.this.names.indexOf(path.controllerName));
                    ScreenController.this.dispatcher.dispatch(path);
                }
            }
        });
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                final TabBar bar = ScreenController.this.bodyPanel.getTabBar();
                for (int i = 0; i < bar.getTabCount(); i++) {
                    final String name = ScreenController.this.names.get(i);
                    // TODO better permissions check !?!?
                    bar.setTabEnabled(i, session.isAllowed(Action.INDEX, name)
                            || session.isAllowed(Action.SHOW, name)
                            || session.isAllowed(Action.UPDATE, name));

                    // TODO something better when no Tab gets enabled
                    GWT.log(bar.toString(), null);
                }
                final String pathValue = History.getToken().length() == 0
                        ? "/"
                        : History.getToken();
                GWT.log(pathValue, null);
                final ScreenPath path = new ScreenPath(pathValue);

                ScreenController.this.bodyPanel.selectTab(ScreenController.this.names.indexOf(path.controllerName));
                ScreenController.this.dispatcher.dispatch(path);
            }

        });
    }

    public void addScreen(final Screen<?> screen, final String name) {
        addScreen(screen, new TranslatableHyperlink(name,
                "/" + name,
                this.getText));
    }

    public void addScreen(final Screen<?> screen,
            final TranslatableHyperlink link) {
        this.bodyPanel.add((Widget) screen, link);
        this.dispatcher.register(link.getCode(), screen);
        this.names.add(link.getCode());
    }
}