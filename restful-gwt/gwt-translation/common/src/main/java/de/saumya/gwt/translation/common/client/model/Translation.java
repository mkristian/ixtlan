/**
 *
 */
package de.saumya.gwt.translation.common.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;

public class Translation extends Resource<Translation> {

    private final UserFactory userFactory;

    protected Translation(final Repository repository,
            final TranslationFactory factory, final UserFactory userFactory,
            final int id) {
        super(repository, factory, id);
        this.userFactory = userFactory;
    }

    public String    previousText;
    public String    text;
    public Timestamp approvedAt;
    public User      approvedBy;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "previous_text", this.previousText);
        appendXml(buf, "text", this.text);
        appendXml(buf, "approved_at", this.approvedAt);
        appendXml(buf, "approved_by", this.approvedBy);
    }

    @Override
    protected void fromElement(final Element root) {
        this.previousText = getString(root, "previous_text");
        this.text = getString(root, "text");
        this.approvedAt = getTimestamp(root, "approved_at");
        this.approvedBy = this.userFactory.getChildResource(root, "approved_by");
    }

    @Override
    public void toString(final StringBuilder buf) {
        buf.append(":previous_text => ").append(this.previousText);
        buf.append(":text => ").append(this.text);
        buf.append(", :approved_at => ").append(this.approvedAt);
        if (this.approvedBy != null) {
            buf.append(", :approved_by => ");
            this.approvedBy.toString(buf);
        }
    }

    @Override
    public String display() {
        return new StringBuffer("translation(").append(this.previousText)
                .append("=>")
                .append(this.text)
                .append(")")
                .toString();
    }
}
