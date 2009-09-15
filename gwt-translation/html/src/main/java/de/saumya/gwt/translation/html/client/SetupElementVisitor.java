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

    private final KeyUpHandler enterKeyHandler;

    private final KeyUpHandler extendShrinkOnTextChangeKeyHandler;

    private StringBuilder      builder = new StringBuilder();

    public SetupElementVisitor(final GetText getText, final Panel panel,
            final KeyUpHandler enterKeyHandler) {
        this.getText = getText;
        this.enterKeyHandler = enterKeyHandler;
        this.extendShrinkOnTextChangeKeyHandler = new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                final TextBox box = (TextBox) event.getSource();
                box.setVisibleLength(box.getText().length());
            }
        };
        this.panel = panel;
    }

    public SetupElementVisitor(final GetText getText, final Panel panel) {
        this(getText, panel, null);
    }

    @Override
    public void visit(final Text textNode, final String phraseCode) {
        final int width = ((Element) textNode.getParentNode()).getClientWidth() / 8;
        final Phrase phrase = this.getText.getPhrase(phraseCode);
        final String text = phrase.text == null
                ? phrase.currentText
                : phrase.text;
        this.builder.append(phrase.currentText);
        if (text.length() < 8 || text.length() < width) {
            setupTextBox(new TextBoxWithNode(textNode, phrase), text);
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

    private void setupTextBox(final TextBox box, final String text) {

        box.setText(text);
        box.setVisibleLength(text.length());
        if (this.enterKeyHandler != null) {
            box.addKeyUpHandler(this.enterKeyHandler);
            box.addKeyUpHandler(this.extendShrinkOnTextChangeKeyHandler);
        }
        this.panel.add(box);
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
        setupTextBox(box, text);

        box.setText(text);
        box.setVisibleLength(text.length());
        if (this.enterKeyHandler != null) {
            box.addKeyUpHandler(this.enterKeyHandler);
            box.addKeyUpHandler(this.extendShrinkOnTextChangeKeyHandler);
        }
        this.panel.add(box);
    }
}