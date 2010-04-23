/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceWithId;

public class WordBundle extends ResourceWithId<WordBundle> {

    private final WordFactory       wordFactory;
    private final WordBundleFactory factory;

    WordBundle(final Repository repository, final WordBundleFactory factory,
            final WordFactory wordFactory, final int id) {
        super(repository, factory, id);
        this.wordFactory = wordFactory;
        this.factory = factory;
    }

    public String                   locale;
    public ResourceCollection<Word> words;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "locale", this.locale);
        appendXml(buf, "words", this.words);
    }

    @Override
    protected void fromXml(final Element root) {
        this.id = this.factory.nextId();
        this.locale = getString(root, "locale");
        this.words = this.wordFactory.getChildResourceCollection(root, "words");
    }

    @Override
    public void toString(final StringBuilder buf) {
        buf.append(":locale => ").append(this.locale);
        if (this.words != null) {
            buf.append(", :words => ").append(this.words);
        }
    }

    @Override
    public String display() {
        return new StringBuffer("wordbundle(").append(this.locale)
                .append(")")
                .toString();
    }
}