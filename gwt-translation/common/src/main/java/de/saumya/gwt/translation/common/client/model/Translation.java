/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.datamapper.client.ResourceWithID;
import de.saumya.gwt.session.client.User;
import de.saumya.gwt.session.client.UserFactory;

public class Translation extends ResourceWithID<Translation> {

    private final UserFactory userFactory;

    protected Translation(final Repository repository,
            final ResourceFactory<Translation> factory,
            final UserFactory userFactory) {
        super(repository, factory);
        this.userFactory = userFactory;
    }

    public String    text;
    public Timestamp approvedAt;
    public User      approvedBy;

    @Override
    protected void appendXml(final StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "text", this.text);
        append(buf, "approved_at", this.approvedAt);
        append(buf, "approved_by", this.approvedBy);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.text = getString(root, "text");
        this.approvedAt = getTimestamp(root, "approved_at");
        this.approvedBy = this.userFactory.getChildResource(root, "approved_by");
    }

    @Override
    public void toString(final StringBuffer buf) {
        super.toString(buf);
        buf.append(":text => ").append(this.text);
        buf.append(", :approved_at => ").append(this.approvedAt);
        if (this.approvedBy != null) {
            buf.append(", :approved_by => ");
            this.approvedBy.toString(buf);
        }
    }

}