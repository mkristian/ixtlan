/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class ResourceListRequestCallback implements RequestCallback {

    private final ResourceCollection<? extends Resource<?>> list;

    ResourceListRequestCallback(ResourceCollection<? extends Resource<?>> list) {
        this.list = list;
    }

    public void onError(Request request, Throwable exception) {
        GWT.log("error", exception);
    }

    public void onResponseReceived(Request request, Response response) {
        list.fromXml(response.getText());
    }
}
