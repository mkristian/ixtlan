package org.dhamma.client;

import org.dhamma.client.resource.RepositoryMock;
import org.dhamma.client.resource.Resource;
import org.dhamma.client.resource.ResourceChangeListener;
import org.dhamma.client.resource.Resources;
import org.dhamma.client.resource.ResourcesChangeListener;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class WordGWTTestCase extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "org.dhamma.Translations";
	}

	private RepositoryMock repository;
	private Word word;
	private WordFactory factory;
	protected void gwtSetUp() {
		repository = new RepositoryMock();
		factory = new WordFactory(repository);
		word = new Word(repository, factory);

		word.code = "en";
		word.text = "english";
		repository.add("<word><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></word>", 1);
		word.save();
		repository.reset();
	}

	public void testCreate() {
		assertTrue(word.isUptodate());
		assertEquals(123, word.id);
	}

	public void testRetrieve() {
		repository.add("<word><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></word>", 1);
		final boolean changed[] = {false};
		Resource<Word> l = factory.get(1, new ResourceChangeListener<Word>() {
			
			@Override
			public void onChange(Word resource) {
				changed[0] = true;
			}
		});
		assertTrue(changed[0]);
		assertTrue(word.isUptodate());
		assertEquals(word.toString(), l.toString());
	}

	public void testRetrieveAll() {
		repository.reset();
		repository.add("<words type='array'>" +
				"<word><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></word>" +
				"<word><id>123</id><created_at>2009-07-01T17:24:48+05:30</created_at></word>" +
				"</words>", 1);
		final int changedCount[] = {0};
		Resources<Word> ls = factory.all(new ResourcesChangeListener<Word>() {
			
			@Override
			public void onChange(Resources<Word> resources, Word resource) {
				changedCount[0]++;
			}
		});
		assertEquals(2, changedCount[0]);
		for(Resource<Word> l : ls){
			assertTrue(word.isUptodate());
			assertEquals(word.toString(), l.toString());
		}
	}

	public void testUpdate() {
		word.text = null;
		word.save();	
		assertTrue(word.isUptodate());
	}

	public void testDelete() {
		this.word.destroy();
		assertTrue(word.isDeleted());
	}
}
