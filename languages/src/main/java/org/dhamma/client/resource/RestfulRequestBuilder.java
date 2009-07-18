/**
 * 
 */
package org.dhamma.client.resource;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

public class RestfulRequestBuilder extends RequestBuilder {
	public RestfulRequestBuilder(String httpMethod, String url) {
		super(httpMethod, url);
		setHeader("content-type", "application/xml");
	}

	public Request sendRequest(String data, RequestCallback callback)
			throws RequestException {
		if (data != null) {
			setHeader("content-length", "" + data.length());
		}
		return super.sendRequest(data, callback);
	}
}