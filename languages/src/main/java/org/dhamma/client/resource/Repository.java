/**
 * 
 */
package org.dhamma.client.resource;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

public class Repository {

	private final String url = GWT.getHostPageBaseURL();

	void all(String resourceName, RequestCallback callback) {
		doIt("GET", url + resourceName + ".xml", null, callback);
	}

	void get(String resourceName, int id, RequestCallback callback) {
		doIt("GET", url + resourceName + "/" + id + ".xml", null, callback);
	}

	void post(Resource<? extends Resource<?>> resource, RequestCallback callback) {
		doIt("POST", url + resource.storageName + ".xml", resource.toXml(),
				callback);
	}

	void put(Resource<? extends Resource<?>> resource, RequestCallback callback) {
		doIt("PUT", url + resource.storageName + "/" + resource.key()
				+ ".xml", resource.toXml(), callback);
	}

	void delete(Resource<? extends Resource<?>> resource, RequestCallback callback) {
		doIt("DELETE", url + resource.storageName + "/" + resource.key()
				+ ".xml", null, callback);
	}

	void doIt(String method, String url, String data,
			RequestCallback callback) {
		RequestBuilder builder = new RestfulRequestBuilder(method, url);
		try {
			builder.sendRequest(data, callback);
		} catch (RequestException e) {
			GWT.log("TODO", e);
		}
	}
}