/**
 *
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.AnonymousResource;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceCollection;

public class WordBundle extends AnonymousResource<WordBundle> {

    private final WordFactory wordFactory;

    WordBundle(final Repository repository, final WordBundleFactory factory,
            final WordFactory wordFactory) {
        super(repository, factory);
        this.wordFactory = wordFactory;
    }

    public String                   locale;
    public ResourceCollection<Word> words;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "locale", this.locale);
        appendXml(buf, "words", this.words);
    }

    @Override
    protected void fromElement(final Element root) {
        this.locale = getString(root, "locale");
        this.words = this.wordFactory.getChildResourceCollection(root, "words");
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        toString(indent, buf, "locale", this.locale);
        toString(indent, buf, "words", this.words);
    }

    @Override
    public String display() {
        return new StringBuffer("wordbundle(").append(this.locale)
                .append(")")
                .toString();
    }
}
