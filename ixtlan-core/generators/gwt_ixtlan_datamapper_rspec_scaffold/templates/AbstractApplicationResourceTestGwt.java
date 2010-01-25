package <%= package %>.models;

import de.saumya.gwt.persistence.client.Resource;

abstract class AbstractApplicationResourceTestGwt<E extends Resource<E>>
        extends AbstractUserResourceTestGwt<E> {

    @Override
    public String getModuleName() {
        return "<%= package %>.Application";
    }
}