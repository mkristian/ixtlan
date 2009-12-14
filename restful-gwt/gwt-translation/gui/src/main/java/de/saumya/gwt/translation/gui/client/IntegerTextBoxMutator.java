package de.saumya.gwt.translation.gui.client;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.widget.IntegerTextBox;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator.Mutator;

public abstract class IntegerTextBoxMutator<T extends Resource<T>> extends
        IntegerTextBox implements Mutator<T> {

    public IntegerTextBoxMutator(final ResourceMutator<T> resourceMutator) {
        resourceMutator.add(this);
    }

}
