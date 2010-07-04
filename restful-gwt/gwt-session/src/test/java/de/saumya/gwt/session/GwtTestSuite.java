package de.saumya.gwt.session;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.session.client.AuthenticationTestGwt;
import de.saumya.gwt.session.client.PermissionTestGwt;
import de.saumya.gwt.session.client.RoleTestGwt;
import de.saumya.gwt.session.client.SessionTestGwt;
import de.saumya.gwt.session.client.models.AuditTestGwt;
import de.saumya.gwt.session.client.models.DomainTestGwt;
import de.saumya.gwt.session.client.models.GroupTestGwt;
import de.saumya.gwt.session.client.models.LocaleTestGwt;
import de.saumya.gwt.session.client.models.UserGroupTestGwt;
import de.saumya.gwt.session.client.models.UserTestGwt;

public class GwtTestSuite extends GWTTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for GWT Session");
        suite.addTestSuite(LocaleTestGwt.class);
        suite.addTestSuite(DomainTestGwt.class);
        suite.addTestSuite(RoleTestGwt.class);
        suite.addTestSuite(GroupTestGwt.class);
        suite.addTestSuite(UserGroupTestGwt.class);
        suite.addTestSuite(UserTestGwt.class);
        suite.addTestSuite(PermissionTestGwt.class);
        suite.addTestSuite(SessionTestGwt.class);
        suite.addTestSuite(AuthenticationTestGwt.class);
        suite.addTestSuite(AuditTestGwt.class);
        return suite;
    }
}
