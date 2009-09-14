/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.model.Phrase;

public class SetupElementVisitor implements ElementVisitor {

    private final GetText      getText;

    private final Panel        panel;

    private final KeyUpHandler keyUpHandler;

    public SetupElementVisitor(final GetText getText,
            final KeyUpHandler keyUpHandler, final Panel panel) {
        this.getText = getText;
        this.keyUpHandler = keyUpHandler;
        this.panel = panel;
    }

    @Override
    public void visit(final Text textNode, final String phraseCode) {
        final Phrase phrase = this.getText.getPhrase(phraseCode);
        final int width = ((Element) textNode.getParentNode()).getClientWidth() / 8;
        final String text = phrase.text == null
                ? phrase.currentText
                : phrase.text;
        if (text.length() < 30 || text.length() < width) {
            final TextBox box = new TextBoxWithNode(textNode, phrase);

            box.setText(text);
            box.setVisibleLength(text.length());
            box.addKeyUpHandler(this.keyUpHandler);
            box.addKeyUpHandler(new KeyUpHandler() {

                public void onKeyUp(final KeyUpEvent event) {
                    box.setVisibleLength(box.getText().length());
                }
            });
            this.panel.add(box);
        }
        else {
            final TextArea box = new TextAreaWithNode(textNode, phrase);

            box.setText(text.replaceAll("\\s\\s", " ")
                    .replaceAll("  ", " ")
                    .trim());
            box.setVisibleLines(((Element) textNode.getParentNode()).getClientHeight() / 10);
            box.setCharacterWidth(width);
            GWT.log("" + ((Element) textNode.getParentNode()).getClientWidth(),
                    null);
            GWT.log("" + ((Element) textNode.getParentNode()).getClientHeight(),
                    null);
            box.addKeyUpHandler(this.keyUpHandler);
            this.panel.add(box);
        }
    }

    @Override
    public void reset() {
        this.panel.clear();
    }
}