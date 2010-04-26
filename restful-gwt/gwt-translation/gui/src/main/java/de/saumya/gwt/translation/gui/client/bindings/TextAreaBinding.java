package de.saumya.gwt.translation.gui.client.bindings;

import com.google.gwt.user.client.ui.TextArea;

import de.saumya.gwt.persistence.client.AbstractResource;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class TextAreaBinding<E extends AbstractResource<E>> extends TextArea
        implements Binding<E> {
}
