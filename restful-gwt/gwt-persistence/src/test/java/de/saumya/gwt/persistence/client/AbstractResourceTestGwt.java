package de.saumya.gwt.persistence.client;

import com.google.gwt.junit.client.GWTTestCase;

abstract public class AbstractResourceTestGwt<T extends AbstractResource<T>>
        extends GWTTestCase {

    protected RepositoryMock                     repository;
    protected ResourceNotifications              notifications;
    protected CountingResourceChangeListener<T>  countingResourceListener;
    protected CountingResourcesChangeListener<T> countingResourcesListener;

    protected AbstractResource<T>                resource;
    protected AbstractResourceFactory<T>         factory;

    protected void gwtSetUp(final AbstractResourceFactory<T> factory) {
        this.notifications = new GWTResourceNotification();
        this.factory = factory;

        this.countingResourceListener = new CountingResourceChangeListener<T>();
        this.countingResourcesListener = new CountingResourcesChangeListener<T>();

        this.resource = resourceSetUp();

        this.countingResourceListener.reset();
        this.countingResourcesListener.reset();
    }

    abstract protected String resource1Xml();

    abstract protected AbstractResource<T> resourceSetUp();
}
