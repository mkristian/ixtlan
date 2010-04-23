/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithId;

public class Word extends ResourceWithId<Word> {

    private final WordFactory factory;

    Word(final Repository repository, final WordFactory factory, final int id) {
        super(repository, factory, id);
        this.factory = factory;
    }

    public String code;
    public String text;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "code", this.code);
        appendXml(buf, "text", this.text);
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = this.factory.nextId();
        this.code = getString(root, "code");
        this.text = getString(root, "text");
    }

    @Override
    public void toString(final StringBuilder buf) {
        buf.append(":code => ").append(this.code);
        buf.append(", :text => ").append(this.text);
    }

    @Override
    public String key() {
        return this.code;
    }

    @Override
    public String display() {
        return new StringBuffer(this.text).append(" <")
                .append(this.code)
                .append(">")
                .toString();
    }

    @Override
    public boolean isImmutable() {
        return true;
    }

}