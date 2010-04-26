package de.saumya.gwt.persistence.client;

public interface OneTimeResourceChangeListener<E extends AbstractResource<E>>
        extends ResourceChangeListener<E> {

    // @Override
    // public void onChange(final AbstractResource<?> resource) {
    // resource.removeResourceChangeListener(this);
    // onResourceChange(resource);
    // }
    //
    // @Override
    // public void onError(final int status, final String errorMessage,
    // final AbstractResource<?> resource) {
    // resource.removeResourceChangeListener(this);
    // onResourceError(status, errorMessage, resource);
    // }
    //
    // abstract protected void onResourceChange(AbstractResource<?> resource);
    //
    // abstract protected void onResourceError(int status, String errorMessage,
    // AbstractResource<?> resource);

}
