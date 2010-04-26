package de.saumya.gwt.translation.common.client.widget;

import de.saumya.gwt.persistence.client.AbstractResource;

public interface AllowReadOnly<E extends AbstractResource<E>> extends
        ResourceResetable<E> {

    void setReadOnly(final boolean isReadOnly);

    boolean isReadOnly();

}
