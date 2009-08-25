/**
 * 
 */
package de.saumya.gwt.translation.gui.client;

import com.google.gwt.user.client.ui.TextBox;

import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator;
import de.saumya.gwt.translation.common.client.widget.ResourceMutator.Mutator;

public abstract class TextBoxMutator<E extends Resource<E>> extends TextBox
        implements Mutator<E> {

    public TextBoxMutator(final ResourceMutator<E> resourceMutator) {
        resourceMutator.add(this);
    }
}