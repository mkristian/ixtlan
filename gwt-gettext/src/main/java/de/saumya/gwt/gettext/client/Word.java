/**
 * 
 */
package de.saumya.gwt.gettext.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceWithID;

public class Word extends ResourceWithID<Word> {

    Word(Repository repository, WordFactory factory) {
        super(repository, factory);
    }

    String code;
    public String text;

    // Timestamp created_at;

    protected void appendXml(StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "code", code);
        append(buf, "text", text);
    }

    protected void fromXml(Element root) {
        super.fromXml(root);
        // TODO if (!isNew) {
        code = getString(root, "code");
        text = getString(root, "text");
        // created_at = getTimestamp(root, "created_at");
        // }
        GWT.log(this.toString(), null);
    }

    public void toString(StringBuffer buf) {
        super.toString(buf);
        buf.append(", :code => ").append(code);
        buf.append(", :text => ").append(text);
        // buf.append(", :created_at => ").append(created_at);
    }
}