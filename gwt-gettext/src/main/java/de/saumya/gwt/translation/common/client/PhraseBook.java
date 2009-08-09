/**
 * 
 */
package de.saumya.gwt.translation.common.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class PhraseBook extends Resource<PhraseBook> {

    private final PhraseFactory factory;

    PhraseBook(final Repository repository, final PhraseBookFactory factory,
            final PhraseFactory phraseFactory) {
        super(repository, factory);
        this.factory = phraseFactory;
    }

    String            locale;
    Resources<Phrase> phrases;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "locale", this.locale);
        if (this.phrases != null) {
            this.phrases.toXml(buf);
        }
    }

    @Override
    protected void fromXml(final Element root) {
        this.locale = getString(root, "locale");
        final Element child = getChildElement(root, "phrases");
        if (child != null) {
            this.phrases = this.factory.newResources();
            this.phrases.fromXml(child);
        }
    }

    @Override
    protected String key() {
        return this.locale;
    }

    @Override
    protected void toString(final StringBuffer buf) {
        buf.append(":locale => ").append(this.locale);
        if (this.phrases != null) {
            buf.append(", :phrases => ").append(this.phrases);
        }
    }
}