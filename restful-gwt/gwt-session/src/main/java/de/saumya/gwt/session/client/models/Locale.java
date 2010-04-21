/**
 * 
 */
package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithId;

public class Locale extends ResourceWithId<Locale> {

    public static final String DEFAULT_CODE = "DEFAULT";

    public static final String ALL_CODE     = "ALL";

    Locale(final Repository repository, final LocaleFactory factory,
            final int id) {
        super(repository, factory, id);
    }

    public String    code;
    public Timestamp createdAt;

    @Override
    public boolean isImmutable() {
        return true;
    }

    @Override
    protected void appendXml(final StringBuilder buf) {
        super.appendXml(buf);
        appendXml(buf, "code", this.code);
        appendXml(buf, "created_at", this.createdAt);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.code = getString(root, "code");
        this.createdAt = getTimestamp(root, "created_at");
    }

    @Override
    public void toString(final StringBuilder buf) {
        super.toString(buf);
        toString(buf, "code", this.code);
        toString(buf, "created_at", this.createdAt);
    }

    @Override
    public String display() {
        return this.code;
    }

}