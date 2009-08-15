package de.saumya.gwt.translation.common;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

import de.saumya.gwt.translation.common.client.PhraseBookTestGwt;
import de.saumya.gwt.translation.common.client.PhraseTestGwt;
import de.saumya.gwt.translation.common.client.TranslationTestGwt;
import de.saumya.gwt.translation.common.client.WordBundleTestGwt;
import de.saumya.gwt.translation.common.client.WordTestGwt;

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
