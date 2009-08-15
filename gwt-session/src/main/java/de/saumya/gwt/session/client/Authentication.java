/**
 * 
 */
package de.saumya.gwt.session.client;

import com.google.gwt.xml.client.Element;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.Resource;

class Authentication extends Resource<Authentication> {

    private final UserFactory userFactory;

    protected Authentication(final Repository repository,
            final AuthenticationFactory factory, final UserFactory userFactory) {
        super(repository, factory);
        this.userFactory = userFactory;
    }

    public String login;
    public String password;
    public String token;
    public User   user;

    @Override
    public String key() {
        return this.token;
    }

    @Override
    protected void appendXml(final StringBuffer buf) {
        append(buf, "login", this.login);
        append(buf, "password", this.password);
    }

    @Override
    protected void fromXml(final Element root) {
        this.token = getString(root, "token");
        this.login = null;
        this.password = null;
        this.user = this.userFactory.getChildResource(root, "user");
    }

    @Override
    public void toString(final StringBuffer buf) {
        if (this.login != null) {
            buf.append(":login => ").append(this.login);
        }
        if (this.token != null) {
            buf.append(":token => ").append(this.token);
            buf.append(", :user => ");
            buf.append(this.user.toString());
        }
    }
}