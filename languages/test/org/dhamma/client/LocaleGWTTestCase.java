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
public class LocaleGWTTestCase extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "org.dhamma.Translations";
	}

	private RepositoryMock repository;
	private Locale locale;
	private LocaleFactory factory;
	protected void gwtSetUp() {
		repository = new RepositoryMock();
		factory = new LocaleFactory(repository);
		locale = new Locale(repository, factory);

		locale.country = "DE";
		locale.language = "en";
		repository.add("<locale><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>", 1);
		locale.save();
		repository.reset();
	}

	public void testCreate() {
		assertTrue(locale.isUptodate());
		assertEquals(123, locale.id);
	}

	public void testRetrieve() {
		repository.add("<locale><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>", 1);
		final boolean changed[] = {false};
		Resource<Locale> l = factory.get(1, new ResourceChangeListener<Locale>() {
			
			@Override
			public void onChange(Locale resource) {
				changed[0] = true;
			}
		});
		assertTrue(changed[0]);
		assertTrue(locale.isUptodate());
		assertEquals(locale.toString(), l.toString());
	}

	public void testRetrieveAll() {
		repository.reset();
		repository.add("<locales type='array'>" +
				"<locale><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>" +
				"<locale><id>123</id><created_at>2009-07-01T17:24:48+05:30</created_at></locale>" +
				"</locales>", 1);
		final int changedCount[] = {0};
		Resources<Locale> ls = factory.all(new ResourcesChangeListener<Locale>() {

			@Override
			public void onChange(Resources<Locale> resources, Locale resource) {
				changedCount[0]++;
			}
		});
		assertEquals(2, changedCount[0]);
		for(Resource<Locale> l : ls){
			assertTrue(locale.isUptodate());
			assertEquals(locale.toString(), l.toString());
		}
	}

	public void testUpdate() {
		locale.country = null;
		locale.save();	
		assertTrue(locale.isUptodate());
	}

	public void testDelete() {
		this.locale.destroy();
		assertTrue(locale.isDeleted());
	}
}
