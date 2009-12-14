/**
 * 
 */
package de.saumya.gwt.persistence.client;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;

public class RestfulRequestBuilder extends RequestBuilder {
    public RestfulRequestBuilder(final String httpMethod, final String url,
            final String authenticationToken) {
        super(httpMethod, url);
        setHeader("content-type", "application/xml");
        setHeader("If-None-Match", "blahblah");
        if (authenticationToken != null) {
            setHeader("authenticity-token", authenticationToken);
        }
    }

    @Override
    public Request sendRequest(final String data, final RequestCallback callback)
            throws RequestException {
        if (data != null) {
            setHeader("content-length", "" + data.length());
        }
        return super.sendRequest(data, callback);
    }
}