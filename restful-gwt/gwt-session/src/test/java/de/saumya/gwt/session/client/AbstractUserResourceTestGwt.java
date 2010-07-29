/**
 *
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceTestGwt;
import de.saumya.gwt.session.client.models.DomainFactory;
import de.saumya.gwt.session.client.models.GroupFactory;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.session.client.models.UserFactory;
import de.saumya.gwt.session.client.models.UserGroupFactory;

public abstract class AbstractUserResourceTestGwt<E extends Resource<E>>
        extends ResourceTestGwt<E> {

    protected LocaleFactory    localeFactory;
    protected DomainFactory    domainFactory;
    protected UserGroupFactory userGroupFactory;
    protected UserFactory      userFactory;

    @Override
    protected void gwtSetUp() {
        this.localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        this.domainFactory = new DomainFactory(this.repository,
                this.notifications);
        this.userGroupFactory = new UserGroupFactory(this.repository,
                this.notifications,
                this.localeFactory,
                this.domainFactory);
        this.userFactory = new UserFactory(this.repository,
                this.notifications,
                this.localeFactory,
                this.domainFactory,
                new GroupFactory(this.repository,
                        this.notifications,
                        this.userGroupFactory),
                this.userGroupFactory);
        super.gwtSetUp();
    }
}
