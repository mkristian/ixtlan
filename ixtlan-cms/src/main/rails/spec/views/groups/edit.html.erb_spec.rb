require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/groups/edit.html.erb" do
  include GroupsHelper

  before(:each) do
    assigns[:group] = @group = stub_model(Group,
      :new_record? => false,
      :name => "value for name"
    )
  end

  it "renders the edit group form" do
    render

    response.should have_tag("form[action=#{group_path(@group.key)}][method=post]") do
      with_tag('input#group_name[name=?]', "group[name]")
    end
  end
end
