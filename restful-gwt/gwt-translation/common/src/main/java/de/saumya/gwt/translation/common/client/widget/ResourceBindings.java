package de.saumya.gwt.translation.common.client.widget;

import java.util.ArrayList;
import java.util.List;

import de.saumya.gwt.persistence.client.Resource;

public class ResourceBindings<E extends Resource<E>> {

    public interface Binding<E extends Resource<E>> {

        void pushInto(E resource);

        void pullFrom(E resource);

        void setEnabled(boolean isEnabled);
    }

    private E                      resource;

    private boolean                readOnly;

    private final List<Binding<E>> bindings = new ArrayList<Binding<E>>();

    public void pullFromResource(final E resource) {
        this.resource = resource;
        if (resource != null) {
            for (final Binding<E> binding : this.bindings) {
                binding.pullFrom(resource);
            }
        }
    }

    public void pushIntoResource() {
        if (this.resource != null) {
            for (final Binding<E> binding : this.bindings) {
                binding.pushInto(this.resource);
            }
        }
    }

    public void setReadOnly(final boolean isReadOnly) {
        for (final Binding<E> binding : this.bindings) {
            binding.setEnabled(!isReadOnly);
        }
        this.readOnly = isReadOnly;
    }

    public boolean isReadOnly() {
        return this.readOnly;
    }

    public void add(final Binding<E> binding) {
        this.bindings.add(binding);
    }

}
