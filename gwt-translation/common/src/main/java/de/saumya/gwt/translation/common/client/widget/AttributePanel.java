/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.translation.common.client.GetTextController;

public abstract class AttributePanel<E extends Resource<E>> extends
        HorizontalPanel {

    private final TextBox box = new TextBox();

    public AttributePanel(final String name, final boolean isReadOnly,
            final GetTextController getText) {
        add(new TranslatableLabel(name, getText));
        add(this.box);
        this.box.setEnabled(!isReadOnly);
    }

    String getText() {
        return this.box.getText();
    }

    public void setReadOnly(final boolean isReadOnly) {
        this.box.setEnabled(!isReadOnly);
    }

    void reset(final E resource) {
        this.box.setText(value(resource));
    }

    protected abstract String value(E resource);
}