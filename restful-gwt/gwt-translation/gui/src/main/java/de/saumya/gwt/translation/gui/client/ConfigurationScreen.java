/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.model.Configuration;
import de.saumya.gwt.session.client.model.ConfigurationFactory;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.route.Screen;
import de.saumya.gwt.translation.common.client.widget.ResourceActionPanel;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;
import de.saumya.gwt.translation.common.client.widget.ResourcePanel;
import de.saumya.gwt.translation.common.client.widget.ResourceScreen;

public class ConfigurationScreen extends ResourceScreen<Configuration> {

    static class ConfigurationPanel extends ResourcePanel<Configuration> {

        ConfigurationPanel(final GetTextController getTextController,
                final ResourceMutator<Configuration> mutator) {
            super(getTextController, mutator);
            add("idle session timeout (in minutes)",
                new IntegerTextBoxMutator<Configuration>(mutator) {

                    @Override
                    public void pull(final Configuration resource) {
                        setText(resource.idleSessionTimeout + "");
                    }

                    @Override
                    public void push(final Configuration resource) {
                        resource.idleSessionTimeout = Integer.parseInt(getText());
                    }
                },
                1,
                Integer.MAX_VALUE);
            add("audit log rotation (in days)",
                new IntegerTextBoxMutator<Configuration>(mutator) {

                    @Override
                    public void pull(final Configuration resource) {
                        setText(resource.auditLogRotation + "");
                    }

                    @Override
                    public void push(final Configuration resource) {
                        resource.auditLogRotation = Integer.parseInt(getText());
                    }
                },
                1,
                Integer.MAX_VALUE);
            add("email recipients for error notification (comma separated list of emails)",
                new TextBoxMutator<Configuration>(mutator) {

                    @Override
                    public void pull(final Configuration resource) {
                        setText(resource.emailForErrorNotification);
                    }

                    @Override
                    public void push(final Configuration resource) {
                        resource.emailForErrorNotification = getText();
                    }
                },
                254);
        }
    }

    public ConfigurationScreen(final ConfigurationFactory configFactory,
            final ResourceMutator<Configuration> mutator,
            final GetTextController getTextController, final Session session) {
        super(getTextController,
                configFactory,
                session,
                new ConfigurationPanel(getTextController, mutator),
                // no displayAll panel
                null,
                // default action panel (save, delete, new, etc buttons)
                new ResourceActionPanel<Configuration>(getTextController,
                        mutator,
                        session,
                        configFactory));
    }

    // TODO put all these methods below except reset(...) into
    // SingletonResourceScreen
    @Override
    public void showAll() {
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