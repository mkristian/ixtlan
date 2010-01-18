/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceWithID;
import de.saumya.gwt.session.client.model.Locale;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.session.client.model.User;
import de.saumya.gwt.session.client.model.UserFactory;

public class Phrase extends ResourceWithID<Phrase> {

    private final TranslationFactory translationFactory;
    private final UserFactory        userFactory;
    private final LocaleFactory      localeFactory;

    Phrase(final Repository repository, final PhraseFactory factory,
            final TranslationFactory translationFactory,
            final UserFactory userFactory, final LocaleFactory localeFactory) {
        super(repository, factory, null);
        this.translationFactory = translationFactory;
        this.userFactory = userFactory;
        this.localeFactory = localeFactory;
    }

    public String      code;
    public String      currentText;
    public String      text;
    public Translation parentTranslation;
    public Translation defaultTranslation;
    public Timestamp   updatedAt;
    public User        updatedBy;
    public Locale      locale;

    @Override
    protected void appendXml(final StringBuilder buf) {
        super.appendXml(buf);
        appendXml(buf, "code", this.code);
        appendXml(buf, "current_text", this.currentText);
        appendXml(buf, "text", this.text);
        appendXml(buf, "parentTranslation", this.parentTranslation);
        appendXml(buf, "default", this.defaultTranslation);
        appendXml(buf, "locale", this.locale);
        appendXml(buf, "updated_at", this.updatedAt);
        appendXml(buf, "updated_by", this.updatedBy);
    }

    @Override
    protected void fromXml(final Element root) {
        super.fromXml(root);
        this.code = getString(root, "code");
        this.defaultTranslation = this.translationFactory.getChildResource(root,
                                                                           "default");
        this.parentTranslation = this.translationFactory.getChildResource(root,
                                                                          "parentTranslation");
        this.text = getString(root, "text");
        this.currentText = getString(root, "current_text");
        this.locale = this.localeFactory.getChildResource(root, "locale");
        this.updatedAt = getTimestamp(root, "updated_at");
        this.updatedBy = this.userFactory.getChildResource(root, "updated_by");
    }

    @Override
    public void toString(final StringBuilder buf) {
        super.toString(buf);
        buf.append(":code => ").append(this.code);
        buf.append(", :current_text => ").append(this.currentText);
        buf.append(", :text => ").append(this.text);
        buf.append(", :default => ").append(this.defaultTranslation);
        buf.append(", :parentTranslation => ").append(this.parentTranslation);
        buf.append(", :locale => ").append(this.locale);
        buf.append(", :updated_at => ").append(this.updatedAt);
        if (this.updatedBy != null) {
            buf.append(", :updated_by => ");
            this.updatedBy.toString(buf);
        }
    }

    @Override
    public String display() {
        return new StringBuffer(this.currentText).append("=>")
                .append(this.text == null ? "?" : this.text)
                .append(" <")
                .append(this.code)
                .append(":")
                .append(this.locale)
                .append(">")
                .toString();
    }

}