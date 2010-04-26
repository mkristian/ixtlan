package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AnonymousResource;
import de.saumya.gwt.persistence.client.AnonymousResourceTestGwt;

public abstract class AbstractCommonAnonymousTestGwt<E extends AnonymousResource<E>>
        extends AnonymousResourceTestGwt<E> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.Common";
    }

}
