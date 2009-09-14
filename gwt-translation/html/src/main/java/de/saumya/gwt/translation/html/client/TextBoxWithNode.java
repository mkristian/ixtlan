/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.model.Phrase;

public class TextBoxWithNode extends TextBox implements Refresh {
    private final Node   node;
    private final Phrase phrase;

    TextBoxWithNode(final Node node, final Phrase phrase) {
        this.node = node;
        this.phrase = phrase;
    }

    public void refresh() {
        GWT.log("refresh " + getText() + this.node.getNodeName(), null);
        this.node.setNodeValue(getText());
        this.phrase.text = getText();
        this.phrase.save();
    }

}