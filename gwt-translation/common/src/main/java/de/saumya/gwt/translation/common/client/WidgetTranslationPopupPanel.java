/**
 * 
 */
package de.saumya.gwt.translation.common.client;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.translation.common.client.model.Phrase;

public class WidgetTranslationPopupPanel extends PopupPanel {

    private final VerticalPanel panel     = new VerticalPanel();

    private final TextBox       box       = new TextBox();

    private final Label         codeLabel = new Label();

    private Phrase              phrase;

    private Translatable        translatable;

    public WidgetTranslationPopupPanel() {
        super(true);
        final KeyUpHandler keyUpHandler = new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    WidgetTranslationPopupPanel.this.hide();
                }
                else {
                    final TextBox box = (TextBox) event.getSource();
                    box.setVisibleLength(box.getText().length());
                }
            }
        };
        final CloseHandler<PopupPanel> closeHandler = new CloseHandler<PopupPanel>() {

            @Override
            public void onClose(final CloseEvent<PopupPanel> event) {
                WidgetTranslationPopupPanel.this.phrase.toBeApproved = WidgetTranslationPopupPanel.this.box.getText();
                WidgetTranslationPopupPanel.this.phrase.save();
                WidgetTranslationPopupPanel.this.translatable.reset();
            }
        };
        addCloseHandler(closeHandler);

        this.box.addKeyUpHandler(keyUpHandler);
        this.panel.add(this.codeLabel);
        this.panel.add(this.box);
        add(this.panel);
    }

    public void setup(final Phrase phrase, final Translatable translatable) {
        this.phrase = phrase;
        this.translatable = translatable;
        this.box.setText(phrase.toBeApproved);
        this.box.setVisibleLength(phrase.toBeApproved.length());

        this.codeLabel.setText(phrase.code);
    }
}