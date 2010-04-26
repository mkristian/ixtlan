/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import de.saumya.gwt.persistence.client.AbstractResource.State;

public class ResourceRequestCallback<E extends AbstractResource<E>> implements
        RequestCallback {

    private final AbstractResource<E>        resource;

    private final AbstractResourceFactory<E> factory;

    ResourceRequestCallback(final AbstractResource<E> resource,
            final AbstractResourceFactory<E> factory) {
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
            final String message = this.resource.state.message;
            switch (this.resource.state) {
            case TO_BE_CREATED:
                // if (this.factory.all != null) {
                // // TODO remove cast
                // this.factory.all.addResource((E) this.resource);
                // }
            case TO_BE_LOADED:
            case TO_BE_UPDATED:
                this.resource.fromXml(response.getText());
                this.resource.state = State.UP_TO_DATE;
                GWT.log(this.resource.toString(), null);
                break;
            case TO_BE_DELETED:
                this.resource.state = State.DELETED;
                this.factory.removeFromCache((E) this.resource);
                break;
            }
            this.resource.fireResourceChangeEvents(message);
        }
        else if (response.getStatusCode() == 304) {
            final String message = this.resource.state.message;
            this.resource.state = State.UP_TO_DATE;
            this.resource.fireResourceChangeEvents(message);
        }
        else {
            switch (response.getStatusCode()) {
            case 404:
                this.resource.state = State.NEW;
                break;
            case 409:
                this.resource.state = State.STALE;
                break;
            case 412:
                break;
            }

            this.resource.fireResourceErrorEvents(response.getStatusCode(),
                                                  response.getStatusText());
        }
    }
}