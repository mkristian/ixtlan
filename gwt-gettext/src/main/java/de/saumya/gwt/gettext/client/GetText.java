package de.saumya.gwt.gettext.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;

import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.datamapper.client.ResourcesChangeListener;
import de.saumya.gwt.gettext.client.Word;
import de.saumya.gwt.gettext.client.WordFactory;

public class GetText {

    private final WordFactory       wordFactory;

    private final Map<String, Word> wordMap = new HashMap<String, Word>();

    public GetText(WordFactory wordFactory) {
        this.wordFactory = wordFactory;
    }

    public void load() {
        wordMap.clear();
        this.wordFactory.all(new ResourcesChangeListener<Word>() {

            public void onChange(Resources<Word> resources, Word word) {
                GWT.log("word " + word.toString(), null);
                wordMap.put(((Word) word).code, (Word) word);
                GWT.log("map " + wordMap.toString(), null);
            }
        });
    }

    public String _(String code) {
        Word result = wordMap.get(code);
        return result == null ? code : result.text;
    }

    public Word getWord(String code) {
        Word word = wordMap.get(code);
        if (word == null) {
            word = wordFactory.newResource();
            word.code = code;
            word.text = code;
            wordMap.put(code, word);
        }
        return word;
    }
}
