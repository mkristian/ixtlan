package de.saumya.gwt.persistence;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.persistence.client.CacheTestGwt;
import de.saumya.gwt.persistence.client.SampleTestGwt;
import de.saumya.gwt.persistence.client.SingletonCacheTestGwt;

public class GwtTestSuite extends GWTTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for GWT Persistence");
        suite.addTestSuite(SampleTestGwt.class);
        suite.addTestSuite(CacheTestGwt.class);
        suite.addTestSuite(SingletonCacheTestGwt.class);
        return suite;
    }
}
