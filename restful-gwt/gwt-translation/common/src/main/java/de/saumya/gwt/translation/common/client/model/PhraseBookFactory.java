package de.saumya.gwt.translation.common.client.model;

import java.util.HashMap;
import java.util.Map;

import de.saumya.gwt.persistence.client.AnonymousResourceFactory;
import de.saumya.gwt.persistence.client.Repository;
import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.persistence.client.ResourceCollection;
import de.saumya.gwt.persistence.client.ResourceNotifications;
import de.saumya.gwt.persistence.client.ResourcesChangeListener;

public class PhraseBookFactory extends AnonymousResourceFactory<PhraseBook> {

    private final PhraseFactory factory;

    public PhraseBookFactory(final Repository repository,
            final ResourceNotifications notification,
            final PhraseFactory factory) {
        super(repository, notification);
        this.factory = factory;
    }

    @Override
    public String storageName() {
        return "phrase_book";
    }

    @Override
    public PhraseBook newResource() {
        return new PhraseBook(this.repository, this, this.factory);
    }

    public PhraseBook first(final String localeCode,
            final ResourceChangeListener<PhraseBook> listener) {
        final PhraseBook book = newResource();
        book.addResourceChangeListener(listener);
        book.locale = localeCode;
        final Map<String, String> q = new HashMap<String, String>();
        q.put("code", book.locale);
        all(q, new ResourcesChangeListener<PhraseBook>() {

            @Override
            public void onLoaded(final ResourceCollection<PhraseBook> resources) {
                final PhraseBook phrases = resources.iterator().next();
                book.locale = phrases.locale;
                book.phrases = phrases.phrases;
                // TODO maybe fire resource change
            }
        });
        return book;
    }
}
