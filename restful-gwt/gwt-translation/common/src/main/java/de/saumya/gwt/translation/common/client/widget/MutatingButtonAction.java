/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyUpEvent;

import de.saumya.gwt.persistence.client.Resource;

public abstract class MutatingButtonAction<E extends Resource<E>> extends
        ButtonAction<E> {

    private final ResourceBindings<E> bindings;

    public MutatingButtonAction(final ResourceBindings<E> bindings) {
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
    public void onClick(final ClickEvent event) {
        this.bindings.pushIntoResource();
        action(this.resource);
    }

    @Override
    public void onKeyUp(final KeyUpEvent event) {
        if (event.getNativeKeyCode() == 32) {
            this.bindings.pushIntoResource();
            action(this.resource);
        }
    }
}