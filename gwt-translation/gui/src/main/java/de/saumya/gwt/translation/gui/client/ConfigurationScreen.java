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
            addTranslatableLabel("idle session timeout");
            add(new TextBoxMutator<Configuration>(mutator) {

                @Override
                public void pull(final Configuration resource) {
                    setText(resource.idleSessionTimeout + "");
                }

                @Override
                public void push(final Configuration resource) {
                    resource.idleSessionTimeout = Integer.parseInt(getText());
                }
            });
            addTranslatableLabel("audit log rotation");
            add(new TextBoxMutator<Configuration>(mutator) {

                @Override
                public void pull(final Configuration resource) {
                    setText(resource.auditLogRotation + "");
                }

                @Override
                public void push(final Configuration resource) {
                    resource.auditLogRotation = Integer.parseInt(getText());
                }
            });
            addTranslatableLabel("email for error notification");
            add(new TextBoxMutator<Configuration>(mutator) {

                @Override
                public void pull(final Configuration resource) {
                    setText(resource.emailForErrorNotification);
                }

                @Override
                public void push(final Configuration resource) {
                    resource.emailForErrorNotification = getText();
                }
            });
        }
    }

    public ConfigurationScreen(final ConfigurationFactory configFactory,
            final ResourceMutator<Configuration> mutator,
            final GetTextController getTextController, final Session session) {
        super(getTextController,
                configFactory,
                session,
                new ConfigurationPanel(getTextController, mutator),
                null,
                new ResourceActionPanel<Configuration>(getTextController,
                        mutator,
                        session,
                        configFactory));
    }

    @Override
    public void showAll() {
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