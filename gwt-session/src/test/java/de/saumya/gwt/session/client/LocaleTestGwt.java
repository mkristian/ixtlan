package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class LocaleTestGwt extends AbstractResourceTestGwt<Locale> {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Locale        locale;
    private LocaleFactory factory;
    private static final String  RESOURCE_XML  = "<locale>"
                                              + "<code>en</code>"
                                              + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                              + "</locale>";

    private static final String  RESOURCES_XML = "<locales>"
                                              + RESOURCE_XML
                                              + RESOURCE_XML.replace(">en<", ">en_GE<")
                                              + "</locales>";

    protected void resourceSetUp() {
        factory = new LocaleFactory(repository);
        locale = factory.newResource();

        locale.code = "en";

        repository.addXmlResponse(RESOURCE_XML);

        locale.save();
    }

    public void testCreate() {
        assertTrue(locale.isUptodate());
        assertEquals("en", locale.code);
    }

    public void testRetrieve() {
        repository.addXmlResponse(RESOURCE_XML);

        Locale l = factory.get("en", countingResourceListener);

        assertEquals(1, countingResourceListener.count());
        assertTrue(locale.isUptodate());
        assertEquals(locale.toString(), l.toString());
    }

    public void testRetrieveAll() {
        repository.addXmlResponse(RESOURCES_XML);

        Resources<Locale> locales = factory.all(countingResourcesListener);

        assertEquals(2, countingResourcesListener.count());
        int id = 0;
        String[] codes = {"en", "en_GE"};
        for (Resource<Locale> l : locales) {
            assertTrue(locale.isUptodate());
            assertEquals(locale.toXml().replace(">en<", ">" + codes[id++] + "<"),
                         l.toXml());
        }
    }

    public void testUpdate() {
        locale.code = null;
        locale.save();
        
        //TODO should result in an error since they are immutable
        assertTrue(locale.isUptodate());
    }

    public void testDelete() {
        this.locale.destroy();
        
        //TODO should result in an error since they are immutable
        assertTrue(locale.isDeleted());
    }
    
    public void testMarshallingUnmarshallingResource(){
        Resource<Locale> resource = factory.newResource();
        resource.fromXml(RESOURCE_XML);
        
        assertEquals(RESOURCE_XML, resource.toXml());
    }

    public void testMarshallingUnmarshallingResources(){
        Resources<Locale> resources = new Resources<Locale>(factory);
        resources.fromXml(RESOURCES_XML);
        
        assertEquals(RESOURCES_XML, resources.toXml());
    }
}
