/**
 * 
 */
package org.dhamma.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class ResourceRequestCallback implements RequestCallback {

	private final Resource resource;

	ResourceRequestCallback(Resource resource) {
		this.resource = resource;
	}

	public void onError(Request request, Throwable exception) {
		GWT.log("error", exception);
	}

	public void onResponseReceived(Request request, Response response) {
		if(response.getText() != null && response.getText().trim().length() > 0){
			GWT.log(response.getText(), null);
			resource.fromXml(response.getText());
		}
		resource.fireResourceChangeEvents();
	}
}