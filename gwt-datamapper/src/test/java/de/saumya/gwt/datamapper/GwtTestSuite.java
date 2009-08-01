package de.saumya.gwt.datamapper;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.datamapper.client.SampleTestGwt;

public class GwtTestSuite extends GWTTestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for GWT Datamapper");
        suite.addTestSuite(SampleTestGwt.class);
        return suite;
    }
}
