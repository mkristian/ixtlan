/**
 * 
 */
package de.saumya.gwt.gettext.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;

public class WordFactory extends ResourceFactory<Word> {

    public WordFactory(Repository repository) {
        super(repository);
    }

    public String storageName() {
        return "word";
    }

    protected Word newResource() {
        return new Word(repository, this);
    }

}