package de.saumya.gwt.datamapper.client;

import com.google.gwt.junit.client.GWTTestCase;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
abstract public class AbstractResourceTestGwt<T extends Resource<T>> extends GWTTestCase {

    protected RepositoryMock                          repository;
    protected CountingResourceChangeListener<T>  countingResourceListener;
    protected CountingResourcesChangeListener<T> countingResourcesListener;
    //private Resource<T> resource;
    
    protected void gwtSetUp() {
        repository = new RepositoryMock();
        countingResourceListener = new CountingResourceChangeListener<T>();
        countingResourcesListener = new CountingResourcesChangeListener<T>();
        
        resourceSetUp();

        countingResourceListener.reset();
        countingResourcesListener.reset();
        repository.reset();
    }

    abstract protected void resourceSetUp();
    
    abstract public void testCreate();

    abstract public void testRetrieve();
    
    abstract public void testRetrieveAll();
    
    abstract public void testUpdate();
    
    abstract public void testDelete();
}
