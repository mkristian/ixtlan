/**
 * 
 */
package de.saumya.gwt.datamapper.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import de.saumya.gwt.datamapper.client.Resource.State;

public class ResourceRequestCallback<E extends Resource<E>> implements
        RequestCallback {

    private final Resource<E>        resource;

    private final ResourceFactory<E> factory;

    ResourceRequestCallback(final Resource<E> resource,
            final ResourceFactory<E> factory) {
        this.resource = resource;
        this.factory = factory;
    }

    public void onError(final Request request, final Throwable exception) {
        GWT.log("error", exception);
    }

    @SuppressWarnings("unchecked")
    public void onResponseReceived(final Request request,
            final Response response) {
        if (response.getStatusCode() < 300) {
            GWT.log(this.resource.state + " " + this.resource.toString(), null);
            switch (this.resource.state) {
            case TO_BE_CREATED:
            case TO_BE_LOADED:
            case TO_BE_UPDATED:
                this.resource.fromXml(response.getText());
                this.resource.state = State.UP_TO_DATE;
                break;
            case TO_BE_DELETED:
                this.resource.state = State.DELETED;
                this.factory.removeFromCache((E) this.resource);
                break;
            }
            this.resource.fireResourceChangeEvents();
        }
        else {
            GWT.log("TODO status >= 300", null);
        }
    }
}