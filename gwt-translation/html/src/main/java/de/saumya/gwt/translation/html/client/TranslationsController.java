/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.PopupPanel;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.translation.common.client.GetText;

public class TranslationsController {

    private final TranslationsPopupPanel panel;

    private final KeyUpHandler           keyUpHandler;

    public TranslationsController(final GetText getText,
            final TranslationsPopupPanel panel, final Session session) {
        this.panel = panel;
        panel.addCloseHandler(new CloseHandler<PopupPanel>() {

            public void onClose(final CloseEvent<PopupPanel> event) {
                for (int i = 0; i < panel.flow.getWidgetCount(); i++) {
                    ((WordNodeTextBox) panel.flow.getWidget(i)).refresh();
                }
                panel.flow.clear();
            }
        });
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onSessionTimeout() {
                panel.flow.clear();
            }

            @Override
            public void onLoggedOut() {
                panel.flow.clear();
            }
        });
        this.keyUpHandler = new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    panel.hide();
                }
            }
        };
        Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

            public void onPreviewNativeEvent(final NativePreviewEvent event) {
                if (session.hasUser()
                        && event.getNativeEvent().getType().equals("click")) {
                    final Element e = Element.as(event.getNativeEvent()
                            .getEventTarget());
                    if (e.getId().length() > 0) {
                        final NodeList<Node> l = e.getChildNodes();
                        for (int i = 0; i < l.getLength(); i++) {
                            final String val = l.getItem(i).getNodeValue();
                            if (val != null) {
                                GWT.log(val, null);
                            }
                        }
                        translate(e);
                    }
                    // } else if (!event.getNativeEvent().getType()
                    // .startsWith("mouse")) {
                    // GWT.log("+++++" + event.getNativeEvent().getType(),
                    // null);
                }
            }

        });

    }

    private void translate(final Element element) {
        this.panel.setupTextBoxes(element, this.keyUpHandler);
        this.panel.setPopupPosition(element.getOffsetLeft(),
                                    element.getOffsetTop()
                                            + element.getOffsetHeight() / 2);
        this.panel.setVisible(true);
        this.panel.show();
        ((Focusable) this.panel.flow.getWidget(0)).setFocus(true);
    }
}