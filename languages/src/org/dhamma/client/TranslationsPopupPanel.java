/**
 * 
 */
package org.dhamma.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;

public class TranslationsPopupPanel extends PopupPanel {

	private final GetText getText;

	final FlowPanel flow = new FlowPanel();
	
	public TranslationsPopupPanel(GetText getText) {
		super(true);
		this.getText = getText;
		add(flow);
	}
	
	void setupTextBoxes(final Element element, KeyUpHandler keyUpHandler) {
		String[] phrases = element.getAttribute("id").split("\\|");
		int index = 0;
		NodeList<Node> list = element.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node child = list.getItem(i);
			if (child.getNodeType() == 3) {
				final Word word = getText.getWord(phrases[index++]);
				GWT.log("translate: " + word, null);
				final TextBox box = new WordNodeTextBox(child, word);
				box.setText(word.text);
				box.setVisibleLength(word.text.length());
				box.addKeyUpHandler(keyUpHandler);
				box.addKeyUpHandler(new KeyUpHandler() {
					
					@Override
					public void onKeyUp(KeyUpEvent event) {
						box.setVisibleLength(box.getText().length());
					}
				});
				flow.add(box);
			}
			else {
				setupTextBoxes(Element.as(child), keyUpHandler);
			}
		}
	}
}