/**
 *
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.AnonymousResource;
import de.saumya.gwt.persistence.client.Repository;

public class Word extends AnonymousResource<Word> {

    Word(final Repository repository, final WordFactory factory) {
        super(repository, factory);
    }

    public String code;
    public String text;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "code", this.code);
        appendXml(buf, "text", this.text);
    }

    @Override
    protected void fromElement(final Element root) {
        this.code = getString(root, "code");
        this.text = getString(root, "text");
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "code", this.code);
        toString(indent, buf, "text", this.text);
    }

    @Override
    public String display() {
        return new StringBuffer(this.text).append(" <")
                .append(this.code)
                .append(">")
                .toString();
    }
}
