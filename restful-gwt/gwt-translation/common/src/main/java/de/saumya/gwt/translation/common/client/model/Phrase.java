/**
 *
 */
package de.saumya.gwt.translation.common.client.model;

import java.sql.Timestamp;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;

public class Phrase extends Resource<Phrase> {

    private final TranslationFactory translationFactory;
    private final UserFactory        userFactory;
    private final LocaleFactory      localeFactory;

    Phrase(final Repository repository, final PhraseFactory factory,
            final TranslationFactory translationFactory,
            final UserFactory userFactory, final LocaleFactory localeFactory,
            final int id) {
        super(repository, factory, id);
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
    protected void fromElement(final Element root) {
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
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "code", this.code);
        toString(indent, buf, "current_text", this.currentText);
        toString(indent, buf, "text", this.text);
        toString(indent, buf, "parentTranslation", this.parentTranslation);
        toString(indent, buf, "default", this.defaultTranslation);
        toString(indent, buf, "locale", this.locale);
        toString(indent, buf, "updated_at", this.updatedAt);
        toString(indent, buf, "updated_by", this.updatedBy);
    }

    @Override
    public String display() {
        return new StringBuilder().append(this.currentText)
                .append(" -> ")
                .append(this.text == null ? "?" : this.text)
                .append(" <")
                .append(this.code)
                .append(":")
                .append(this.locale.display())
                .append(">")
                .toString();
    }

    public boolean isApproved() {
        return this.text == null;
    }

}
