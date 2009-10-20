/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Text;

public class TreeWalker {

    // TODO switch this to 'id' and allow customization
    private final List<String> allowedAttributeNames = Arrays.asList(new String[] {
            "x-Translate", "for", "id"              });

    private final boolean      deepWalk;
    private final List<String> EMPTY                 = Collections.emptyList();

    TreeWalker() {
        this(false);
    }

    TreeWalker(final boolean deep) {
        this.deepWalk = deep;
    }

    private Iterator<String> attributeValue(final Element element) {
        for (final String name : this.allowedAttributeNames) {
            final String value = element.getAttribute(name);
            if (value.length() > 0) {
                return Arrays.asList(value.split("\\|")).iterator();
            }
        }
        return null;
    }

    boolean isAllowed(final Element element) {
        return attributeValue(element) != null;
    }

    void accept(final Element element, final ElementVisitor visitor) {
        Iterator<String> phrases = attributeValue(element);
        if (phrases == null) {
            if (this.deepWalk) {
                phrases = this.EMPTY.iterator();
            }
            else {
                return;
            }
        }
        final String value = element.getAttribute("x-Value");
        GWT.log("value " + element.getAttribute("x-Value"), null);
        if (value.length() > 0) {
            visitor.visit(element, phrases.next());
        }
        else {
            final NodeList<Node> list = element.getChildNodes();
            for (int i = 0; i < list.getLength(); i++) {
                final Node child = list.getItem(i);
                switch (child.getNodeType()) {
                case 3: // text node
                    if (phrases.hasNext()) {
                        visitor.visit((Text) child, phrases.next());
                    }
                    break;
                case 1: // element node
                    accept(Element.as(child), visitor);
                    break;
                default: // type 8 = comment node
                    GWT.log(child.getNodeName() + " " + child.getNodeType(),
                            null);
                }
            }
        }
    }
}