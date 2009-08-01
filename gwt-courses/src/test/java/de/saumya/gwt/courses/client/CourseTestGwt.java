package de.saumya.gwt.courses.client;

import java.sql.Date;

import de.saumya.gwt.datamapper.client.AbstractResourceTestGwt;
import de.saumya.gwt.datamapper.client.Resource;
import de.saumya.gwt.datamapper.client.Resources;
import de.saumya.gwt.session.client.Venue;
import de.saumya.gwt.session.client.VenueFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class CourseTestGwt extends AbstractResourceTestGwt<Course> {

    /**
     * Must refer to a valid module that sources this class.
     */
    public String getModuleName() {
        return "de.saumya.gwt.courses.Courses";
    }

    private Course        course;
    private CourseFactory factory;

    private final String  COURSE_XML = "<course>"
                                             + "<id>123</id>"
                                             + "<venue><id>dvara</id></venue>"
                                             + "<from>2009-07-09</from>"
                                             + "<to>2009-08-09</to>"
                                             + "<created_at>2009-07-09 17:14:48</created_at>"
                                             + "<updated_at>2009-07-09 17:14:48</updated_at>"
                                             + "</course>";
    
    private final String COURSES_XML = "<courses>"
        + COURSE_XML + COURSE_XML.replace("123", "124")
        + "</courses>"; 
    
    protected void resourceSetUp() {
        VenueFactory venueFactory = new VenueFactory(repository);
        factory = new CourseFactory(repository, venueFactory);
        course = factory.newResource();
        Venue dvara = venueFactory.newResource();
        dvara.id = "dvara";

        course.venue = dvara;
        course.from = Date.valueOf("2009-07-09");
        course.to = Date.valueOf("2009-08-09");

        repository.addXmlResponse(COURSE_XML);

        course.save();
    }

    public void testCreate() {
        assertTrue(course.isUptodate());
        assertEquals(123, course.id);
    }

    public void testRetrieve() {
        repository.addXmlResponse(COURSE_XML);

        Course l = factory.get(123, countingResourceListener);

        assertEquals(1, countingResourceListener.count());
        assertTrue(course.isUptodate());
        assertEquals(course.toString(), l.toString());
    }

    public void testRetrieveAll() {
        repository.addXmlResponse(COURSES_XML);

        Resources<Course> courses = factory.all(countingResourcesListener);

        assertEquals(2, countingResourcesListener.count());
        int id = 123;
        for (Resource<Course> crs : courses) {
            assertTrue(course.isUptodate());
            assertEquals(course.toString().replace("123", "" + id++), crs.toString());
        }
    }

    public void testUpdate() {
        course.to = Date.valueOf("2009-07-01");

        course.save();
        
  //      assertEquals(1, countingResourceListener.count());
        assertTrue(course.isUptodate());
    }

    public void testDelete() {
        this.course.destroy();
        
//        assertEquals(1, countingResourceListener.count());
        assertTrue(course.isDeleted());
    }
}
