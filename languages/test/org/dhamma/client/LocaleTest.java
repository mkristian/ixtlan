package org.dhamma.client;

import org.dhamma.client.Resource.State;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class LocaleTest extends GWTTestCase {

	/**
	 * Must refer to a valid module that sources this class.
	 */
	public String getModuleName() {
		return "org.dhamma.Translations";
	}

	private Locale locale;
	private LocaleFactory factory;
	private RepositoryMock repository;
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
		assertEquals(State.UP_TO_DATE, locale.state);
		assertEquals(123, locale.id);
	}

	public void testRetrieve() {
		repository.add("<locale><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>", 1);
		Locale l = factory.get(1);
		assertEquals(State.UP_TO_DATE, l.state);
		assertEquals(locale.toString(), l.toString());
	}

	public void testRetrieveAll() {
		repository.reset();
		repository.add("<locales type='array'>" +
				"<locale><id>123</id><created_at>2009-07-09T17:14:48+05:30</created_at></locale>" +
				"<locale><id>123</id><created_at>2009-07-01T17:24:48+05:30</created_at></locale>" +
				"</locales>", 1);
		Resources<Locale> ls = factory.all();
		for(Locale l : ls){
			assertEquals(State.UP_TO_DATE, l.state);
			assertEquals(locale.toString(), l.toString());
		}
	}

	public void testUpdate() {
		locale.country = null;
		locale.save();	
		assertEquals(State.UP_TO_DATE, locale.state);
	}

	public void testDelete() {
		this.locale.destroy();
		assertEquals(State.DELETED, this.locale.state);
	}
}
