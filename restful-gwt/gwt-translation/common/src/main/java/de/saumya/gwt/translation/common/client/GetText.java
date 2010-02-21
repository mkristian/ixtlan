package de.saumya.gwt.translation.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Widget;

import de.saumya.gwt.persistence.client.ResourceChangeListener;
import de.saumya.gwt.session.client.models.Locale;
import de.saumya.gwt.session.client.models.LocaleFactory;
import de.saumya.gwt.translation.common.client.model.Phrase;
import de.saumya.gwt.translation.common.client.model.PhraseBook;
import de.saumya.gwt.translation.common.client.model.PhraseBookFactory;
import de.saumya.gwt.translation.common.client.model.PhraseFactory;
import de.saumya.gwt.translation.common.client.model.Word;
import de.saumya.gwt.translation.common.client.model.WordBundle;
import de.saumya.gwt.translation.common.client.model.WordBundleFactory;
import de.saumya.gwt.translation.common.client.model.WordFactory;

public class GetText {

    private final WordBundleFactory                  bundleFactory;

    private final PhraseBookFactory                  bookFactory;

    private final WordFactory                        wordFactory;

    private final PhraseFactory                      phraseFactory;

    private final List<Translatable>                 translatables   = new ArrayList<Translatable>();

    private Map<String, Word>                        wordMap         = new HashMap<String, Word>();

    private Map<String, Phrase>                      phraseMap       = new HashMap<String, Phrase>();

    private final Map<String, Map<String, Word>>     wordCache       = new HashMap<String, Map<String, Word>>();

    private final Map<String, Map<String, Phrase>>   phraseCache     = new HashMap<String, Map<String, Phrase>>();

    private boolean                                  isInTranslation = false;

    private Locale                                   locale;
    private final ResourceChangeListener<WordBundle> wordBundleListener;

    private final ResourceChangeListener<PhraseBook> phraseBookChangeListener;

    public GetText(final WordBundleFactory bundleFactory,
            final WordFactory wordFactory, final PhraseBookFactory bookFactory,
            final PhraseFactory phraseFactory, final LocaleFactory localeFactory) {
        this.bundleFactory = bundleFactory;
        this.bookFactory = bookFactory;
        this.wordFactory = wordFactory;
        this.phraseFactory = phraseFactory;
        this.locale = localeFactory.defaultLocale();
        this.wordBundleListener = new ResourceChangeListener<WordBundle>() {

            @Override
            public void onChange(final WordBundle resource) {
                for (final Word word : resource.words) {
                    GetText.this.wordMap.put(word.code, word);
                }
                final Locale locale = localeFactory.get(resource.locale);
                for (final Word word : GetText.this.wordMap.values()) {
                    if (word.isNew()) {
                        final Phrase phrase = phraseFactory.newResource();
                        phrase.code = word.code;
                        phrase.currentText = word.code;
                        phrase.locale = locale;
                        phrase.save();
                        GWT.log(phrase.toString(), null);
                        // TODO do I really need to do this ??
                        GetText.this.phraseMap.put(word.code, phrase);
                    }
                }
                resetTranslatables();
            }

            @Override
            public void onError(final WordBundle resource) {
                // nothing to do
            }
        };
        this.phraseBookChangeListener = new ResourceChangeListener<PhraseBook>() {

            @Override
            public void onChange(final PhraseBook resource) {
                for (final Phrase phrase : resource.phrases) {
                    GetText.this.phraseMap.put(phrase.code, phrase);
                }
                resetTranslatables();
            }

            @Override
            public void onError(final PhraseBook resource) {
                // nothing to do
            }
        };
    }

    private void loadWordBundle(final Locale locale) {
        if (!this.wordCache.containsKey(locale.code)) {
            this.wordMap = new HashMap<String, Word>();
            this.wordCache.put(locale.code, this.wordMap);
        }
        else {
            this.wordMap = this.wordCache.get(locale.code);
        }
        resetTranslatables();
        this.bundleFactory.get(locale.code, this.wordBundleListener);
    }

    private void loadPhraseBook(final Locale locale) {
        if (!this.phraseCache.containsKey(locale.code)) {
            this.phraseMap = new HashMap<String, Phrase>();
            this.phraseCache.put(locale.code, this.phraseMap);
        }
        else {
            this.phraseMap = this.phraseCache.get(locale.code);
        }
        resetTranslatables();
        this.bookFactory.get(locale.code, this.phraseBookChangeListener);
    }

    public Phrase getPhrase(final String code, final String defaultPhrase) {
        Phrase phrase = this.phraseMap.get(code);
        if (phrase == null && defaultPhrase != null) {
            phrase = this.phraseFactory.newResource();
            phrase.code = code;
            phrase.currentText = defaultPhrase;
            phrase.locale = this.locale;
            phrase.save();
            GWT.log(phrase.toString(), null);
            this.phraseMap.put(code, phrase);
        }
        return phrase;
    }

    public Phrase getPhrase(final String code) {
        return getPhrase(code, null);
    }

    public Word getWord(final String code) {
        Word word = this.wordMap.get(code);
        if (word == null) {
            // put the default in place into all words are loaded
            word = this.wordFactory.newResource();
            word.code = code;
            word.text = code;
            this.wordMap.put(code, word);
        }
        return word;
    }

    public void resetWord(final Phrase phrase) {
        Word word = this.wordMap.get(phrase.code);
        if (word == null) {
            word = this.wordFactory.newResource();
            word.code = phrase.code;
        }
        word.text = phrase.currentText;
    }

    public void load(final Locale locale, final boolean isInTranslation) {
        if (isInTranslation == this.isInTranslation
                && this.locale.equals(locale)) {
            return;
        }
        this.isInTranslation = isInTranslation;
        this.locale = locale;
        if (this.isInTranslation) {
            loadPhraseBook(locale);
        }
        else {
            loadWordBundle(locale);
        }
    }

    public void resetTranslatables() {
        DeferredCommand.addCommand(new Command() {

            @Override
            public void execute() {
                GWT.log("before reset translatables "
                        + GetText.this.translatables.size(), null);
                final Iterator<Translatable> iterator = GetText.this.translatables.iterator();
                while (iterator.hasNext()) {
                    final Translatable translatable = iterator.next();
                    if (((Widget) translatable).isAttached()) {
                        translatable.reset();
                    }
                    else {
                        GWT.log("TODO remove the need to remove this detached element "
                                        + translatable,
                                null);
                        iterator.remove();
                    }
                }
                GWT.log("after reset translatables "
                        + GetText.this.translatables.size(), null);
            }
        });
    }

    public void addTranslatable(final Translatable translatable) {
        this.translatables.add(translatable);
    }

    public String get(final String code) {
        if (code == null || code.length() == 0) {
            return "";
        }
        if (this.isInTranslation) {
            final Phrase phrase = getPhrase(code);
            return phrase.text == null
                    ? phrase.parentTranslation.text
                    : phrase.text == null
                            ? phrase.parentTranslation.text
                            : phrase.text;
        }
        else {
            return getWord(code).text;
        }
    }

    public boolean isInTranslation() {
        return this.isInTranslation;
    }
}
