/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import java.util.ArrayList;
import java.util.List;

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
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;

import de.saumya.gwt.session.client.Session;
import de.saumya.gwt.session.client.SessionListenerAdapter;
import de.saumya.gwt.translation.common.client.GetText;
import de.saumya.gwt.translation.common.client.model.Phrase;

public class TranslationsController {

    static class BootstrapElementVisitor implements ElementVisitor {

        private final GetText getText;

        private int           newCount = 0;

        private int           count    = 0;

        BootstrapElementVisitor(final GetText getText) {
            this.getText = getText;
        }

        @Override
        public void reset() {
            this.newCount = 0;
            this.count = 0;
        }

        @Override
        public void visit(final Text text, final String phraseCode) {
            final Phrase phrase = this.getText.getPhrase(phraseCode,
                                                         text.getNodeValue());
            if (!phrase.isUptodate()) {
                this.newCount++;
            }
            this.count++;
        }

        int getCount() {
            return this.count;
        }

        int getNewCount() {
            return this.newCount;
        }
    }

    public TranslationsController(final GetText getText, final Session session) {
        final PopupPanel popup = new PopupPanel(true);

        final FlowPanel flow = new FlowPanel();

        popup.add(flow);

        popup.addCloseHandler(new CloseHandler<PopupPanel>() {

            public void onClose(final CloseEvent<PopupPanel> event) {
                for (int i = 0; i < flow.getWidgetCount(); i++) {
                    ((Refresh) flow.getWidget(i)).refresh();
                }
                flow.clear();
                GWT.log(flow.toString(), null);
            }
        });
        session.addSessionListern(new SessionListenerAdapter() {

            @Override
            public void onLogin() {
                final TreeWalker walker = new TreeWalker(true);
                final BootstrapElementVisitor visitor = new BootstrapElementVisitor(getText);
                walker.accept(RootPanel.getBodyElement(), visitor);
                GWT.log("new phrases  : " + visitor.getNewCount(), null);
                GWT.log("total phrases: " + visitor.getCount(), null);
            }

            @Override
            public void onTimeout() {
                flow.clear();
            }

            @Override
            public void onLogout() {
                flow.clear();
            }
        });
        final KeyUpHandler keyUpHandler = new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    popup.hide();
                }
            }
        };

        final TreeWalker treeWalker = new TreeWalker();
        final ElementVisitor visitor = new SetupElementVisitor(getText,
                keyUpHandler,
                flow);

        Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

            private Element          old;
            private final List<Node> currentNodes = new ArrayList<Node>();
            private Element          current;
            private String           currentLabelFor;

            public void onPreviewNativeEvent(final NativePreviewEvent event) {
                if (session.hasUser()) {
                    final Element e = Element.as(event.getNativeEvent()
                            .getEventTarget());
                    // if (this.current != null && e ==
                    // this.current.getElement()) {
                    // return;
                    // }
                    if (event.getNativeEvent().getType().equals("click")) {
                        if (this.current != null) {
                            boolean isChild = false;
                            final NodeList<Element> list = this.current.getElementsByTagName(e.getNodeName());
                            for (int i = 0; i < list.getLength(); i++) {
                                isChild = isChild || (e == list.getItem(i));
                            }

                            if (!isChild) {
                                for (int i = 0; i < flow.getWidgetCount(); i++) {
                                    ((Refresh) flow.getWidget(i)).refresh();
                                }
                                flow.clear();
                                this.current.setInnerHTML("");
                                for (final Node node : this.currentNodes) {
                                    GWT.log("add " + node.getNodeName() + ":"
                                            + node.getNodeValue(), null);
                                    this.current.appendChild(node);
                                }
                                if (this.current.getNodeName()
                                        .equalsIgnoreCase("label")) {
                                    this.current.setAttribute("for",
                                                              this.currentLabelFor);
                                }

                                GWT.log("inline"
                                        + this.current.getParentElement()
                                                .getInnerHTML(), null);
                                this.current = null;
                                this.currentNodes.clear();
                            }
                        }
                        if (treeWalker.isAllowedPosition(e)) {

                            switch (event.getNativeEvent().getButton()) {
                            case 1:
                                visitor.reset();
                                treeWalker.accept(e, visitor);
                                final NodeList<Node> childNodes = e.getChildNodes();
                                for (int i = 0; i < childNodes.getLength(); i++) {
                                    final Node node = childNodes.getItem(i);
                                    GWT.log("remove " + node.getNodeName()
                                            + ":" + node.getNodeValue(), null);
                                    this.currentNodes.add(node);
                                    e.removeChild(node);
                                }
                                final Element inner = flow.getElement();
                                if (!inner.getAttribute("style")
                                        .startsWith("display: inline")) {
                                    inner.setAttribute("style",
                                                       "display: inline;; width: auto;"
                                                               + inner.getAttribute("style"));
                                }
                                e.appendChild(inner);
                                if (e.getNodeName().equalsIgnoreCase("label")) {
                                    this.currentLabelFor = e.getAttribute("for");
                                    e.setAttribute("for", null);
                                }
                                this.current = e;
                                GWT.log("popup"
                                        + this.current.getParentElement()
                                                .getInnerHTML(), null);

                                ((Focusable) flow.getWidget(0)).setFocus(true);
                                break;
                            case 2:
                                if (!popup.isShowing()) {
                                    visitor.reset();
                                    treeWalker.accept(e, visitor);
                                    popup.setPopupPosition(e.getAbsoluteLeft(),
                                                           e.getAbsoluteTop()
                                                                   + e.getOffsetHeight());
                                    popup.setVisible(true);
                                    popup.show();
                                    popup.getElement().scrollIntoView();
                                    ((Focusable) flow.getWidget(0)).setFocus(true);
                                }
                                break;
                            default:
                                GWT.log("no action for button "
                                                + event.getNativeEvent()
                                                        .getButton(),
                                        null);
                            }
                        }
                        // for (int i = 0; i < flow.getWidgetCount(); i++) {
                        // GWT.log(((TextBoxWithNode)
                        // flow.getWidget(i)).getText(),
                        // null);
                        // }
                        // GWT.log(e.getNodeName() + " " + e.getId(), null);

                        // if (this.current != null
                        // && e != this.current.getElement()) {
                        // for (int i = 0; i < flow.getWidgetCount(); i++) {
                        // ((Refresh) flow.getWidget(i)).refresh();
                        // }
                        // }
                        // visitor.reset();
                        // treeWalker.accept(e, visitor);
                        // this.id = "" + System.currentTimeMillis();
                        // e.setId(this.id);
                        // this.current = RootPanel.get(this.id);
                        // this.current.clear();
                        // this.current.add(flow);
                    }
                    else {
                        // final Element e = Element.as(event.getNativeEvent()
                        // .getEventTarget());
                        if (this.old != e) {
                            if (this.old != null) {
                                final String name = this.old.getClassName()
                                        .replaceFirst("over$", "")
                                        .trim();
                                this.old.setClassName(name.length() == 0
                                        ? null
                                        : name);
                            }
                            this.old = e;
                            if (treeWalker.isAllowedPosition(e)
                                    && !popup.isShowing()) {
                                e.setClassName((e.getClassName() + " over").trim());
                            }
                        }
                    }
                }
            }

        });

    }
}