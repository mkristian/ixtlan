/**
 *
 */
package de.saumya.gwt.translation.common.client.widget;

import java.sql.Timestamp;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Label;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.translation.common.client.GetTextController;

public abstract class ResourceHeaderPanel<E extends AbstractResource<E>>
        extends FlowPanel implements ResourceResetable<E> {

    private final GetTextController getTextController;

    // private final Label keyLabel;
    // private final Label keyValue;
    private final Label             modifiedAtLabel;
    private final Label             modifiedAtValue;
    private final Label             byLabel;
    private final Label             modifiedByLabel;
    private final Label             modifiedByValue;

    public ResourceHeaderPanel(final GetTextController getTextController) {
        setStyleName("resource-header-panel");
        this.getTextController = getTextController;
        // this.keyLabel = label("key");
        // this.keyValue = label();
        this.modifiedAtLabel = label("modified at");
        this.modifiedAtValue = label();
        this.byLabel = label("by");
        this.modifiedByLabel = label("modified by");
        this.modifiedByValue = label();
    }

    /**
     * only the resource knows whether it has updated Timestamp and/or updatedBy
     * User. an implementation needs to forward the respective info to the
     * {@link ResourceScreen#reset(AbstractResource, Timestamp, User)} using
     * null where the info does not exists
     */
    protected void reset(final E resource, final Timestamp updatedAt,
            final User updatedBy) {
        // final int keyValue = resource.isNew() ? 0 : resource.id;
        // if (keyValue != 0) {
        // this.keyValue.setText("\u00a0" + keyValue + "\u00a0");
        // }
        // this.keyValue.setVisible(keyValue != 0);
        // this.keyLabel.setVisible(keyValue != 0);
        this.modifiedByLabel.setVisible(false);
        this.byLabel.setVisible(false);
        if (updatedAt != null) {
            this.modifiedAtValue.setText("\u00a0"
                    + updatedAt.toString().replaceFirst("[.]0+$", "")
                    + "\u00a0");
            if (updatedBy != null) {
                this.byLabel.setVisible(true);
                this.modifiedByValue.setText("\u00a0" + updatedBy.display());
            }
        }
        else if (updatedBy != null) {
            this.modifiedByLabel.setVisible(true);
            this.modifiedByValue.setText(updatedBy.display());
        }
        this.modifiedByValue.setVisible(updatedBy != null);
        this.modifiedAtLabel.setVisible(updatedAt != null);
        this.modifiedAtValue.setVisible(updatedAt != null);

        setVisible(true);
    }

    private Label label() {
        return label(null);
    }

    private Label label(final String labelValue) {
        final Label label = labelValue == null
                ? new Label()
                : new TranslatableLabel(this.getTextController, labelValue);
        label.setVisible(false);
        add(label);
        return label;
    }
}
