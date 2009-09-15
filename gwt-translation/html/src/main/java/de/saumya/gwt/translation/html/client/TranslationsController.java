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
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

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
        RootPanel.getBodyElement().setAttribute("oncontextmenu",
                                                "return false;");
        final PopupPanel popup = new PopupPanel(true);
        final ComplexPanel phraseContext = new VerticalPanel();
        phraseContext.setStyleName("phrase-context");
        final FlowPanel popupTextParts = new FlowPanel();
        final FlowPanel inlineTextParts = new FlowPanel();

        phraseContext.add(popupTextParts);

        final Label currentTextLabel = new Label("current text");
        currentTextLabel.setStyleName("current-text-label");
        phraseContext.add(currentTextLabel);
        final Label currentText = new Label();
        currentText.setStyleName("current-text");
        phraseContext.add(currentText);
        popup.add(phraseContext);

        popup.addCloseHandler(new CloseHandler<PopupPanel>() {

            public void onClose(final CloseEvent<PopupPanel> event) {
                for (int i = 0; i < popupTextParts.getWidgetCount(); i++) {
                    ((Refresh) popupTextParts.getWidget(i)).refresh();
                }
                popupTextParts.clear();
                GWT.log(popupTextParts.toString(), null);
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
                popupTextParts.clear();
            }

            @Override
            public void onLogout() {
                popupTextParts.clear();
            }
        });
        final KeyUpHandler popupKeyUpHandler = new KeyUpHandler() {

            public void onKeyUp(final KeyUpEvent event) {
                if (event.getNativeKeyCode() == 13) {
                    popup.hide();
                }
            }
        };

        final TreeWalker treeWalker = new TreeWalker();
        final SetupElementVisitor popupTextPartsVisitor = new SetupElementVisitor(getText,
                popupTextParts,
                popupKeyUpHandler);
        final ElementVisitor inlineTextPartsVisitor = new SetupElementVisitor(getText,
                inlineTextParts);

        Event.addNativePreviewHandler(new Event.NativePreviewHandler() {

            private Element          old;
            private final List<Node> currentNodes = new ArrayList<Node>();
            private Element          current;
            private String           currentLabelFor;

            public void onPreviewNativeEvent(final NativePreviewEvent event) {
                if (session.hasUser()) {
                    final Element clickedElement = Element.as(event.getNativeEvent()
                            .getEventTarget());
                    if (this.current != null) {
                        if (event.getNativeEvent().getType().contains("key")
                                && event.getNativeEvent().getKeyCode() == 13) {
                            if (!"textarea".equalsIgnoreCase(clickedElement.getNodeName())) {
                                event.getNativeEvent().preventDefault();
                                event.getNativeEvent().stopPropagation();
                                writeNewContentBackToDom(inlineTextParts);
                            }
                        }
                    }
                    // only click events are handled
                    if (event.getNativeEvent().getType().equals("click")) {
                        // if we have a current element we need to write back
                        // the text
                        if (this.current != null) {
                            // find out whether the click element is child
                            // element of the current element
                            boolean isChild = false;
                            final NodeList<Element> list = this.current.getElementsByTagName(clickedElement.getNodeName());
                            for (int i = 0; i < list.getLength(); i++) {
                                isChild = isChild
                                        || (clickedElement == list.getItem(i));
                            }

                            if (!isChild) {
                                // OK the click was outside so write back the
                                // text
                                writeNewContentBackToDom(inlineTextParts);
                            }
                        }

                        if (treeWalker.isAllowed(clickedElement)) {
                            // it is an element which allows editing
                            switch (event.getNativeEvent().getButton()) {
                            case 1: // left mouse click => inline one or more
                                // textboxes

                                // first collect the all text nodes from the
                                // clicked element as well of its children
                                // and setup a text box for each one
                                inlineTextPartsVisitor.reset();
                                treeWalker.accept(clickedElement,
                                                  inlineTextPartsVisitor);

                                // copy the child nodes of the clicked element
                                // and empty the element
                                final NodeList<Node> childNodes = clickedElement.getChildNodes();
                                for (int i = 0; i < childNodes.getLength(); i++) {
                                    final Node node = childNodes.getItem(i);
                                    GWT.log("remove " + node.getNodeName()
                                            + ":" + node.getNodeValue(), null);
                                    this.currentNodes.add(node);
                                    clickedElement.removeChild(node);
                                }

                                // take the DOM from the assembled and allow
                                // inlining them
                                final Element inner = inlineTextParts.getElement();
                                if (!inner.getAttribute("style")
                                        .startsWith("display: inline")) {
                                    inner.setAttribute("style",
                                                       "display: inline;; width: auto;"
                                                               + inner.getAttribute("style"));
                                }

                                // attach the new DOM to the clicked element
                                clickedElement.appendChild(inner);
                                if (clickedElement.getNodeName()
                                        .equalsIgnoreCase("label")) {
                                    this.currentLabelFor = clickedElement.getAttribute("for");
                                    clickedElement.setAttribute("for", null);
                                }

                                // remember the clicked element for later events
                                this.current = clickedElement;
                                GWT.log("input box "
                                        + this.current.getParentElement()
                                                .getInnerHTML(), null);

                                // put the focus into the first text box
                                ((Focusable) inlineTextParts.getWidget(0)).setFocus(true);
                                break;
                            case 2: // right mouse click
                                if (!popup.isShowing()) {
                                    // first setup the input boxes
                                    popupTextPartsVisitor.reset();
                                    treeWalker.accept(clickedElement,
                                                      popupTextPartsVisitor);
                                    currentText.setText(popupTextPartsVisitor.currentText());

                                    // set the position just below the clicked
                                    // element
                                    popup.setPopupPosition(clickedElement.getAbsoluteLeft(),
                                                           clickedElement.getAbsoluteTop()
                                                                   + clickedElement.getOffsetHeight());
                                    // pop it up
                                    popup.setVisible(true);
                                    popup.show();

                                    // scroll it into view and set the focus on
                                    // the first textbox
                                    popup.getElement().scrollIntoView();
                                    ((Focusable) popupTextParts.getWidget(0)).setFocus(true);
                                }
                                break;
                            default:
                                GWT.log("no action for mouse button "
                                                + event.getNativeEvent()
                                                        .getButton(),
                                        null);
                            }
                        }
                    }
                    else {
                        // change the class attribute of editable elements on
                        // mouse hover
                        // by added an 'over' class to it
                        if (this.old != clickedElement) {
                            if (this.old != null) {
                                // remove the 'over' class from the former
                                // element
                                final String name = this.old.getClassName()
                                        .replaceFirst("over$", "")
                                        .trim();
                                this.old.setClassName(name.length() == 0
                                        ? null
                                        : name);
                            }
                            this.old = clickedElement;
                            // set the 'over' class for editable elements
                            if (treeWalker.isAllowed(clickedElement)
                                    && !popup.isShowing()) {
                                clickedElement.setClassName((clickedElement.getClassName() + " over").trim());
                            }
                        }
                    }
                }
            }

            private void writeNewContentBackToDom(
                    final FlowPanel inlineTextParts) {
                for (int i = 0; i < inlineTextParts.getWidgetCount(); i++) {
                    ((Refresh) inlineTextParts.getWidget(i)).refresh();
                }
                this.current.setInnerHTML("");

                // rebuild the DOM with previous nodes which
                // contains the new text now
                for (final Node node : this.currentNodes) {
                    GWT.log("add " + node.getNodeName() + ":"
                            + node.getNodeValue(), null);
                    this.current.appendChild(node);
                }

                // put the for attribute of the label tag back
                // in place
                if (this.current.getNodeName().equalsIgnoreCase("label")) {
                    this.current.setAttribute("for", this.currentLabelFor);
                }

                GWT.log("inline"
                        + this.current.getParentElement().getInnerHTML(), null);

                // cleanup
                inlineTextParts.clear();
                this.current = null;
                this.currentNodes.clear();
            }

        });

    }
}