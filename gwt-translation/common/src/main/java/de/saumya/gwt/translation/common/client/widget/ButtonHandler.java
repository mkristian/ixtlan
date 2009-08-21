/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import de.saumya.gwt.datamapper.client.Resource;

public abstract class ButtonHandler<E extends Resource<E>>
        implements ClickHandler, KeyUpHandler {

    private E resource;

    public void reset(final E resource) {
        this.resource = resource;
    }

    @Override
    public void onClick(final ClickEvent event) {
        action(this.resource);
    }

    @Override
    public void onKeyUp(final KeyUpEvent event) {
        if (event.getNativeKeyCode() == 13) {
            action(this.resource);
        }
    }

    abstract protected void action(E resource);
}