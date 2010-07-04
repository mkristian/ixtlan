/**
 *
 */
package de.saumya.gwt.session.client.models;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;

public class Audit extends Resource<Audit> {

    protected Audit(final Repository repository, final AuditFactory factory,
            final int id) {
        super(repository, factory, id);
    }

    public String date;
    public String login;
    public String message;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "date", this.date);
        appendXml(buf, "login", this.login);
        appendXml(buf, "message", this.message);
    }

    @Override
    protected void fromElement(final Element root) {
        this.date = getString(root, "date");
        this.login = getString(root, "login");
        this.message = getString(root, "message");
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "date", this.date);
        toString(indent, buf, "login", this.login);
        toString(indent, buf, "message", this.message);
    }

    @Override
    public String display() {
        final StringBuilder builder = new StringBuilder();
        builder.append(this.date)
                .append(" [")
                .append(this.login)
                .append("] ")
                .append(this.message);
        return builder.toString();
    }

}
