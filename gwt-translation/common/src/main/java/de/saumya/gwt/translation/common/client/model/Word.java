/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;

public class Word extends Resource<Word> {

    Word(final Repository repository, final WordFactory factory) {
        super(repository, factory);
    }

    public String code;
    public String text;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "code", this.code);
        append(buf, "text", this.text);
    }

    @Override
    protected void fromXml(final Element root) {
        this.code = getString(root, "code");
        this.text = getString(root, "text");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":code => ").append(this.code);
        buf.append(", :text => ").append(this.text);
    }

    @Override
    public String key() {
        return this.code;
    }
}