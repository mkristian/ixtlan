/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class ResourceCollectionRequestCallback implements RequestCallback {

    private final ResourceCollection<? extends Resource<?>> collection;

    ResourceCollectionRequestCallback(ResourceCollection<? extends Resource<?>> collection) {
        this.collection = collection;
    }

    public void onError(Request request, Throwable exception) {
        GWT.log("error", exception);
    }

    public void onResponseReceived(Request request, Response response) {
        collection.fromXml(response.getText());
    }
}
