/**
 * 
 */
package de.saumya.gwt.translation.common.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

class WordBundle extends Resource<WordBundle> {

    private final WordFactory factory;

    WordBundle(final Repository repository, final WordBundleFactory factory,
            final WordFactory wordFactory) {
        super(repository, factory);
        this.factory = wordFactory;
    }

    String          locale;
    Resources<Word> words;

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "locale", this.locale);
        if (this.words != null) {
            this.words.toXml(buf);
        }
        else {
            // TODO something better
            buf.append("<words></words>");
        }
    }

    @Override
    protected void fromXml(final Element root) {
        this.locale = getString(root, "locale");
        this.words = this.factory.newResources();
        final Element child = getChildElement(root, "words");
        if (child != null) {
            this.words.fromXml(child);
        }
    }

    @Override
    protected String key() {
        return this.locale;
    }

    @Override
    protected void toString(final StringBuffer buf) {
        buf.append(":locale => ").append(this.locale);
        if (this.words != null) {
            buf.append(", :words => ").append(this.words);
        }
    }
}