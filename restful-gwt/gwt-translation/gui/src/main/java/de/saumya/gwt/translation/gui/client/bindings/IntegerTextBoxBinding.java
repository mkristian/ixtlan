package de.saumya.gwt.translation.gui.client.bindings;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.widget.IntegerTextBox;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class IntegerTextBoxBinding<T extends Resource<T>> extends
        IntegerTextBox implements Binding<T> {

}
