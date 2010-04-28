/**
 *
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class ResourceCollectionRequestCallback implements RequestCallback {

    private final ResourceCollection<? extends AbstractResource<?>> collection;

    ResourceCollectionRequestCallback(
            final ResourceCollection<? extends AbstractResource<?>> collection) {
        this.collection = collection;
    }

    public void onError(final Request request, final Throwable exception) {
        GWT.log("error", exception);
    }

    public void onResponseReceived(final Request request,
            final Response response) {
        if (response.getStatusCode() < 300) {
            // all kind of OK status
            this.collection.fromXml(response.getText());
        }
        else if (response.getStatusCode() == 304) {
            // UNMODIFIED
            this.collection.fireResourcesLoadedEvents();
        }
    }
}
