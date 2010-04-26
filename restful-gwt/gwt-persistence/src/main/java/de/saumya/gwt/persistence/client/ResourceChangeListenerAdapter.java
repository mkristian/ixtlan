package de.saumya.gwt.persistence.client;

public abstract class ResourceChangeListenerAdapter<E extends AbstractResource<E>>
        implements ResourceChangeListener<E> {

    @Override
    public void onError(final int status, final String errorMessage,
            final E resource) {
    }
}
