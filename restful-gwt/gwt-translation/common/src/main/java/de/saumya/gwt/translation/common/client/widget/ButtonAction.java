/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceNotifications;

public abstract class ButtonAction<E extends Resource<E>> implements
        ClickHandler, KeyUpHandler {

    private E                                resource;

    private final ResourceNotifications changeNotification;

    public ButtonAction(final ResourceNotifications changeNotification) {
        this.changeNotification = changeNotification;
    }

    public void reset(final E resource) {
        this.resource = resource;
    }

    @Override
    public void onClick(final ClickEvent event) {
        doAction();
    }

    protected void doAction() {
        this.resource.setResourceNotification(this.changeNotification);
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