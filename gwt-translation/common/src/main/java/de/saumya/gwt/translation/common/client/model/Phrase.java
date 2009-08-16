/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceWithID;
import de.saumya.gwt.session.client.User;
import de.saumya.gwt.session.client.UserFactory;

public class Phrase extends ResourceWithID<Phrase> {

    private final TranslationFactory translationFactory;
    private final UserFactory        userFactory;

    Phrase(final Repository repository, final PhraseFactory factory,
            final TranslationFactory translationFactory,
            final UserFactory userFactory) {
        super(repository, factory);
        this.translationFactory = translationFactory;
        this.userFactory = userFactory;
    }

    public String      code;
    public Translation origin;
    public Translation current;
    public Translation toBeTranslated;
    public String      toBeApproved;
    public Timestamp   updatedAt;
    public User        updatedBy;

    @Override
    protected void appendXml(final StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "code", this.code);
        append(buf, "origin", this.origin);
        append(buf, "current", this.current);
        append(buf, "to_be_translated", this.toBeTranslated);
        append(buf, "to_be_approved", this.toBeApproved);
        append(buf, "updated_at", this.updatedAt);
        append(buf, "updated_by", this.updatedBy);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.code = getString(root, "code");
        this.origin = this.translationFactory.getChildResource(root, "origin");
        this.current = this.translationFactory.getChildResource(root, "current");
        this.toBeTranslated = this.translationFactory.getChildResource(root,
                                                            "to_be_translated");
        this.toBeApproved = getString(root, "to_be_approved");
        this.updatedAt = getTimestamp(root, "updated_at");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
    }

    @Override
    public void toString(final StringBuffer buf) {
        super.toString(buf);
        buf.append(":code => ").append(this.code);
        buf.append(", :origin => ").append(this.origin);
        buf.append(", :current => ").append(this.current);
        buf.append(", :to_be_translated => ").append(this.toBeTranslated);
        buf.append(", :to_be_approved => ").append(this.toBeApproved);
    }

}