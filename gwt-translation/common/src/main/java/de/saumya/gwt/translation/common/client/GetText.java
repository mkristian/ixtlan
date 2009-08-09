package de.saumya.gwt.translation.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.session.client.Locale;

public class GetText {

    private final WordBundleFactory   bundleFactory;

    private final PhraseBookFactory   bookFactory;

    private final WordFactory         wordFactory;

    private final PhraseFactory       phraseFactory;

    private final Map<String, Word>   wordMap         = new HashMap<String, Word>();

    private final Map<String, Phrase> phraseMap       = new HashMap<String, Phrase>();

    private boolean                   isInTranslation = true;

    public GetText(final WordBundleFactory bundleFactory,
            final WordFactory wordFactory, final PhraseBookFactory bookFactory,
            final PhraseFactory phraseFactory) {
        this.bundleFactory = bundleFactory;
        this.bookFactory = bookFactory;
        this.wordFactory = wordFactory;
        this.phraseFactory = phraseFactory;
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
        Phrase phrase = this.phraseMap.get(code);
        if (phrase == null) {
            phrase = this.phraseFactory.newResource();
            phrase.code = code;
            phrase.toBeApproved = code;
            phrase.save();
            this.phraseMap.put(code, phrase);
        }
        return phrase;
    }

    public Word getWord(final String code) {
        Word word = this.wordMap.get(code);
        if (word == null) {
            word = this.wordFactory.newResource();
            word.code = code;
            word.text = code;
            word.save();
            this.wordMap.put(code, word);
        }
        return word;
    }

    public void load(final Locale locale) {
        loadWordBundle(locale);
    }

    private final List<Translatable> translatables = new ArrayList<Translatable>();

    public void addWidget(final Translatable translatable,
            final HasClickHandlers widget) {
        this.translatables.add(translatable);
        widget.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(final ClickEvent event) {
                GWT.log(event.getNativeEvent().getButton() + " "
                        + NativeEvent.BUTTON_RIGHT, null);
                if (GetText.this.isInTranslation
                        && event.getNativeEvent().getButton() == NativeEvent.BUTTON_LEFT) {
                    GWT.log("translate: " + translatable.getCode(), null);
                }
            }
        });
    }

    void setTranslateMode(final boolean isInTranslation) {
        if (this.isInTranslation != isInTranslation) {
            for (final Translatable translatable : this.translatables) {
                translatable.reset();
            }
        }
        this.isInTranslation = isInTranslation;
    }

    public String get(final String code) {
        if (code == null || code.length() == 0) {
            return "";
        }
        return this.isInTranslation
                + ": "
                + (this.isInTranslation
                        ? getPhrase(code).toBeApproved
                        : getWord(code).text);
    }
}
