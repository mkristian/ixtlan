package de.saumya.gwt.translation.common.client.widget;

import java.util.ArrayList;
import java.util.List;

import de.saumya.gwt.persistence.client.Resource;

public class ResourceMutator<E extends Resource<E>> {

    public interface Mutator<E extends Resource<E>> {

        void push(E resource);

        void pull(E resource);

        void setEnabled(boolean isEnabled);
    }

    private E                      resource;

    private final List<Mutator<E>> mutators = new ArrayList<Mutator<E>>();

    public void pull(final E resource) {
        this.resource = resource;
        for (final Mutator<E> mutator : this.mutators) {
            mutator.pull(resource);
        }
    }

    public void push() {
        for (final Mutator<E> mutator : this.mutators) {
            mutator.push(this.resource);
        }
    }

    public void setReadOnly(final boolean isReadOnly) {
        for (final Mutator<E> mutator : this.mutators) {
            mutator.setEnabled(!isReadOnly);
        }
    }

    public void add(final Mutator<E> mutator) {
        this.mutators.add(mutator);
    }

}
