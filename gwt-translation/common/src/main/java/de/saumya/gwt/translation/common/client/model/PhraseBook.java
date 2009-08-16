/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

public class PhraseBook extends Resource<PhraseBook> {

    private final PhraseFactory phraseFactory;

    PhraseBook(final Repository repository, final PhraseBookFactory factory,
            final PhraseFactory phraseFactory) {
        super(repository, factory);
        this.phraseFactory = phraseFactory;
    }

    public String            locale;
    public Resources<Phrase> phrases;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "locale", this.locale);
        append(buf, "phrases", this.phrases);
    }

    @Override
    protected void fromXml(final Element root) {
        this.locale = getString(root, "locale");
        this.phrases = this.phraseFactory.getChildResources(root, "phrases");
    }

    @Override
    public String key() {
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