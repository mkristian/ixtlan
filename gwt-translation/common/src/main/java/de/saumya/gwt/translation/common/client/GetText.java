package de.saumya.gwt.translation.common.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.HasMouseUpHandlers;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;

import de.saumya.gwt.datamapper.client.ResourceChangeListener;
import de.saumya.gwt.session.client.Locale;

public class GetText {

    private final WordBundleFactory                bundleFactory;

    private final PhraseBookFactory                bookFactory;

    private final WordFactory                      wordFactory;

    private final PhraseFactory                    phraseFactory;

    private Map<String, Word>                      wordMap         = new HashMap<String, Word>();

    private Map<String, Phrase>                    phraseMap       = new HashMap<String, Phrase>();

    private final Map<String, Map<String, Word>>   wordCache       = new HashMap<String, Map<String, Word>>();

    private final Map<String, Map<String, Phrase>> phraseCache     = new HashMap<String, Map<String, Phrase>>();

    private boolean                                isInTranslation = false;

    public GetText(final WordBundleFactory bundleFactory,
            final WordFactory wordFactory, final PhraseBookFactory bookFactory,
            final PhraseFactory phraseFactory) {
        this.bundleFactory = bundleFactory;
        this.bookFactory = bookFactory;
        this.wordFactory = wordFactory;
        this.phraseFactory = phraseFactory;
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
        this.bundleFactory.get(locale.code,
                               new ResourceChangeListener<WordBundle>() {

                                   @Override
                                   public void onChange(
                                           final WordBundle resource) {
                                       for (final Word word : resource.words) {
                                           GetText.this.wordMap.put(word.code,
                                                                    word);
                                       }
                                       GetText.this.wordCache.put(locale.code,
                                                                  GetText.this.wordMap);
                                       resetTranslatables();
                                   }
                               });
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
        this.bookFactory.get(locale.code,
                             new ResourceChangeListener<PhraseBook>() {

                                 @Override
                                 public void onChange(final PhraseBook resource) {
                                     for (final Phrase phrase : resource.phrases) {
                                         GetText.this.phraseMap.put(phrase.code,
                                                                    phrase);
                                     }
                                     GetText.this.phraseCache.put(locale.code,
                                                                  GetText.this.phraseMap);
                                     resetTranslatables();
                                 }
                             });
    }

    private Phrase getPhrase(final String code) {
        Phrase phrase = this.phraseMap.get(code);
        if (phrase == null) {
            phrase = this.phraseFactory.newResource();
            phrase.code = code;
            phrase.toBeApproved = code;
            phrase.save();
            GWT.log(phrase.toString(), null);
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
        load(locale, this.isInTranslation);
    }

    public void load(final Locale locale, final boolean isInTranslation) {
        this.isInTranslation = isInTranslation;
        if (this.isInTranslation) {
            loadPhraseBook(locale);
        }
        else {
            loadWordBundle(locale);
        }
    }

    private void resetTranslatables() {
        for (final Translatable translatable : this.translatables) {
            translatable.reset();
        }
    }

    private final List<Translatable>          translatables = new ArrayList<Translatable>();
    private final WidgetTranslationPopupPanel popupPanel    = new WidgetTranslationPopupPanel();

    public void addWidget(final Translatable translatable,
            final HasMouseUpHandlers widget) {
        this.translatables.add(translatable);
        widget.addMouseUpHandler(new MouseUpHandler() {

            @Override
            public void onMouseUp(final MouseUpEvent event) {
                if (GetText.this.isInTranslation
                        && event.getNativeEvent().getButton() == NativeEvent.BUTTON_MIDDLE) {
                    final Translatable translatableWidget = (Translatable) event.getSource();
                    final Phrase phrase = getPhrase(translatableWidget.getCode());
                    GetText.this.popupPanel.setup(phrase, translatableWidget);
                    GetText.this.popupPanel.setPopupPosition(event.getClientX(),
                                                             event.getClientY());
                    GetText.this.popupPanel.show();
                }
            }
        });
    }

    public String get(final String code) {
        if (code == null || code.length() == 0) {
            return "";
        }
        return this.isInTranslation
                ? getPhrase(code).toBeApproved
                : getWord(code).text;
    }

    public boolean isInTranslation() {
        return this.isInTranslation;
    }
}
