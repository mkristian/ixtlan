package org.dhamma.client.resource;

import java.util.ArrayList;
import java.util.List;

import org.dhamma.client.resource.Repository;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

public class RepositoryMock extends Repository {

    private final List<String> responses = new ArrayList<String>();
    final List<String>         requests  = new ArrayList<String>();
    private int                current   = 0;

    public void add(String responseText, int times) {
        for (int i = 0; i < times; i++) {
            responses.add(responseText);
        }
    }

    public void add(String responseText) {
        responses.add(responseText);
    }

    public void reset() {
        this.requests.clear();
        this.responses.clear();
        this.current = 0;
    }

    @Override
    void doIt(String method, String url, String data, RequestCallback callback) {
        this.requests.add(data);
        callback.onResponseReceived(null, new Response() {

            @Override
            public String getHeader(String header) {
                return null;
            }

            @Override
            public Header[] getHeaders() {
                return null;
            }

            @Override
            public String getHeadersAsString() {
                return null;
            }

            @Override
            public int getStatusCode() {
                return 200;
            }

            @Override
            public String getStatusText() {
                return null;
            }

            @Override
            public String getText() {
                if (responses.size() > current) {
                    return responses.get(current++);
                }
                else {
                    return null;
                }
            }
        });
    }
}
