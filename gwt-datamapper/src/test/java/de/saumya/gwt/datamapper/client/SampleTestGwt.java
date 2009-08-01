package de.saumya.gwt.datamapper.client;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class SampleTestGwt extends AbstractResourceTestGwt<Sample> {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.saumya.gwt.datamapper.Datamapper";
    }

    private Sample        locale;
    private SampleFactory factory;
    private final String  LOCALE_XML  = "<locale>"
                                              + "<id>123</id>"
                                              + "<created_at>2009-07-09 17:14:48</created_at>"
                                              + "</locale>";

    private final String  LOCALES_XML = "<locales>"
                                              + LOCALE_XML
                                              + LOCALE_XML.replace("123", "124")
                                              + "</locales>";

    protected void resourceSetUp() {
        factory = new SampleFactory(repository);
        locale = factory.newResource();

        locale.country = "DE";
        locale.language = "en";

        repository.addXmlResponse(LOCALE_XML);

        locale.save();
    }

    public void testCreate() {
        assertTrue(locale.isUptodate());
        assertEquals(123, locale.id);
    }

    public void testRetrieve() {
        repository.addXmlResponse(LOCALE_XML);

        Sample l = factory.get(1, countingResourceListener);

        assertEquals(1, countingResourceListener.count());
        assertTrue(locale.isUptodate());
        assertEquals(locale.toString(), l.toString());
    }

    public void testRetrieveAll() {
        repository.addXmlResponse(LOCALES_XML);

        Resources<Sample> locales = factory.all(countingResourcesListener);

        assertEquals(2, countingResourcesListener.count());
        int id = 123;
        for (Resource<Sample> l : locales) {
            assertTrue(locale.isUptodate());
            assertEquals(locale.toString().replace("123", "" + id++),
                         l.toString());
        }
    }

    public void testUpdate() {
        locale.country = null;

        locale.save();

        assertTrue(locale.isUptodate());
    }

    public void testDelete() {
        this.locale.destroy();

        assertTrue(locale.isDeleted());
    }
}
