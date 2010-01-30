/**
 * 
 */
package de.saumya.gwt.translation.gui.client.views.configuration;

import java.util.Map;

import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.model.Configuration;
import de.saumya.gwt.session.client.model.ConfigurationFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.DefaultResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.LoadingNotice;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.common.client.widget.ResourceHeaderPanel;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;
import de.saumya.gwt.translation.gui.client.IntegerTextBoxBinding;
import de.saumya.gwt.translation.gui.client.TextBoxBinding;

public class ConfigurationScreen extends ResourceScreen<Configuration> {

    private static class ConfigurationFields extends
            ResourceFields<Configuration> {

        ConfigurationFields(final GetTextController getTextController,
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

    private static class ConfigurationHeaders extends
            ResourceHeaderPanel<Configuration> {

        public ConfigurationHeaders(final GetTextController getTextController) {
            super(getTextController);
        }

        @Override
        public void reset(final Configuration resource) {
            reset(resource, resource.updatedAt, resource.updatedBy);
        }
    }

    public ConfigurationScreen(final LoadingNotice loadingNotice,
            final ConfigurationFactory configFactory,
            final ResourceBindings<Configuration> bindings,
            final GetTextController getTextController, final Session session,
            final ResourceNotifications notifications) {
        super(loadingNotice,
                configFactory,
                session,
                new ResourcePanel<Configuration>(new ConfigurationHeaders(getTextController),
                        new ConfigurationFields(getTextController, bindings)),
                // default action panel (save, delete, new, etc buttons)
                new DefaultResourceActionPanel<Configuration>(getTextController,
                        bindings,
                        session,
                        configFactory,
                        notifications),
                notifications);
    }

    // TODO put all these methods below except reset(...) into
    // SingletonResourceScreen
    @Override
    public void showAll(final Map<String, String> query) {
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

}