package de.saumya.gwt.translation.common;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.translation.common.client.model.PhraseBookTestGwt;
import de.saumya.gwt.translation.common.client.model.PhraseTestGwt;
import de.saumya.gwt.translation.common.client.model.TranslationTestGwt;
import de.saumya.gwt.translation.common.client.model.WordBundleTestGwt;
import de.saumya.gwt.translation.common.client.model.WordTestGwt;

public class GwtTestSuite extends GWTTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for GWT Translation Common");
        suite.addTestSuite(WordTestGwt.class);
        suite.addTestSuite(WordBundleTestGwt.class);
        suite.addTestSuite(TranslationTestGwt.class);
        suite.addTestSuite(PhraseTestGwt.class);
        suite.addTestSuite(PhraseBookTestGwt.class);
        return suite;
    }
}
