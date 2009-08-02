package de.saumya.gwt.session;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.session.client.LocaleTestGwt;
import de.saumya.gwt.session.client.PermissionTestGwt;
import de.saumya.gwt.session.client.RoleTestGwt;
import de.saumya.gwt.session.client.SessionTestGwt;
import de.saumya.gwt.session.client.UserTestGwt;
import de.saumya.gwt.session.client.VenueTestGwt;

public class GwtTestSuite extends GWTTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for GWT Session");
        suite.addTestSuite(LocaleTestGwt.class);
        suite.addTestSuite(VenueTestGwt.class);
        suite.addTestSuite(RoleTestGwt.class);
        suite.addTestSuite(UserTestGwt.class);
        suite.addTestSuite(PermissionTestGwt.class);
        suite.addTestSuite(SessionTestGwt.class);
        return suite;
    }
}
