require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/users/show.html.erb" do
  include UsersHelper
  before(:each) do
    assigns[:user] = @user = stub_model(User,
      :login => "value for login",
      :name => "value for name",
      :email => "value for email",
      :language => "value for language"
    )
  end

  it "renders attributes in <p>" do
    render
    response.should have_text(/value\ for\ login/)
    response.should have_text(/value\ for\ name/)
    response.should have_text(/value\ for\ email/)
    response.should have_text(/value\ for\ language/)
  end
end
