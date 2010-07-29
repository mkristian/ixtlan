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

    private final Label modifiedAtLabel;
    private final Label modifiedAtValue;
    private final Label modifiedByOnlyLabel;
    private final Label modifiedByLabel;
    private final Label modifiedByValue;
    private final Label createdAtLabel;
    private final Label createdAtValue;
    private final Label createdByOnlyLabel;
    private final Label createdByLabel;
    private final Label createdByValue;

    public ResourceHeaderPanel(final GetTextController getTextController) {
        setStyleName("resource-header-panel");
        this.createdAtLabel = label("created at", getTextController);
        this.createdAtValue = label(getTextController);
        this.createdByOnlyLabel = label("created by", getTextController);
        this.createdByLabel = label("by", getTextController);
        this.createdByValue = label(getTextController);
        this.modifiedAtLabel = label("modified at", getTextController);
        this.modifiedAtValue = label(getTextController);
        this.modifiedByOnlyLabel = label("modified gby", getTextController);
        this.modifiedByLabel = label("by", getTextController);
        this.modifiedByValue = label(getTextController);
    }

    /**
     * only the resource knows whether it has updated Timestamp and/or updatedBy
     * User. an implementation needs to forward the respective info to the
     * {@link ResourceScreen#reset(Timestamp, User)} using null where the info
     * does not exists
     */
    protected void reset(final Timestamp updatedAt, final User updatedBy) {
        doReset(updatedAt,
                updatedBy,
                this.modifiedByLabel,
                this.modifiedByValue,
                this.modifiedByOnlyLabel,
                this.modifiedAtLabel,
                this.modifiedAtValue);
        setVisible(true);
    }

    protected void reset(final Timestamp createdAt, final User createdBy,
            final Timestamp updatedAt, final User updatedBy) {
        doReset(createdAt,
                createdBy,
                this.createdByLabel,
                this.createdByValue,
                this.createdByOnlyLabel,
                this.createdAtLabel,
                this.createdAtValue);
        doReset(updatedAt,
                updatedBy,
                this.modifiedByLabel,
                this.modifiedByValue,
                this.modifiedByOnlyLabel,
                this.modifiedAtLabel,
                this.modifiedAtValue);
        setVisible(true);
    }

    protected void doReset(final Timestamp at, final User by,
            final Label byLabel, final Label byValue, final Label byOnlyLabel,
            final Label atLabel, final Label atValue) {
        byLabel.setVisible(false);
        byOnlyLabel.setVisible(false);
        if (at != null) {
            atValue.setText("\u00a0" + at.toString().replaceFirst("[.]0+$", "")
                    + "\u00a0");
            if (by != null) {
                byLabel.setVisible(true);
                byValue.setText("\u00a0" + by.display() + "\u00a0");
            }
        }
        else if (by != null) {
            byLabel.setVisible(true);
            byValue.setText(by.display() + "\u00a0");
        }
        byValue.setVisible(by != null);
        atLabel.setVisible(at != null);
        atValue.setVisible(at != null);
    }

    private Label label(final GetTextController getTextController) {
        return label(null, getTextController);
    }

    private Label label(final String labelValue,
            final GetTextController getTextController) {
        final Label label = labelValue == null
                ? new Label()
                : new TranslatableLabel(getTextController, labelValue);
        label.setVisible(false);
        add(label);
        return label;
    }
}
