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

    private StringBuilder      builder = new StringBuilder();

    public SetupElementVisitor(final GetText getText, final Panel panel,
            final KeyUpHandler keyUpHandler) {
        this.getText = getText;
        this.keyUpHandler = keyUpHandler;
        this.panel = panel;
    }

    public SetupElementVisitor(final GetText getText, final Panel panel) {
        this(getText, panel, null);
    }

    @Override
    public void visit(final Text textNode, final String phraseCode) {
        final Phrase phrase = this.getText.getPhrase(phraseCode);
        final int width = ((Element) textNode.getParentNode()).getClientWidth() / 8;
        final String text = phrase.text == null
                ? phrase.currentText
                : phrase.text;
        this.builder.append(phrase.currentText);
        if (text.length() < 30 || text.length() < width) {
            final TextBox box = new TextBoxWithNode(textNode, phrase);

            box.setText(text);
            box.setVisibleLength(text.length());
            if (this.keyUpHandler != null) {
                box.addKeyUpHandler(this.keyUpHandler);
                box.addKeyUpHandler(new KeyUpHandler() {

                    public void onKeyUp(final KeyUpEvent event) {
                        box.setVisibleLength(box.getText().length());
                    }
                });
            }
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
            this.panel.add(box);
        }
    }

    @Override
    public void reset() {
        this.panel.clear();
        this.builder = new StringBuilder();
    }

    String currentText() {
        return this.builder.toString();
    }

    @Override
    public void visit(final Element element, final String phraseCode) {
        final Phrase phrase = this.getText.getPhrase(phraseCode);
        final String text = phrase.text == null
                ? phrase.currentText
                : phrase.text;
        this.builder.append(phrase.currentText);
        final TextBox box = new TextBoxWithNode(element, phrase);

        box.setText(text);
        box.setVisibleLength(text.length());
        if (this.keyUpHandler != null) {
            box.addKeyUpHandler(this.keyUpHandler);
            box.addKeyUpHandler(new KeyUpHandler() {

                public void onKeyUp(final KeyUpEvent event) {
                    box.setVisibleLength(box.getText().length());
                }
            });
        }
        this.panel.add(box);
    }
}