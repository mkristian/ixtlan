/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;

public abstract class MutatingButtonAction<E extends AbstractResource<E>>
        extends ButtonAction<E> {

    private final ResourceBindings<E> bindings;

    public MutatingButtonAction(final ResourceChangeListener<E> listener,
            final ResourceBindings<E> bindings) {
        super(listener);
        this.bindings = bindings;
    }

    @Override
    public void reset(final E resource) {
        super.reset(resource);
        if (resource != null) {
            this.bindings.pullFromResource(resource);
        }
    }

    @Override
    public void doAction() {
        this.bindings.pushIntoResource();
        super.doAction();
    }

}