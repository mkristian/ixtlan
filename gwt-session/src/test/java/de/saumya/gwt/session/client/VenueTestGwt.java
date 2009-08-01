package de.saumya.gwt.session.client;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class VenueTestGwt extends AbstractResourceTestGwt<Venue> {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.saumya.gwt.session.Session";
    }

    private Venue        resource;
    private VenueFactory factory;
    private static final String  RESOURCE_XML  = "<venue>"
                                              + "<id>dhara</id>"
                                              + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                              + "</venue>";

    private static final String  RESOURCES_XML = "<venues>"
                                              + RESOURCE_XML
                                              + RESOURCE_XML.replace(">dhara<", ">dvara<")
                                              + "</venues>";

    protected void resourceSetUp() {
        factory = new VenueFactory(repository);
        resource = factory.newResource();

        resource.id = "dhara";

        repository.addXmlResponse(RESOURCE_XML);

        resource.save();
    }

    public void testCreate() {
        assertTrue(resource.isUptodate());
        assertEquals("dhara", resource.id);
    }

    public void testRetrieve() {
        repository.addXmlResponse(RESOURCE_XML);

        Venue rsrc = factory.get("dhara", countingResourceListener);

        assertEquals(1, countingResourceListener.count());
        assertTrue(resource.isUptodate());
        assertEquals(resource.toString(), rsrc.toString());
    }

    public void testRetrieveAll() {
        repository.addXmlResponse(RESOURCES_XML);

        Resources<Venue> resources = factory.all(countingResourcesListener);

        assertEquals(2, countingResourcesListener.count());
        int id = 0;
        String[] codes = {"dhara", "dvara"};
        for (Resource<Venue> rsrc : resources) {
            assertTrue(resource.isUptodate());
            assertEquals(resource.toXml().replace(">dhara<", ">" + codes[id++] + "<"),
                         rsrc.toXml());
        }
    }

    public void testUpdate() {
        resource.id = null;
        resource.save();
        
        //TODO should result in an error since they are immutable
        assertTrue(resource.isUptodate());
    }

    public void testDelete() {
        this.resource.destroy();
        
        //TODO should result in an error since they are immutable
        assertTrue(resource.isDeleted());
    }
    
    public void testMarshallingUnmarshallingResource(){
        Resource<Venue> resource = factory.newResource();
        resource.fromXml(RESOURCE_XML);
        
        assertEquals(RESOURCE_XML, resource.toXml());
    }

    public void testMarshallingUnmarshallingResources(){
        Resources<Venue> resources = new Resources<Venue>(factory);
        resources.fromXml(RESOURCES_XML);
        
        assertEquals(RESOURCES_XML, resources.toXml());
    }

}
