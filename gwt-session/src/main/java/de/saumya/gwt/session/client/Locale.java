/**
 * 
 */
package de.saumya.gwt.session.client;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;

public class Locale extends Resource<Locale> {

    Locale(Repository repository, LocaleFactory factory) {
        super(repository, factory);
    }

    // TODO change 'code' to 'id'
    public String code;
    public Timestamp createdAt;

    @Override
    protected String key() {
        return code;
    }

    protected void appendXml(StringBuffer buf) {
        append(buf, "code", code);
        append(buf, "created_at", createdAt);
    }

    protected void fromXml(Element root) {
        code = getString(root, "code");
        createdAt = getTimestamp(root, "created_at");
    }

    public void toString(StringBuffer buf) {
        buf.append(":code => ").append(code);
        buf.append(", :created_at => ").append(createdAt);
    }
}