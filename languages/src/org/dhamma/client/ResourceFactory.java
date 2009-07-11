/**
 * 
 */
package org.dhamma.client;

import org.dhamma.client.Resource.State;



public abstract class ResourceFactory<E extends Resource> {
	final Repository repository;

	public ResourceFactory(Repository repository) {
		this.repository = repository;
	}

	abstract public String storageName();

	abstract protected E newResource();

	void get(int id, Resource resource) {
		resource.state = State.TO_BE_LOADED;
		repository.get(storageName(), id, new ResourceRequestCallback(
				resource));
	}

	void all(Resources<? extends Resource> list) {
		repository.all(storageName(), new ResourceListRequestCallback(list));
	}

}