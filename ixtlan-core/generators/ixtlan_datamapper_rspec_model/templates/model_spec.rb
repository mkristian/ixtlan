require File.expand_path(File.dirname(__FILE__) + '<%= '/..' * class_nesting_depth %>/../spec_helper')
<% if !options[:skip_modified_by] || options[:current_user] -%>
USER = Object.full_const_get(Ixtlan::Models::USER) unless Object.const_defined? "USER"
<% end -%>
describe <%= class_name %> do
  before(:each) do
<% if !options[:skip_modified_by] || options[:current_user] -%>
    user = USER.first
    unless user
      user = USER.new(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :language => 'en', :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
      user.created_by_id = 1
      user.updated_by_id = 1
      user.save!
    end
<% end -%>
    @valid_attributes = {
<% if !options[:skip_modified_by] || options[:current_user]  -%>
      :current_user => user,
<% end -%>
<% attributes.each_with_index do |attribute, attribute_index| -%>
      :<%= attribute.name %> => <%= attribute.default_value %><%= attribute_index == attributes.length - 1 ? '' : ','%>
<% end -%>
    }
  end

  it "should create a new instance given valid attributes" do
    <%= class_name %>.all(:<%= attributes.first.name %> => <%= attributes.first.default_value %>).destroy!
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes)
    <%= singular_name %>.valid?.should be_true
  end

<% attributes.each do |attribute| -%>
  it "should require <%= attribute.name %>" do
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => nil))
    <%= singular_name %>.errors.on(:<%= attribute.name %>).should_not == nil
  end

<% if [:string, :text, :slug].member? attribute.type -%>
  it 'should not match <%= attribute.name %>' do
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => "<script" ))
    <%= singular_name %>.errors.on(:<%= attribute.name %>).should_not == nil
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => "sc'ript" ))
    <%= singular_name %>.errors.on(:<%= attribute.name %>).should_not == nil
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => "scr&ipt" ))
    <%= singular_name %>.errors.on(:<%= attribute.name %>).should_not == nil
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => 'scr"ipt' ))
    <%= singular_name %>.errors.on(:<%= attribute.name %>).should_not == nil
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => "script>" ))
    <%= singular_name %>.errors.on(:<%= attribute.name %>).should_not == nil
  end

<% elsif [:integer, :big_decimal, :float].member? attribute.type %>
  it "should be numerical <%= attribute.name %>" do
    <%= singular_name %> = <%= class_name %>.create(@valid_attributes.merge(:<%= attribute.name %> => "none-numberic" ))
    <%= singular_name %>.<%= attribute.name %>.to_i.should == 0
    <%= singular_name %>.errors.size.should == 1
  end

<% end -%>
<% end -%>
end
