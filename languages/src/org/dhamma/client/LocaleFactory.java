/**
 * 
 */
package org.dhamma.client;

public class LocaleFactory extends ResourceFactory<Locale> {

	public LocaleFactory(Repository repository) {
		super(repository);
	}

	public String storageName() {
		return "locale";
	}

	protected Locale newResource() {
		return new Locale(repository, this);
	}

	public Locale get(int id) {
		Locale locale = new Locale(repository, this);
		get(id, locale);
		return locale;
	}

	public Resources<Locale> all() {
		Resources<Locale> list = new Resources<Locale>(this);
		all(list);
		return list;
	}
}