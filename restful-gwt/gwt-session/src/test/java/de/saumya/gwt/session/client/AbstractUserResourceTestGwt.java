/**
 * 
 */
package de.saumya.gwt.session.client;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.session.client.model.DomainFactory;
import de.saumya.gwt.session.client.model.GroupFactory;
import de.saumya.gwt.session.client.model.LocaleFactory;
import de.saumya.gwt.session.client.model.UserFactory;

public abstract class AbstractUserResourceTestGwt<E extends Resource<E>>
        extends AbstractResourceTestGwt<E> {

    protected LocaleFactory localeFactory;
    protected DomainFactory domainFactory;
    protected GroupFactory  groupFactory;
    protected UserFactory   userFactory;

    @Override
    protected void gwtSetUp() {
        this.localeFactory = new LocaleFactory(this.repository,
                this.notifications);
        this.domainFactory = new DomainFactory(this.repository,
                this.notifications);
        this.groupFactory = new GroupFactory(this.repository,
                this.notifications,
                this.localeFactory,
                this.domainFactory);
        this.userFactory = new UserFactory(this.repository,
                this.notifications,
                this.localeFactory,
                this.groupFactory);
        super.gwtSetUp();
    }
}