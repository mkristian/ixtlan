/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public abstract class MutatingButtonAction<E extends Resource<E>> extends
        ButtonAction<E> {

    private final ResourceBindings<E> bindings;

    public MutatingButtonAction(
            final ResourceNotifications changeNotification,
            final ResourceBindings<E> bindings) {
        super(changeNotification);
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