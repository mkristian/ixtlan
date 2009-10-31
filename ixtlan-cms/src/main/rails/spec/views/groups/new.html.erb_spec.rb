require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/groups/new.html.erb" do
  include GroupsHelper

  before(:each) do
    assigns[:group] = stub_model(Group,
      :new_record? => true,
      :name => "value for name"
    )
  end

  it "renders new group form" do
    render

    response.should have_tag("form[action=?][method=post]", groups_path) do
      with_tag("input#group_name[name=?]", "group[name]")
    end
  end
end
