/**
 * 
 */
package de.saumya.gwt.translation.common.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.widget.TranslatableButton;

public class TranslationPopupPanel extends PopupPanel {

    private final VerticalPanel panel     = new VerticalPanel();

    private final TextBox       box       = new TextBox();

    private final Label         codeLabel = new Label();

    private final Button        approveIt;

    private Phrase              phrase;

    private Translatable        translatable;

    private final Session       session;

    public TranslationPopupPanel(final GetTextController getText,
            final Session session) {
        super(true);
        this.session = session;
        final KeyUpHandler keyUpHandler = new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    TranslationPopupPanel.this.hide();
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
                if (TranslationPopupPanel.this.box.getText().length() > 0) {
                    TranslationPopupPanel.this.phrase.text = TranslationPopupPanel.this.box.getText();
                    TranslationPopupPanel.this.phrase.save();
                    TranslationPopupPanel.this.translatable.reset();
                }
            }
        };
        addCloseHandler(closeHandler);

        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onSessionTimeout() {
                hide();
            }
        });
        this.box.addKeyUpHandler(keyUpHandler);
        this.panel.add(this.codeLabel);
        this.panel.add(this.box);
        final Panel buttons = new HorizontalPanel();
        final Button keep = new TranslatableButton("keep old version", getText);
        keep.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                TranslationPopupPanel.this.box.setText(TranslationPopupPanel.this.phrase.parentTranslation.text);
            }
        });
        this.approveIt = new TranslatableButton("approve", getText);
        buttons.add(this.approveIt);
        buttons.add(keep);
        this.panel.add(buttons);
        add(this.panel);
    }

    public void setup(final Phrase phrase, final Translatable translatable) {
        this.phrase = phrase;
        this.translatable = translatable;
        this.box.setText(phrase.text);
        this.box.setVisibleLength((phrase.text == null
                ? phrase.parentTranslation.text
                : phrase.text).length());

        this.codeLabel.setText(phrase.code);
        this.approveIt.setVisible(this.session.isAllowed("approver", "phrase"));
    }
}
