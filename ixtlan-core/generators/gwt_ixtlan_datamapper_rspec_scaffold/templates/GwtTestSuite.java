package <%= package %>;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.google.gwt.junit.tools.GWTTestSuite;

public class GwtTestSuite extends GWTTestSuite {

    public static Test suite() {
        final TestSuite suite = new TestSuite("Test for GWT Application");
        return suite;
    }
}
