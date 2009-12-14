/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import de.saumya.gwt.persistence.client.Resource;

public abstract class ButtonHandler<E extends Resource<E>> implements
        ClickHandler, KeyUpHandler {

    private E                        resource;

    private final ResourceMutator<E> mutator;

    public ButtonHandler(final ResourceMutator<E> mutator) {
        this.mutator = mutator;
    }

    public void reset(final E resource) {
        this.resource = resource;
        this.mutator.pullFromResource(resource);
    }

    @Override
    public void onClick(final ClickEvent event) {
        if (this.resource != null) {
            this.mutator.pushIntoResource();
        }
        action(this.resource);
    }

    @Override
    public void onKeyUp(final KeyUpEvent event) {
        if (event.getNativeKeyCode() == 32) {
            this.mutator.pushIntoResource();
            action(this.resource);
        }
    }

    abstract protected void action(E resource);
}