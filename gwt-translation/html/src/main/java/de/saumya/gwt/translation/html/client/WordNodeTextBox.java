/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.Word;

public class WordNodeTextBox extends TextBox {
    private final Node node;
    private final Word word;

    WordNodeTextBox(Node node, Word word) {
        this.node = node;
        this.word = word;
    }

    void refresh() {
        node.setNodeValue(getText());
        word.text = getText();
        word.save();
    }

}