/**
 * 
 */
package de.saumya.gwt.translation.common.client.widget;

import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.GetTextController;

public class TextBoxWithButton extends FlowPanel {

    protected final TextBox                   box = new TextBox();
    protected final TranslatableTextBoxButton button;

    TextBoxWithButton(final GetTextController getTextController) {

        this.button = new TranslatableTextBoxButton(this.box,
                getTextController);
        add(this.box);
        add(this.button);
    }
}