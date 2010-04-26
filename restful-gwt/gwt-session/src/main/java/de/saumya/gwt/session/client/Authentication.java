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
    String token;
    User   user;

    @Override
    protected void appendXml(final StringBuilder buf) {
        appendXml(buf, "login", this.login);
        appendXml(buf, "password", this.password);
    }

    @Override
    protected void fromElement(final Element root) {
        this.token = getString(root, "token");
        this.login = null;
        this.password = null;
        System.out.println(root);
        this.user = this.userFactory.getChildResource(root, "user");
    }

    @Override
    public void toString(final StringBuilder buf) {
        if (this.login != null) {
            buf.append(":login => ").append(this.login);
        }
        if (this.token != null) {
            buf.append(":token => ").append(this.token);
            buf.append(", :user => ");
            buf.append(this.user.toString());
        }
    }

    @Override
    public String display() {
        return this.login == null ? this.user.login : this.login;
    }
}
