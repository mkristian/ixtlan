/**
 * 
 */
package de.saumya.gwt.translation.html.client;

import com.google.gwt.dom.client.Text;

public interface ElementVisitor {
    public void visit(final Text text, final String phraseCode);

    public void reset();
}