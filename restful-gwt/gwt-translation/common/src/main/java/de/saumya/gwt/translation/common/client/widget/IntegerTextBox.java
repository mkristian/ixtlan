package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.TextBox;

public class IntegerTextBox extends TextBox {

    private static final KeyPressHandler keyPressHandler = new KeyPressHandler() {

                                                             @Override
                                                             public void onKeyPress(
                                                                     final KeyPressEvent event) {
                                                                 if ((event.getCharCode() < '0' || event.getCharCode() > '9')
                                                                         && KeyCodesHelper.isPrintable(event.getNativeEvent()
                                                                                 .getKeyCode())) {
                                                                     event.stopPropagation();
                                                                     event.preventDefault();
                                                                 }
                                                             }
                                                         };

    public IntegerTextBox() {
        super();
        addKeyPressHandler(keyPressHandler);
    }

    public int getTextAsInt() {
        return Integer.parseInt(getText());
    }

    public boolean isNumber() {
        try {
            getTextAsInt();
            return true;
        }
        catch (final RuntimeException e) {
            return false;
        }
    }

    public void setText(final int n) {
        setText("" + n);
    }
}
