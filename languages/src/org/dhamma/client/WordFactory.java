/**
 * 
 */
package org.dhamma.client;

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