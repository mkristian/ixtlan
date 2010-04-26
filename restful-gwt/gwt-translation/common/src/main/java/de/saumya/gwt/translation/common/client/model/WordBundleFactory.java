package de.saumya.gwt.translation.common.client.model;

import java.util.HashMap;
import java.util.Map;

import de.saumya.gwt.persistence.client.AnonymousResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;

public class WordBundleFactory extends AnonymousResourceFactory<WordBundle> {

    private final WordFactory wordFactory;

    public WordBundleFactory(final Repository repository,
            final ResourceNotifications notification,
            final WordFactory wordFactory) {
        super(repository, notification);
        this.wordFactory = wordFactory;
    }

    @Override
    public String storageName() {
        return "word_bundle";
    }

    @Override
    public WordBundle newResource() {
        return new WordBundle(this.repository, this, this.wordFactory);
    }

    public WordBundle first(final String localeCode,
            final ResourceChangeListener<WordBundle> listener) {
        final WordBundle bundle = newResource();
        bundle.addResourceChangeListener(listener);
        bundle.locale = localeCode;
        final Map<String, String> q = new HashMap<String, String>();
        q.put("code", bundle.locale);
        all(q, new ResourcesChangeListener<WordBundle>() {

            @Override
            public void onLoaded(final ResourceCollection<WordBundle> resources) {
                final WordBundle words = resources.iterator().next();
                bundle.locale = words.locale;
                bundle.words = words.words;
                // TODO maybe fire resource change
            }
        });
        return bundle;
    }

}
