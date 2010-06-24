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

    <% first = attributes.detect {|a| a.type == :string } || attributes.first -%>
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
        this.resource = this.factory.newResource(idValue());

        this.resource.id = 1;
<% Array(attributes).each do |attribute| -%>
				  this.resource.<%= attribute.name.javanize %> = <% if [:date, :time, :date_time].member? attribute.type  %>new de.saumya.gwt.persistence.client.TimestampFactory("<%= attribute.sample_value %>").to<%= attribute.type == :date_time ? "Timestamp" : attribute.type.to_s.constantize %>()<% elsif [:integer, :float, :decimal, :big_decimal, :boolean].member? attribute.type  %><%= attribute.sample_value %><% else %>"<%= attribute.sample_value %>"<% end -%>;
<% end -%>

        this.repository.addXmlResponse(RESOURCE_XML);

        this.resource.save();

        return this.resource;
    }

    @Override
    public void doTestCreate() {
        assertEquals("<%= first.sample_value %>", this.resource.<%= first.name.javanize %><% if [:date, :time, :date_time].member? first.type  %>.toString()<% end -%>);
    }

    @Override
    public void doTestUpdate() {
        this.resource.<%= first.name.javanize %> = <% if [:date, :time, :date_time].member? first.type  %>new de.saumya.gwt.persistence.client.TimestampFactory(changedValue()).toDate()<% else %>changedValue()<% end -%>;
        this.resource.save();
        assertEquals(this.resource.<%= first.name.javanize %><% if [:date, :time, :date_time].member? first.type  %>.toString()<% end -%>, changedValue());
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
        return "<%= first.sample_value(false) %>";
    }

    @Override
    protected int idValue() {
        return 1;
    }

    @Override
    protected String marshallingXml() {
        return XML;
    }

    @Override
    protected String value() {
        return "<%= first.sample_value %>";
    }
}
