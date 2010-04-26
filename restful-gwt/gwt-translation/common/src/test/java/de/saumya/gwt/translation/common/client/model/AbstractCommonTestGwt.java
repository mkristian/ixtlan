package de.saumya.gwt.translation.common.client.model;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceTestGwt;

public abstract class AbstractCommonTestGwt<E extends Resource<E>> extends
        ResourceTestGwt<E> {

    @Override
    public String getModuleName() {
        return "de.saumya.gwt.translation.common.Common";
    }

}
