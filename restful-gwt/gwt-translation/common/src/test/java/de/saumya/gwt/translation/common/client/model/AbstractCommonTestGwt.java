package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.AbstractResourceTestGwt;
import de.saumya.gwt.persistence.client.Resource;

public abstract class AbstractCommonTestGwt<E extends Resource<E>> extends
        AbstractResourceTestGwt<E> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.Common";
    }

}
