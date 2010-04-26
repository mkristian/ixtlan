/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.persistence.client.ResourceChangeListener;

public abstract class ButtonAction<E extends AbstractResource<E>> implements
        ClickHandler, KeyUpHandler {

    private E                               resource;

    private final ResourceChangeListener<E> listener;

    public ButtonAction(final ResourceChangeListener<E> listener) {
        this.listener = listener;
    }

    public void reset(final E resource) {
        this.resource = resource;
    }

    @Override
    public void onClick(final ClickEvent event) {
        doAction();
    }

    protected void doAction() {
        if (this.resource != null) {
            this.resource.addResourceChangeListener(this.listener);
        }
        action(this.resource);
    }

    @Override
    public void onKeyUp(final KeyUpEvent event) {
        KeyCodesHelper.isPrintable(12);
        if (event.getNativeKeyCode() == 32
                || event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
            doAction();
        }
    }

    abstract protected void action(E resource);
}