/**
 *
 */
package de.saumya.gwt.session.client.models;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

public class Locale extends Resource<Locale> {

    public static final String DEFAULT_CODE = "DEFAULT";

    public static final String ALL_CODE     = "ALL";

    private final UserFactory  userFactory;

    Locale(final Repository repository, final LocaleFactory factory,
            final int id, final UserFactory userFactory) {
        super(repository, factory, id);
        this.userFactory = userFactory;
    }

    public String    code;

    public Timestamp createdAt;

    public User      createdBy;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "code", this.code);
        appendXml(buf, "created_at", this.createdAt);
        appendXml(buf, "created_by", this.createdBy);
    }

    @Override
    protected void fromElement(final Element root) {
        this.code = getString(root, "code");
        this.createdAt = getTimestamp(root, "created_at");
        this.createdBy = this.userFactory.getChildResource(root,
                                                           "created_by",
                                                           this.createdBy);
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "code", this.code);
        toString(indent, buf, "created_at", this.createdAt);
        toString(indent, buf, "created_by", this.createdBy);
    }

    @Override
    public String display() {
        return this.code;
    }

}
