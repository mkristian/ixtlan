package de.saumya.gwt.translation.common.client.widget;

import java.util.List;

import de.saumya.gwt.datamapper.client.Resource;

public class ResourceMutator<E extends Resource<E>> {

    interface Mutator<E extends Resource<E>> {

        void push(E resource);

        void pull(E resource);
    }

    private E                      resource;

    private final List<Mutator<E>> mutators = null;

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

    public void addAttribute(final Mutator<E> mutator) {
        this.mutators.add(mutator);
    }

}
