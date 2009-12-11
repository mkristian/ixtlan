/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.GetTextController;

public class ResourcePanel<E extends Resource<E>> extends FlowPanel {

    protected final GetTextController getTextController;

    private final ResourceMutator<E>  mutator;

    protected ResourcePanel(final GetTextController getTextController,
            final ResourceMutator<E> mutator) {
        setStyleName("resource-panel");
        this.getTextController = getTextController;
        this.mutator = mutator;
    }

    protected void doReset(final E resource) {
    }

    protected final void reset(final E resource) {
        doReset(resource);
        this.mutator.pullFromResource(resource);
        setVisible(true);
    }

    public void setReadOnly(final boolean isReadOnly) {
        this.mutator.setReadOnly(isReadOnly);
    }

    public boolean isReadOnly() {
        return this.mutator.isReadOnly();
    }

    protected void addTranslatableLabel(final String text) {
        add(new TranslatableLabel(text, this.getTextController));
    }
}