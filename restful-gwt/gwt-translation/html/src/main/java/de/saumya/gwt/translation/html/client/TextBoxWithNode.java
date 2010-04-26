/**
 *
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.translation.common.client.model.Phrase;

public class TextBoxWithNode extends TextBox implements Refresh {
    private final Node    node;
    private final Phrase  phrase;
    private final Element element;

    TextBoxWithNode(final Node node, final Phrase phrase) {
        this.element = null;
        this.node = node;
        this.phrase = phrase;
    }

    TextBoxWithNode(final Element element, final Phrase phrase) {
        this.element = element;
        this.node = null;
        this.phrase = phrase;
    }

    public void refresh() {
        if (this.element == null) {
            GWT.log("refresh " + getText() + this.node.getNodeName(), null);
            this.node.setNodeValue(getText());
        }
        else {
            GWT.log("refresh " + getText() + this.element.getNodeName(), null);
            this.element.setAttribute(this.element.getAttribute("x-Value"),
                                      getText());
        }
        this.phrase.text = getText();
        this.phrase.save();
    }

}
