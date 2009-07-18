/**
 * 
 */
package org.dhamma.client;

import org.dhamma.client.resource.Repository;
import org.dhamma.client.resource.ResourceFactory;

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

}