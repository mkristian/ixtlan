package de.saumya.gwt.translation.common.client;

import java.util.HashMap;
import java.util.Map;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.session.client.Locale;

public class GetText {

    private final WordBundleFactory   bundleFactory;

    private final PhraseBookFactory   bookFactory;

    private final Map<String, Word>   wordMap   = new HashMap<String, Word>();

    private final Map<String, Phrase> phraseMap = new HashMap<String, Phrase>();

    public GetText(final WordBundleFactory bundleFactory,
            final PhraseBookFactory bookFactory) {
        this.bundleFactory = bundleFactory;
        this.bookFactory = bookFactory;
    }

    public void loadWordBundle(final Locale locale) {
        this.wordMap.clear();
        this.bundleFactory.get(locale.code,
                               new ResourceChangeListener<WordBundle>() {

                                   @Override
                                   public void onChange(
                                           final WordBundle resource) {
                                       for (final Word word : resource.words) {
                                           GetText.this.wordMap.put(word.code,
                                                                    word);
                                       }

                                   }
                               });
    }

    public void loadPhraseBook(final Locale locale) {
        this.phraseMap.clear();
        this.bookFactory.get(locale.code,
                             new ResourceChangeListener<PhraseBook>() {

                                 @Override
                                 public void onChange(final PhraseBook resource) {
                                     for (final Phrase phrase : resource.phrases) {
                                         GetText.this.phraseMap.put(phrase.code,
                                                                    phrase);
                                     }

                                 }
                             });
    }

    public Phrase getPhrase(final String code) {
        return this.phraseMap.get(code);
    }

    public Word getWord(final String code) {
        final Word word = this.wordMap.get(code);
        // if (word == null) {
        // // word = this.wordFactory.newResource();
        // word.code = code;
        // word.text = code;
        // this.wordMap.put(code, word);
        // }
        return word;
    }
}
