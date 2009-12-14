package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.TextArea;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator.Mutator;

public abstract class TextAreaMutator<E extends Resource<E>> extends TextArea
        implements Mutator<E> {

    public TextAreaMutator(final ResourceMutator<E> resourceMutator) {
        resourceMutator.add(this);
    }
}
