package org.dhamma.client;

import java.util.HashMap;
import java.util.Map;

import org.dhamma.client.resource.Resources;
import org.dhamma.client.resource.ResourcesChangeListener;

import com.google.gwt.core.client.GWT;

public class GetText {

    private final WordFactory       wordFactory;

    private final Map<String, Word> wordMap = new HashMap<String, Word>();

    GetText(WordFactory wordFactory) {
        this.wordFactory = wordFactory;
    }

    void load() {
        wordMap.clear();
        this.wordFactory.all(new ResourcesChangeListener<Word>() {

            public void onChange(Resources<Word> resources, Word word) {
                GWT.log("word " + word.toString(), null);
                wordMap.put(((Word) word).code, (Word) word);
                GWT.log("map " + wordMap.toString(), null);
            }
        });
    }

    String _(String code) {
        Word result = wordMap.get(code);
        return result == null ? code : result.text;
    }

    Word getWord(String code) {
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
