/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import java.sql.Timestamp;
import java.util.Set;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.User;
import de.saumya.gwt.translation.common.client.GetText;

public abstract class ResourceScreen<E extends Resource<E>> extends
        VerticalPanel {

    private final ResourceHeaderPanel header;

    protected ResourceScreen(final GetText getText) {
        this.header = new ResourceHeaderPanel(getText);
        add(this.header);
    }

    protected abstract void reset(final E resource);

    @SuppressWarnings("unchecked")
    protected void reset(final E resource, final Timestamp updatedAt,
            final User updatedBy) {
        this.header.reset(resource.key(), updatedAt, updatedBy);
        for (final Widget panel : getChildren()) {
            if (panel != this.header) {
                ((AttributePanel<E>) panel).reset(resource);
            }
        }
    }

    public static class ResourceActionPanel<E extends Resource<E>> extends
            HorizontalPanel {

        private final GetText     getText;

        private final Session     session;

        private final Set<String> allowedRoles;

        private final String      resourceName;
        private final Button      create;
        private final Button      save;
        private final Button      delete;

        public ResourceActionPanel(final GetText getText,
                final Session session, final String resourceName,
                final Set<String> roles) {
            this.getText = getText;
            this.session = session;
            this.resourceName = resourceName;
            this.allowedRoles = roles;
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
                    && this.session.isAllowed("delete",
                                              this.resourceName,
                                              this.allowedRoles.iterator()
                                                      .next())) {
                this.delete.setVisible(true);

            }
            if (resource.isUptodate()
                    && this.session.isAllowed("edit",
                                              this.resourceName,
                                              this.allowedRoles.iterator()
                                                      .next())) {
                this.save.setVisible(true);

            }
            if (resource.isNew()
                    && this.session.isAllowed("create",
                                              this.resourceName,
                                              this.allowedRoles.iterator()
                                                      .next())) {
                this.create.setVisible(true);
            }
        }
    }

    public static class ResourceHeaderPanel extends HorizontalPanel {

        private final GetText getText;

        private final Label   keyLabel;
        private final Label   keyValue;
        private final Label   modifiedAtLabel;
        private final Label   modifiedAtValue;
        private final Label   byLabel;
        private final Label   modifiedByLabel;
        private final Label   modifiedByValue;

        public ResourceHeaderPanel(final GetText getText) {
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