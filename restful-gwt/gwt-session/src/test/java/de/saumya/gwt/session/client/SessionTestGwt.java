package de.saumya.gwt.session.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;

import de.saumya.gwt.persistence.client.GWTResourceNotification;
import de.saumya.gwt.persistence.client.RepositoryMock;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.session.client.models.ConfigurationFactory;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroup;
import de.saumya.gwt.session.client.models.UserGroupFactory;

public class SessionTestGwt extends GWTTestCase {

    protected RepositoryMock        repository;

    protected ResourceNotifications notifications;

    protected Session               session;

    protected SessionListenerMock   listener;

    protected UserFactory           userFactory;

    protected LocaleFactory         localeFactory;

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
        this.notifications = new GWTResourceNotification();
        this.repository.reset();
        final DomainFactory domainFactory = new DomainFactory(this.repository,
                this.notifications);
        this.localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        final UserGroupFactory userGroupFactory = new UserGroupFactory(this.repository,
                this.notifications,
                this.localeFactory,
                domainFactory);
        final RoleFactory roleFactory = new RoleFactory(this.repository,
                this.notifications);
        final PermissionFactory permissionFactory = new PermissionFactory(this.repository,
                this.notifications,
                roleFactory);
        this.userFactory = new UserFactory(this.repository,
                this.notifications,
                this.localeFactory,
                domainFactory,
                new GroupFactory(this.repository,
                        this.notifications,
                        userGroupFactory),
                userGroupFactory);
        this.repository.add("<authentication>" + "<user><id>1</id>"
                + "<login>dhamma</login>" + "<groups>" + "<group>"
                + "<id>1</id>" + "<name>admin</name>"
                + "<created_at>2005-07-09 17:14:48.0</created_at>"
                + "<locales>" + "<locale>" + "<id>1</id><code>en</code>"
                + "</locale>" + "</locales>" + "</group>" + "</groups>"
                + "</user>" + "<permissions>" + "<permission>"
                + "<resource>user</resource>" + "<action>create</action>"
                + "<roles>" + "<role>" + "<name>admin</name>" + "</role>"
                + "<role>" + "<name>root</name>" + "</role>" + "</roles>"
                + "</permission>" + "</permissions>" + "</authentication>");
        this.repository.add("<configuration></configuration>");
        // TODO maybe the whole mockup here is not neccessary and instead the
        // repository mock with authentication xml is sufficient
        this.session = new Session(new AuthenticationFactory(this.repository,
                this.notifications,
                permissionFactory,
                this.userFactory), new ConfigurationFactory(this.repository,
                this.notifications,
                this.userFactory,
                this.localeFactory)) {

            @Override
            void login(final String username, final String password) {
                if ("dhamma".equals(username) && "mudita".equals(password)) {
                    final Authentication authentication = this.authenticationFactory.newResource();
                    authentication.login = username;
                    final User user = SessionTestGwt.this.userFactory.newResource(1);
                    user.login = username;
                    authentication.user = user;
                    user.groups = userGroupFactory.newResources();
                    final UserGroup root = userGroupFactory.newResource(1);
                    root.name = "root";
                    root.locales = SessionTestGwt.this.localeFactory.newResources();
                    final Locale en = SessionTestGwt.this.localeFactory.newResource(123);
                    en.code = "en";
                    root.locales = SessionTestGwt.this.localeFactory.newResources();
                    root.locales.add(en);
                    user.groups.add(root);
                    doLogin(authentication);
                }
                else {
                    doAccessDenied();
                }
            }

            @Override
            void logout() {
                this.timer.cancel();
                GWT.log("log out " + this.authentication.user.login, null);
                this.authentication = null;
                fireLoggedOut();
            }

        };
        this.listener = new SessionListenerMock();
        this.session.addSessionListern(this.listener);

        this.repository.reset();

        this.repository.add("<authentications>" + "<authentication>"
                + "<user><login>dhamma</login>" + "<groups>" + "<group>"
                + "<name>admin</name>"
                + "<created_at>2005-07-09 17:14:48.0</created_at>"
                + "<locales>" + "<locale>" + "<id>1</id><code>en</code>"
                + "</locale>" + "</locales>" + "</group>" + "</groups>"
                + "</user>" + "<permissions>" + "<permission>"
                + "<resource>user</resource>" + "<action>create</action>"
                + "<roles>" + "<role>" + "<name>admin</name>" + "</role>"
                + "<role>" + "<name>root</name>" + "</role>" + "</roles>"
                + "</permission>" + "</permissions>" + "</authentication>"
                + "</authentications>");
    }

    static class SessionListenerMock implements SessionListener {

        int countSuccess      = 0;
        int countAccessDenied = 0;
        int countLogout       = 0;

        @Override
        public void onLogin() {
            this.countSuccess++;
        }

        @Override
        public void onTimeout() {
            fail();
        }

        @Override
        public void onLogout() {
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
        assertTrue(this.session.hasUser());
        assertNotNull(this.session.getUser());
    }

    public void testAccessDeniedWrongUser() {
        this.session.login("wrong", "mudita");

        assertEquals(0, this.listener.countSuccess);
        assertEquals(1, this.listener.countAccessDenied);
        assertEquals(0, this.listener.countLogout);
        assertFalse(this.session.hasUser());
        assertNull(this.session.getUser());
    }

    public void testAccessDeniedWrongPassword() {
        this.session.login("dhamma", "wrong");

        assertEquals(0, this.listener.countSuccess);
        assertEquals(1, this.listener.countAccessDenied);
        assertEquals(0, this.listener.countLogout);
        assertFalse(this.session.hasUser());
        assertNull(this.session.getUser());
    }

    public void testLogout() {
        this.session.login("dhamma", "mudita");
        this.session.logout();
        assertEquals(1, this.listener.countSuccess);
        assertEquals(0, this.listener.countAccessDenied);
        assertEquals(1, this.listener.countLogout);
        assertFalse(this.session.hasUser());
        assertNull(this.session.getUser());
    }

    public void testIsAllowed() {
        this.session.login("dhamma", "mudita");

        assertTrue(this.session.isAllowed("create", "user"));
    }

    public void testIsAllowedWithLocale() {
        this.session.login("dhamma", "mudita");
        final Locale en = this.localeFactory.newResource(1234);
        en.code = "en";
        assertTrue(this.session.isAllowed("create", "user", en));
    }

    public void testNotIsAllowed() {
        this.session.login("dhamma", "mudita");

        assertFalse(this.session.isAllowed("update", "user"));
        assertFalse(this.session.isAllowed("create", "locale"));
    }

    public void testNotIsAllowedWithLocale() {
        this.session.login("dhamma", "mudita");
        final Locale de = this.localeFactory.newResource(432);
        de.code = "de";
        assertFalse(this.session.isAllowed("create", "user", de));
        assertFalse(this.session.isAllowed("update", "user", de));
        assertFalse(this.session.isAllowed("create", "locale", de));
    }
}
