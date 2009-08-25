/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.translation.common.client.GetTextController;

public class ResourcePanel<E extends Resource<E>> extends VerticalPanel {

    protected final GetTextController getTextController;

    private final ResourceMutator<E>  mutator;

    protected ResourcePanel(final GetTextController getTextController,
            final ResourceMutator<E> mutator) {
        this.getTextController = getTextController;
        this.mutator = mutator;
    }

    protected void doReset(final E resource) {
    }

    protected final void reset(final E resource) {
        doReset(resource);
        this.mutator.pull(resource);
        setVisible(true);
    }

    public void setReadOnly(final boolean isReadOnly) {
        this.mutator.setReadOnly(isReadOnly);
    }

    protected void addTranslatableLabel(final String text) {
        add(new TranslatableLabel(text, this.getTextController));
    }
}