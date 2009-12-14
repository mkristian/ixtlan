/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBoxBase;

abstract class TextBoxButtonHandler implements KeyUpHandler, ClickHandler {

    @Override
    public void onKeyUp(final KeyUpEvent event) {
        if (event.getNativeKeyCode() == 32) {
            doIt(event.getSource());
        }
    }

    @Override
    public void onClick(final ClickEvent event) {
        doIt(event.getSource());
    }

    private void doIt(final Object source) {
        final TextBoxBase textBox = ((TranslatableTextBoxButton) source).box;
        final String text = textBox.getText();
        if (text != null && text.length() > 0) {
            action(textBox);
        }
    }

    abstract protected void action(TextBoxBase textBox);

}