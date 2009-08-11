/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.Word;

public class TranslationsPopupPanel extends PopupPanel {

    private final GetText getText;

    final FlowPanel       flow = new FlowPanel();

    public TranslationsPopupPanel(final GetText getText) {
        super(true);
        this.getText = getText;
        add(this.flow);
    }

    void setupTextBoxes(final Element element, final KeyUpHandler keyUpHandler) {
        final String[] phrases = element.getAttribute("id").split("\\|");
        int index = 0;
        final NodeList<Node> list = element.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            final Node child = list.getItem(i);
            if (child.getNodeType() == 3) {
                final Word word = this.getText.getWord(phrases[index++]);
                GWT.log("translate: " + word, null);
                final TextBox box = new WordNodeTextBox(child, word);
                box.setText(word.text);
                box.setVisibleLength(word.text.length());
                box.addKeyUpHandler(keyUpHandler);
                box.addKeyUpHandler(new KeyUpHandler() {

                    public void onKeyUp(final KeyUpEvent event) {
                        box.setVisibleLength(box.getText().length());
                    }
                });
                this.flow.add(box);
            }
            else {
                setupTextBoxes(Element.as(child), keyUpHandler);
            }
        }
    }
}