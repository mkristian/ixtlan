/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceWithId;

public class PhraseBook extends ResourceWithId<PhraseBook> {

    private final PhraseBookFactory factory;
    private final PhraseFactory     phraseFactory;

    PhraseBook(final Repository repository, final PhraseBookFactory factory,
            final PhraseFactory phraseFactory, final int id) {
        super(repository, factory, id);
        this.factory = factory;
        this.phraseFactory = phraseFactory;
    }

    public String                     locale;
    public ResourceCollection<Phrase> phrases;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "locale", this.locale);
        appendXml(buf, "phrases", this.phrases);
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = this.factory.nextId();
        this.locale = getString(root, "locale");
        this.phrases = this.phraseFactory.getChildResourceCollection(root,
                                                                     "phrases");
    }

    @Override
    public String key() {
        return this.locale;
    }

    @Override
    public void toString(final StringBuilder buf) {
        buf.append(":locale => ").append(this.locale);
        if (this.phrases != null) {
            buf.append(", :phrases => ").append(this.phrases);
        }
    }

    @Override
    public String display() {
        return this.locale;
    }

}