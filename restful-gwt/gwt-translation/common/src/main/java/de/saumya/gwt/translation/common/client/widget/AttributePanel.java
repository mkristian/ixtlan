/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.GetTextController;

public abstract class AttributePanel<E extends AbstractResource<E>> extends
        HorizontalPanel {

    private final TextBox box = new TextBox();

    public AttributePanel(final String name, final GetTextController getText) {
        add(new TranslatableLabel(getText, name));
        add(this.box);
        this.box.setEnabled(false);
    }

    // String getText() {
    // return this.box.getText();
    // }

    public void setReadOnly(final boolean isReadOnly) {
        this.box.setEnabled(!isReadOnly);
    }

    void reset(final E resource) {
        this.box.setText(value(resource));
    }

    void fill(final E resource) {
        fill(resource, this.box.getText());
    }

    protected abstract void fill(E resource, String value);

    protected abstract String value(E resource);
}