package de.saumya.gwt.translation.gui.client.bindings;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.widget.IntegerTextBox;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class IntegerTextBoxBinding<T extends AbstractResource<T>> extends
        IntegerTextBox implements Binding<T> {

}
