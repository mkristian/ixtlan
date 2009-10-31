require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/groups/show.html.erb" do
  include GroupsHelper
  before(:each) do
    assigns[:group] = @group = stub_model(Group,
      :name => "value for name"
    )
  end

  it "renders attributes in <p>" do
    render
    response.should have_text(/value\ for\ name/)
  end
end
