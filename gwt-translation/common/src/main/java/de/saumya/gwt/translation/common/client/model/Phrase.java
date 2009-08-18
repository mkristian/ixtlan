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
    public String      currentText;
    public String      text;
    public Translation parent;
    public Translation defaultTranslation;
    public Timestamp   updatedAt;
    public User        updatedBy;

    @Override
    protected void appendXml(final StringBuffer buf) {
        super.appendXml(buf);
        append(buf, "code", this.code);
        append(buf, "current_text", this.currentText);
        append(buf, "text", this.text);
        append(buf, "parent", this.parent);
        append(buf, "default", this.defaultTranslation);
        append(buf, "updated_at", this.updatedAt);
        append(buf, "updated_by", this.updatedBy);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.code = getString(root, "code");
        this.defaultTranslation = this.translationFactory.getChildResource(root,
                                                                           "default");
        this.parent = this.translationFactory.getChildResource(root, "parent");
        this.text = getString(root, "text");
        this.currentText = getString(root, "current_text");
        this.updatedAt = getTimestamp(root, "updated_at");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
    }

    @Override
    public void toString(final StringBuffer buf) {
        super.toString(buf);
        buf.append(":code => ").append(this.code);
        buf.append(", :current_text => ").append(this.currentText);
        buf.append(", :text => ").append(this.text);
        buf.append(", :default => ").append(this.defaultTranslation);
        buf.append(", :parent => ").append(this.parent);
        buf.append(", :updated_at => ").append(this.updatedAt);
        if (this.updatedBy != null) {
            buf.append(", :updated_by => ");
            this.updatedBy.toString(buf);
        }
    }

}