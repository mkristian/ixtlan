package de.saumya.gwt.gettext.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.datamapper.client.ResourcesChangeListener;

public class GetText {

    private final WordFactory       wordFactory;

    private final Map<String, Word> wordMap = new HashMap<String, Word>();

    public GetText(final WordFactory wordFactory) {
        this.wordFactory = wordFactory;
    }

    public void load() {
        this.wordMap.clear();
        this.wordFactory.all(new ResourcesChangeListener<Word>() {

            @Override
            public void onChange(final Resources<Word> resources,
                    final Word word) {
                GWT.log("word " + word.toString(), null);
                GetText.this.wordMap.put((word).code, word);
                GWT.log("map " + GetText.this.wordMap.toString(), null);
            }

            @Override
            public void onLoaded(final Resources<Word> resources) {
            }
        });
    }

    public String _(final String code) {
        final Word result = this.wordMap.get(code);
        return result == null ? code : result.text;
    }

    public Word getWord(final String code) {
        Word word = this.wordMap.get(code);
        if (word == null) {
            word = this.wordFactory.newResource();
            word.code = code;
            word.text = code;
            this.wordMap.put(code, word);
        }
        return word;
    }
}
