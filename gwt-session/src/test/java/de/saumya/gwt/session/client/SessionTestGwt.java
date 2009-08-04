package de.saumya.gwt.session.client;

import com.google.gwt.junit.client.GWTTestCase;

import de.saumya.gwt.datamapper.client.RepositoryMock;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class SessionTestGwt extends GWTTestCase {

    protected RepositoryMock      repository;

    protected Session             session;

    protected SessionListenerMock listener;

    protected UserFactory         userFactory;

    protected LocaleFactory       localeFactory;

    /**
     * Must refer to a valid module that sources this class.
     */
    @Override
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    @Override
    protected void gwtSetUp() {
        this.repository = new RepositoryMock();
        this.repository.reset();
        final VenueFactory venueFactory = new VenueFactory(this.repository);
        this.localeFactory = new LocaleFactory(this.repository);
        final RoleFactory roleFactory = new RoleFactory(this.repository,
                this.localeFactory,
                venueFactory);
        final PermissionFactory permissionFactory = new PermissionFactory(this.repository,
                roleFactory);
        this.userFactory = new UserFactory(this.repository,
                this.localeFactory,
                roleFactory);
        this.repository.add("<permissions>" + "<permission>"
                + "<resource_name>user</resource_name>"
                + "<action>create</action>" + "<roles>" + "<role>"
                + "<name>admin</name>"
                + "<created_at>2005-07-09 17:14:48.0</created_at>" + "</role>"
                + "</roles>" + "</permission>" + "</permissions>");
        this.session = new Session(venueFactory,
                permissionFactory,
                roleFactory,
                this.userFactory);
        this.listener = new SessionListenerMock();
        this.session.addSessionListern(this.listener);
    }

    static class SessionListenerMock implements SessionListener {

        int countSuccess      = 0;
        int countAccessDenied = 0;
        int countLogout       = 0;

        @Override
        public void onSuccessfulLogin() {
            this.countSuccess++;
        }

        @Override
        public void onSessionTimeout() {
            fail();
        }

        @Override
        public void onLoggedOut() {
            this.countLogout++;
        }

        @Override
        public void onAccessDenied() {
            this.countAccessDenied++;
        }
    }

    public void testLogin() {
        this.session.login("dhamma", "mudita");

        assertEquals(1, this.listener.countSuccess);
        assertEquals(0, this.listener.countAccessDenied);
        assertEquals(0, this.listener.countLogout);
        assertNotNull(this.session.sessionToken());
        assertTrue(this.session.hasUser());
        assertNotNull(this.session.getUser());
    }

    public void testAccessDeniedWrongUser() {
        this.session.login("wrong", "mudita");

        assertEquals(0, this.listener.countSuccess);
        assertEquals(1, this.listener.countAccessDenied);
        assertEquals(0, this.listener.countLogout);
        assertNull(this.session.sessionToken());
        assertFalse(this.session.hasUser());
        assertNull(this.session.getUser());
    }

    public void testAccessDeniedWrongPassword() {
        this.session.login("dhamma", "wrong");

        assertEquals(0, this.listener.countSuccess);
        assertEquals(1, this.listener.countAccessDenied);
        assertEquals(0, this.listener.countLogout);
        assertNull(this.session.sessionToken());
        assertFalse(this.session.hasUser());
        assertNull(this.session.getUser());
    }

    public void testLogout() {
        this.session.login("dhamma", "mudita");
        this.session.logout();
        assertEquals(1, this.listener.countSuccess);
        assertEquals(0, this.listener.countAccessDenied);
        assertEquals(1, this.listener.countLogout);
        assertNull(this.session.sessionToken());
        assertFalse(this.session.hasUser());
        assertNull(this.session.getUser());
    }

    public void testIsAllowed() {
        this.session.login("dhamma", "mudita");

        assertTrue(this.session.isAllowed("create", this.userFactory, "admin"));
    }

    public void testNotIsAllowed() {
        this.session.login("dhamma", "mudita");

        assertFalse(this.session.isAllowed("update", this.userFactory, "admin"));
        assertFalse(this.session.isAllowed("create", this.userFactory, "master"));
        assertFalse(this.session.isAllowed("create",
                                           this.localeFactory,
                                           "admin"));
    }
}
