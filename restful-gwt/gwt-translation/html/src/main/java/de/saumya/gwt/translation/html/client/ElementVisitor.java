/**
 *
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Text;

public interface ElementVisitor {

    public void visit(Text text, String phraseCode);

    public void visit(Element element, String next);

    public void reset();

}
