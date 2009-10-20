/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.TextArea;

import de.saumya.gwt.translation.common.client.model.Phrase;

public class TextAreaWithNode extends TextArea implements Refresh {
    private final Node   node;
    private final Phrase phrase;

    TextAreaWithNode(final Node node, final Phrase word) {
        this.node = node;
        this.phrase = word;
    }

    public void refresh() {
        this.node.setNodeValue(getText());
        this.phrase.text = getText();
        this.phrase.save();
    }

}