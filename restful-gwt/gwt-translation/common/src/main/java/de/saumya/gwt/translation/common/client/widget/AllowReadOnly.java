package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.Resource;

public interface AllowReadOnly<E extends Resource<E>> extends
        ResourceResetable<E> {

    void setReadOnly(final boolean isReadOnly);

    boolean isReadOnly();

}