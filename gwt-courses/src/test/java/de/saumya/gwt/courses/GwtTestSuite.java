package de.saumya.gwt.courses;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.courses.client.CourseTestGwt;

public class GwtTestSuite extends GWTTestSuite {
    
    public static Test suite() {
        TestSuite suite = new TestSuite("Test for GWT Courses");
        suite.addTestSuite(CourseTestGwt.class);
        return suite;
    }
}
