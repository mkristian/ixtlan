/**
 * 
 */
package org.dhamma.client.resource;

import org.dhamma.client.resource.Resource.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class ResourceRequestCallback implements RequestCallback {

    private final Resource<? extends Resource<?>> resource;

    ResourceRequestCallback(Resource<? extends Resource<?>> resource) {
        this.resource = resource;
    }

    public void onError(Request request, Throwable exception) {
        GWT.log("error", exception);
    }

    public void onResponseReceived(Request request, Response response) {
        GWT.log(resource.state + " " + resource.toString(), null);
        switch (resource.state) {
        case TO_BE_CREATED:
        case TO_BE_LOADED:
            resource.fromXml(response.getText());
        case TO_BE_UPDATED:
            resource.state = State.UP_TO_DATE;
            break;
        case TO_BE_DELETED:
            resource.state = State.DELETED;
            break;
        }
        resource.fireResourceChangeEvents();
    }
}