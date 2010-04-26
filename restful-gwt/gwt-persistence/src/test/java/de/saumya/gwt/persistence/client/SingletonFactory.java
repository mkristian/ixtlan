/**
 * 
 */
package de.saumya.gwt.persistence.client;

public class SingletonFactory extends
        SingletonResourceFactory<Singleton> {

    public SingletonFactory(final Repository repository,
            final ResourceNotifications notification) {
        super(repository, notification);
    }

    @Override
    public String storageName() {
        return "singleton";
    }

    @Override
    public Singleton newResource() {
        return new Singleton(this.repository, this);
    }
}