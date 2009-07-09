/**
 * 
 */
package org.dhamma.client;



public abstract class ResourceFactory {
	final Repository repository;

	public ResourceFactory(Repository repository) {
		this.repository = repository;
	}

	abstract public String storageName();

	abstract protected Resource newResource();

	void get(int id, Resource resource) {
		repository.get(storageName(), id, new ResourceRequestCallback(
				resource));
	}

	void all(ResourceList<? extends Resource> list) {
		repository.all(storageName(), new ResourceListRequestCallback(list));
	}

}