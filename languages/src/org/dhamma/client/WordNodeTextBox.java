/**
 * 
 */
package org.dhamma.client;

import com.google.gwt.dom.client.Node;
import com.google.gwt.user.client.ui.TextBox;

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