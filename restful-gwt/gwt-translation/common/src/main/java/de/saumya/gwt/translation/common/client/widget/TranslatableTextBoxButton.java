/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.GetTextController;

class TranslatableTextBoxButton extends TranslatableButton {

    final TextBox box;

    TranslatableTextBoxButton(final TextBox box, final String text,
            final GetTextController getTextController) {
        super(text, getTextController);
        this.box = box;
    }

    void add(final TextBoxButtonHandler handler) {
        addClickHandler(handler);
        addKeyUpHandler(handler);
        this.box.addKeyUpHandler(new KeyUpHandler() {

            @Override
            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    final TextBox textBox = (TextBox) event.getSource();
                    final String text = textBox.getText();
                    if (text != null && text.length() > 0) {
                        handler.action(textBox);
                        event.stopPropagation();
                    }
                }
            }
        });
    }
}