/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import java.util.Map;

import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.model.Configuration;
import de.saumya.gwt.session.client.model.ConfigurationFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.DefaultResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

public class ConfigurationScreen extends ResourceScreen<Configuration> {

    static class ConfigurationPanel extends ResourcePanel<Configuration> {

        ConfigurationPanel(final GetTextController getTextController,
                final ResourceBindings<Configuration> bindings) {
            super(getTextController, bindings);
            add("idle session timeout (in minutes)",
                new IntegerTextBoxBinding<Configuration>() {

                    @Override
                    public void pullFrom(final Configuration resource) {
                        setText(resource.sessionIdleTimeout + "");
                    }

                    @Override
                    public void pushInto(final Configuration resource) {
                        resource.sessionIdleTimeout = Integer.parseInt(getText());
                    }
                },
                1,
                Integer.MAX_VALUE);
            add("audit log rotation (in days)",
                new IntegerTextBoxBinding<Configuration>() {

                    @Override
                    public void pullFrom(final Configuration resource) {
                        setText(resource.keepAuditLogs + "");
                    }

                    @Override
                    public void pushInto(final Configuration resource) {
                        resource.keepAuditLogs = Integer.parseInt(getText());
                    }
                },
                1,
                Integer.MAX_VALUE);
            add("email recipients for error notification (comma separated list)",
                new TextBoxBinding<Configuration>() {

                    @Override
                    public void pullFrom(final Configuration resource) {
                        setText(resource.notificationRecipientEmails);
                    }

                    @Override
                    public void pushInto(final Configuration resource) {
                        resource.notificationRecipientEmails = getText();
                    }
                },
                254);
        }
    }

    public ConfigurationScreen(final ConfigurationFactory configFactory,
            final ResourceBindings<Configuration> mutator,
            final GetTextController getTextController, final Session session,
            final ResourceNotifications notifications) {
        super(getTextController,
                configFactory,
                session,
                new ConfigurationPanel(getTextController, mutator),
                // no displayAll panel
                null,
                // default action panel (save, delete, new, etc buttons)
                new DefaultResourceActionPanel<Configuration>(getTextController,
                        mutator,
                        session,
                        configFactory,
                        notifications),
                notifications);
    }

    // TODO put all these methods below except reset(...) into
    // SingletonResourceScreen
    @Override
    public void showAll(Map<String, String> query) {
        // singletons act on urls which look like a collection of resources =>
        // no showNew, showRead, showEdit. only showAll
        showSingleton();
    }

    @Override
    public void showNew() {
        throw new UnsupportedOperationException("configuration has no 'new'");
    }

    @Override
    public void showRead(final String key) {
        throw new UnsupportedOperationException("configuration has no 'show'");
    }

    @Override
    public void showEdit(final String key) {
        throw new UnsupportedOperationException("configuration has no 'edit'");
    }

    @Override
    protected void reset(final Configuration resource) {
        reset(resource, resource.updatedAt, resource.updatedBy);
    }

    @Override
    public Screen<Phrase> child(final String key) {
        return null;
    }

}