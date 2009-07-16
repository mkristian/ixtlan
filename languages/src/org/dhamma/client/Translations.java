package org.dhamma.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Text;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Translations implements EntryPoint {

	public static class NodeTextBox extends TextBox {
		private final Node node;
		private final Word word;

		NodeTextBox(Node node, Word word) {
			this.node = node;
			this.word = word;
		}

		void refresh() {
			node.setNodeValue(getText());
			word.text = getText();
			word.save();
		}

	}

	public static class TranslationPanel extends PopupPanel {

		private final GetText getText;

		private final FlowPanel flow = new FlowPanel();
		
		private final KeyUpHandler keyUpHandler;

		public TranslationPanel(GetText getText) {
			super(true);
			this.getText = getText;
			add(flow);
			addCloseHandler(new CloseHandler<PopupPanel>() {

				@Override
				public void onClose(CloseEvent<PopupPanel> event) {
					// TODO Auto-generated method stub
					GWT.log("closed", null);
					for (int i = 0; i < flow.getWidgetCount(); i++) {
						GWT.log(flow.getWidget(i).toString(), null);
						((NodeTextBox) flow.getWidget(i)).refresh();
					}

					flow.clear();
				}
			});
			keyUpHandler = new KeyUpHandler() {

				@Override
				public void onKeyUp(KeyUpEvent event) {
					GWT.log(event.toDebugString(), null);
					if(event.getNativeKeyCode() == 13){
						hide();
					}
				}
			};
		}

		private void setupTextBoxes(final Element element) {
			String[] phrases = element.getAttribute("id").split("\\|");
			int index = 0;
			NodeList<Node> list = element.getChildNodes();
			for (int i = 0; i < list.getLength(); i++) {
				Node child = list.getItem(i);
				if (child.getNodeType() == 3) {
					final Word word = getText.getWord(phrases[index++]);
					GWT.log("translate: " + word, null);
					final TextBox box = new NodeTextBox(child, word);
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
					setupTextBoxes(Element.as(child));
				}
			}
		}

		public void translate(final Element element) {
			setupTextBoxes(element);
			setPopupPosition(element.getOffsetLeft(), element.getOffsetTop()
					+ element.getOffsetHeight() / 2);
			setVisible(true);
			show();
			((Focusable)flow.getWidget(0)).setFocus(true);
		}
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Repository repository = new Repository();
		final LocaleFactory lfactory = new LocaleFactory(repository);
		final Locale l = new Locale(repository, lfactory);
		final WordFactory wordFactory = new WordFactory(repository);

		final GetText getText = new GetText(wordFactory);
		getText.load();
		final TranslationPanel translations = new TranslationPanel(getText);

		Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

			@Override
			public void onPreviewNativeEvent(NativePreviewEvent event) {
				if (event.getNativeEvent().getType().equals("click")) {
					Element e = Element.as(event.getNativeEvent()
							.getEventTarget());
					if (e.getId().length() > 0) {
						GWT.log("here", null);
						GWT.log(Element.as(
								event.getNativeEvent().getEventTarget())
								.getId(), null);
						GWT.log(Element.as(
								event.getNativeEvent().getEventTarget())
								.getNodeName(), null);
						GWT.log(Text
								.as(event.getNativeEvent().getEventTarget())
								.getNodeType()
								+ "", null);
						NodeList<Node> l = e.getChildNodes();
						for (int i = 0; i < l.getLength(); i++) {
							String val = l.getItem(i).getNodeValue();
							if (val != null) {
								GWT.log(val, null);
							}
						}
						GWT.log("there", null);
						translations.translate(e);
					}
				} else if (!event.getNativeEvent().getType()
						.startsWith("mouse")) {
					GWT.log("+++++" + event.getNativeEvent().getType(), null);
				}
			}

		});

		if (true)
			return;

		final Label label1 = new Label("loading ...");
		final Label label2 = new Label("loading ...");
		RootPanel.get("nameFieldContainer").add(label1);
		RootPanel.get("sendButtonContainer").add(label2);

		l.country = "asd";
		l.language = "dsa";
		// l.created_at = new Timestamp(0);
		l.addResourceChangeListener(new ResourceChangeListener<Resource>() {

			@Override
			public void onChange(Resource resource) {

				Locale ll = lfactory.get(l.id, null);
				ll.removeResourceChangeListener(this);
				ll
						.addResourceChangeListener(new ResourceChangeListener<Resource>() {

							@Override
							public void onChange(Resource locale) {
								label1.setText(locale.toString());

								locale.removeResourceChangeListener(this);
								((Locale) locale).language = "rew";

								locale.save();
								lfactory.get(((Locale) locale).id, null);
								locale
										.addResourceChangeListener(new ResourceChangeListener<Resource>() {

											@Override
											public void onChange(
													Resource resource) {
												label2.setText(resource
														.toString());

												// resource.destroy();
											}
										});
							}
						});
			}

		});
		l.save();

		lfactory.all(null);
		Resources<Locale> r = new Resources<Locale>(lfactory);
		r
				.fromXml("<locales type='array'>"
						+ "<locale><id>1</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>"
						+ "<locale><id>2</id><created_at>2009-07-01T17:24:48+05:30</created_at></locale>"
						+ "</locales>");
		GWT.log(r.toString(), null);
	}
}
