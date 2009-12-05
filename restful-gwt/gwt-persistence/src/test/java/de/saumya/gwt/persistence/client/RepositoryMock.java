package de.saumya.gwt.persistence.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.http.client.Header;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import de.saumya.gwt.persistence.client.Repository;

public class RepositoryMock extends Repository {

    private final List<String> responses = new ArrayList<String>();
    public final List<String>  requests  = new ArrayList<String>();
    private int                current   = 0;

    public void addXmlResponse(final String responseText) {
        addXmlResponse(responseText, 1);
    }

    public void addXmlResponse(final String responseText, final int times) {
        for (int i = 0; i < times; i++) {
            this.responses.add(responseText);
        }
    }

    public void add(final String responseText) {
        this.responses.add(responseText);
    }

    public void reset() {
        this.requests.clear();
        this.responses.clear();
        this.current = 0;
    }

    @Override
    void doIt(final String method, final String url, final String data,
            final RequestCallback callback) {
        this.requests.add(data);
        callback.onResponseReceived(null, new Response() {

            @Override
            public String getHeader(final String header) {
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
                return RepositoryMock.this.responses.size() == 0 ? 404 : 200;
            }

            @Override
            public String getStatusText() {
                return null;
            }

            @Override
            public String getText() {
                if (RepositoryMock.this.responses.size() > RepositoryMock.this.current) {
                    return RepositoryMock.this.responses.get(RepositoryMock.this.current++);
                }
                else {
                    return null;
                }
            }
        });
    }
}
