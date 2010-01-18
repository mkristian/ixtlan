package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.TextArea;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.translation.common.client.widget.ResourceBindings.Binding;

public abstract class TextAreaBinding<E extends Resource<E>> extends TextArea
        implements Binding<E> {
}
