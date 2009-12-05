/**
 * 
 */
package de.saumya.gwt.session.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

public class Locale extends Resource<Locale> {

    Locale(final Repository repository, final LocaleFactory factory) {
        super(repository, factory);
    }

    public String    code;
    public Timestamp createdAt;

    @Override
    public String key() {
        return this.code;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "code", this.code);
        append(buf, "created_at", this.createdAt);
    }

    @Override
    protected void fromXml(final Element root) {
        this.code = getString(root, "code");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public void toString(final StringBuffer buf) {
        buf.append(":code => ").append(this.code);
        buf.append(", :created_at => ").append(this.createdAt);
    }

    @Override
    public String display() {
        return this.code;
    }
}