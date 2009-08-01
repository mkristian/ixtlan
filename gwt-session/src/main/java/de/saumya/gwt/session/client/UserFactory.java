package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.Repository;
import de.saumya.gwt.datamapper.client.ResourceFactory;
import de.saumya.gwt.datamapper.client.Resources;

public class UserFactory extends ResourceFactory<User>{

    private final LocaleFactory localeFactory;
    private final RoleFactory roleFactory;
    
    public UserFactory(Repository repository, LocaleFactory localeFactory, RoleFactory roleFactory) {
        super(repository);
        this.localeFactory = localeFactory;
        this.roleFactory = roleFactory;
    }

    @Override
    public String storageName() {
        return "user";
    }

    @Override
    public User newResource() {
        return new User(repository, this);
    }
    
    public Locale newLocaleResource() {
        return localeFactory.newResource();
    }

    public Resources<Role> newRoleResources() {
        return roleFactory.newResources();
    }

}
