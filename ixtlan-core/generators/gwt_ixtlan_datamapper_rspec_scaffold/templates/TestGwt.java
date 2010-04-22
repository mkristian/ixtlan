/**
 *
 */
package <%= package %>.models;

import de.saumya.gwt.persistence.client.Resource;
import de.saumya.gwt.persistence.client.ResourceFactory;

/**
 * GWT JUnit tests must extend GWTTestCase.
 */
public class <%= class_name %>TestGwt extends AbstractApplicationResourceTestGwt<<%= class_name %>> {


    private <%= class_name %> resource;

    private static final String RESOURCE_XML = "<<%= singular_name%>>"
                                                     + "<id>1</id>"
<% Array(attributes).each do |attribute| -%>
                                                     + "<<%= attribute.name %>><%= attribute.sample_value %></<%= attribute.name %>>"
<% end -%>
<% unless options[:skip_timestamps] -%>
                                                     + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                                     + "<updated_at>2009-07-09 17:14:48.0</updated_at>"
<% end -%>
                                                     + "</<%= singular_name%>>";

    @Override
    protected String resourceNewXml() {
        return RESOURCE_XML<% unless options[:skip_timestamps] -%>.replaceFirst("<created_at>[0-9-:. ]*</created_at>", "").replaceFirst("<updated_at>[0-9-:. ]*</updated_at>", "")<% end -%>.replace("<id>1</id>", "");
    }

    @Override
    protected String resource1Xml() {
        return RESOURCE_XML;
    }

    @Override
    protected String resource2Xml() {
        return RESOURCE_XML.replace(">1<", ">2<");
    }

    @Override
    protected String resourcesXml() {
        return "<<%= plural_name %>>" + resource1Xml() + resource2Xml() + "</<%= plural_name %>>";
    }

    @Override
    protected ResourceFactory<<%= class_name %>> factorySetUp() {
        return new <%= class_name %>Factory(this.repository,
         this.notifications<% unless options[:skip_modified_by] -%>, this.userFactory<% end -%>);
    }

    @Override
    protected Resource<<%= class_name %>> resourceSetUp() {
        this.resource = this.factory.newResource();

        this.resource.id = 1;
        this.resource.<%= attributes.first.name.javanize %> = <% if [:date, :time, :date_time].member? attributes.first.type  %>new de.saumya.gwt.persistence.client.TimestampFactory("<%= attributes.first.sample_value %>").toDate()<% else %>"<%= attributes.first.sample_value %>"<% end -%>;

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("<%= attributes.first.sample_value %>", this.resource.<%= attributes.first.name.javanize %><% if [:date, :time, :date_time].member? attributes.first.type  %>.toString()<% end -%>);
    }

    @Override
    public void doTestUpdate() {
        this.resource.<%= attributes.first.name.javanize %> = <% if [:date, :time, :date_time].member? attributes.first.type  %>new de.saumya.gwt.persistence.client.TimestampFactory(changedValue()).toDate()<% else %>changedValue()<% end -%>;
        this.resource.save();
        assertEquals(this.resource.<%= attributes.first.name.javanize %><% if [:date, :time, :date_time].member? attributes.first.type  %>.toString()<% end -%>, changedValue());
    }

    private final static String XML = "<<%= singular_name%>>"
                                            + "<id>1</id>"
<% Array(attributes).each do |attribute| -%>
                                            + "<<%= attribute.name %>><%= attribute.sample_value %></<%= attribute.name %>>"
<% end -%>
<% unless options[:skip_timestamps] -%>
                                            + "<created_at>2009-07-09 17:14:48.0</created_at>"
                                            + "<updated_at>2007-07-09 17:14:48.0</updated_at>"
<% end -%>
                                            + "</<%= singular_name%>>";

    @Override
    protected String changedValue() {
        return "<%= attributes.first.sample_value(false) %>";
    }

    @Override
    protected String keyValue() {
        return "1";
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    @Override
    protected String value() {
        return "<%= attributes.first.sample_value %>";
    }
}
