/**
 * 
 */
package de.saumya.gwt.translation.gui.client.views.configuration;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.models.Configuration;
import de.saumya.gwt.session.client.models.ConfigurationFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.HyperlinkFactory;
import de.saumya.gwt.translation.common.client.widget.LoadingNotice;
import de.saumya.gwt.translation.common.client.widget.NotificationListeners;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings;
import de.saumya.gwt.translation.common.client.widget.ResourceFields;
import de.saumya.gwt.translation.common.client.widget.ResourceHeaderPanel;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.SingletonResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.SingletonResourceScreen;
import de.saumya.gwt.translation.gui.client.bindings.IntegerTextBoxBinding;
import de.saumya.gwt.translation.gui.client.bindings.TextBoxBinding;

public class ConfigurationScreen extends SingletonResourceScreen<Configuration> {

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
            add("email address of sender for error notification",
                new TextBoxBinding<Configuration>() {

                    @Override
                    public void pullFrom(final Configuration resource) {
                        setText(resource.notificationSenderEmail);
                    }

                    @Override
                    public void pushInto(final Configuration resource) {
                        resource.notificationSenderEmail = getText();
                    }
                },
                64);
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
            final NotificationListeners notifications,
            final HyperlinkFactory hyperlinkFactory) {
        super(loadingNotice,
                configFactory,
                session,
                new ResourcePanel<Configuration>(new ConfigurationHeaders(getTextController),
                        new ConfigurationFields(getTextController, bindings)),
                // default action panel (save, delete, new, etc buttons)
                new SingletonResourceActionPanel<Configuration>(getTextController,
                        bindings,
                        session,
                        configFactory,
                        notifications,
                        hyperlinkFactory),
                notifications,
                hyperlinkFactory);
    }

}