/**
 *
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.SingletonResource;
import de.saumya.gwt.session.client.models.User;
import de.saumya.gwt.session.client.models.UserFactory;

class Authentication extends SingletonResource<Authentication> {

    private final UserFactory userFactory;

    protected Authentication(final Repository repository,
            final AuthenticationFactory factory, final UserFactory userFactory) {
        super(repository, factory);
        this.userFactory = userFactory;
    }

    String login;
    String password;
    User   user;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "login", this.login);
        appendXml(buf, "password", this.password);
    }

    @Override
    protected void fromElement(final Element root) {
        this.login = null;
        this.password = null;
        this.user = this.userFactory.getChildResource(root, "user");
    }

    @Override
    public void toString(final String indent, final StringBuilder buf) {
        if (this.login != null) {
            toString(indent, buf, "login", this.login);
        }
        if (this.user != null) {
            toString(indent, buf, "user", this.user);
        }
    }

    @Override
    public String display() {
        return this.login == null ? this.user.login : this.login;
    }
}
