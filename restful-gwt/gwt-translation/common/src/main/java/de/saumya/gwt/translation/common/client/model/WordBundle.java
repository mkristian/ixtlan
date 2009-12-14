/**
 * 
 */
package de.saumya.gwt.translation.common.client.model;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.Resources;

public class WordBundle extends Resource<WordBundle> {

    private final WordFactory wordFactory;

    WordBundle(final Repository repository, final WordBundleFactory factory,
            final WordFactory wordFactory) {
        super(repository, factory, null);
        this.wordFactory = wordFactory;
    }

    public String          locale;
    public Resources<Word> words;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "locale", this.locale);
        append(buf, "words", this.words);
    }

    @Override
    protected void fromXml(final Element root) {
        this.locale = getString(root, "locale");
        this.words = this.wordFactory.getChildResources(root, "words");
    }

    @Override
    public String key() {
        return this.locale;
    }

    @Override
    protected void toString(final StringBuffer buf) {
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