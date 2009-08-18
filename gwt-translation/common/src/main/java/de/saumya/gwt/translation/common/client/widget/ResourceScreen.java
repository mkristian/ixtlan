/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import java.sql.Timestamp;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.User;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.route.PathFactory;
import de.saumya.gwt.translation.common.client.route.Screen;

public abstract class ResourceScreen<E extends Resource<E>> extends
        VerticalPanel implements Screen<E> {

    private final ResourceHeaderPanel header;

    private final PathFactory         pathFactory;

    private PathFactory               parentPathFactory;

    protected E                       resource;

    protected ResourceScreen(final GetTextController getText,
            final ResourceFactory<E> factory) {
        this.header = new ResourceHeaderPanel(getText);
        add(this.header);
        this.pathFactory = new PathFactory(factory.storageName());
        this.parentPathFactory = this.pathFactory;
    }

    protected abstract void reset(final E resource);

    @Override
    public PathFactory getPathFactory() {
        return this.parentPathFactory;
    }

    @Override
    public void setupPathFactory(final PathFactory parentPathFactory,
            final String key) {
        this.parentPathFactory = parentPathFactory == null
                ? this.pathFactory
                : this.pathFactory.newPathFactory(parentPathFactory.showPath(key));
    }

    @SuppressWarnings("unchecked")
    protected void reset(final E resource, final Timestamp updatedAt,
            final User updatedBy) {
        this.header.reset(resource.key(), updatedAt, updatedBy);
        for (final Widget panel : getChildren()) {
            if (panel instanceof AttributePanel) {
                ((AttributePanel<E>) panel).reset(resource);
            }
        }
    }

    public static class ResourceActionPanel<E extends Resource<E>> extends
            HorizontalPanel {

        private final GetTextController getText;

        private final Session           session;

        private final String            resourceName;
        private final Button            create;
        private final Button            save;
        private final Button            delete;

        public ResourceActionPanel(final GetTextController getText,
                final Session session, final String resourceName) {
            this.getText = getText;
            this.session = session;
            this.resourceName = resourceName;
            this.create = button("create");
            this.save = button("save");
            this.delete = button("delete");
        }

        private Button button(final String name) {
            final Button button = new TranslatableButton(name, this.getText);
            button.setVisible(false);
            add(button);
            return button;
        }

        public void reset(final E resource) {
            if (resource.isUptodate()
                    && this.session.isAllowed("delete", this.resourceName)) {
                this.delete.setVisible(true);

            }
            if (resource.isUptodate()
                    && this.session.isAllowed("edit", this.resourceName)) {
                this.save.setVisible(true);

            }
            if (resource.isNew()
                    && this.session.isAllowed("create", this.resourceName)) {
                this.create.setVisible(true);
            }
        }
    }

    public static class ResourceHeaderPanel extends HorizontalPanel {

        private final GetTextController getText;

        private final Label             keyLabel;
        private final Label             keyValue;
        private final Label             modifiedAtLabel;
        private final Label             modifiedAtValue;
        private final Label             byLabel;
        private final Label             modifiedByLabel;
        private final Label             modifiedByValue;

        public ResourceHeaderPanel(final GetTextController getText) {
            this.getText = getText;
            this.keyLabel = label("key");
            this.keyValue = label();
            this.modifiedAtLabel = label("modified at");
            this.modifiedAtValue = label();
            this.byLabel = label("by");
            this.modifiedByLabel = label("modified by");
            this.modifiedByValue = label();
        }

        public void reset(final String keyValue, final Timestamp updatedAt,
                final User updatedBy) {
            if (keyValue != null) {
                this.keyLabel.setVisible(true);
                this.keyValue.setText("\u00a0" + keyValue + "\u00a0");
                this.keyValue.setVisible(true);
            }
            if (updatedAt != null) {
                this.modifiedAtLabel.setVisible(true);
                this.modifiedAtValue.setVisible(true);
                this.modifiedAtValue.setText("\u00a0" + updatedAt.toString()
                        + "\u00a0");
                if (updatedBy != null) {
                    this.byLabel.setVisible(true);
                    this.modifiedByValue.setVisible(true);
                    this.modifiedByValue.setText("\u00a0"
                            + updatedBy.toDisplay());
                }
            }
            else if (updatedBy != null) {
                this.modifiedByLabel.setVisible(true);
                this.modifiedByValue.setVisible(true);
                this.modifiedByValue.setText(updatedBy.toDisplay());
            }
        }

        private Label label() {
            return label(null);
        }

        private Label label(final String labelValue) {
            final Label label = labelValue == null
                    ? new Label()
                    : new TranslatableLabel(labelValue, this.getText);
            label.setVisible(false);
            add(label);
            return label;
        }
    }
}