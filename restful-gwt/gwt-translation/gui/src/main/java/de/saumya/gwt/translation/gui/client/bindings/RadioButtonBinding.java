/**
 *
 */
package de.saumya.gwt.translation.gui.client.bindings;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.RadioButton;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.GetTextController;
import de.saumya.gwt.translation.common.client.widget.TranslatableRadioButton;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class RadioButtonBinding<T extends AbstractResource<T>> extends
        FlowPanel implements Binding<T> {

    private final List<RadioButton> buttons;

    public RadioButtonBinding(final GetTextController getTextController,
            final String name, final Object[] labels) {
        this.buttons = new ArrayList<RadioButton>(labels.length);
        for (final Object label : labels) {
            final RadioButton button = new TranslatableRadioButton(getTextController,
                    name,
                    label.toString());
            button.setFormValue(label.toString());
            this.buttons.add(button);
            add(button);
        }
    }

    protected String getText() {
        for (final RadioButton button : this.buttons) {
            GWT.log(button.toString(), null);
            if (button.getValue()) {
                return button.getFormValue();
            }
        }
        return null;
    }

    protected void setText(final Object value) {
        final String val = value == null ? null : value.toString();
        for (final RadioButton button : this.buttons) {
            button.setValue(button.getFormValue().equals(val));
        }
    }

    @Override
    public void setEnabled(final boolean isEnabled) {
        for (final RadioButton button : this.buttons) {
            button.setEnabled(isEnabled);
        }
    }
}
