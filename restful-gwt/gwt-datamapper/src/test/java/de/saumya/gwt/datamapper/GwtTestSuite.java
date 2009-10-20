package de.saumya.gwt.datamapper;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.datamapper.client.CacheTestGwt;
import de.saumya.gwt.datamapper.client.SampleTestGwt;
import de.saumya.gwt.datamapper.client.SingletonTestGwt;

public class GwtTestSuite extends GWTTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for GWT Datamapper");
        suite.addTestSuite(SampleTestGwt.class);
        suite.addTestSuite(CacheTestGwt.class);
        suite.addTestSuite(SingletonTestGwt.class);
        return suite;
    }
}
