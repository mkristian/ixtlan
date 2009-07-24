package de.saumya.gwt.session;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class GwtTestSuite extends GWTTestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for GWT Session");
        //        suite.addTestSuite(LocaleTestGwt.class);
        return suite;
    }
}
