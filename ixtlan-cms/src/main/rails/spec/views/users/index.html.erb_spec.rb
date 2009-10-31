require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/users/index.html.erb" do
  include UsersHelper

  before(:each) do
    assigns[:users] = [
      stub_model(User,
        :login => "value for login",
        :name => "value for name",
        :email => "value for email",
        :language => "value for language"
      ),
      stub_model(User,
        :login => "value for login",
        :name => "value for name",
        :email => "value for email",
        :language => "value for language"
      )
    ]
  end

  it "renders a list of users" do
    render
    response.should have_tag("tr>td", "value for login".to_s, 2)
    response.should have_tag("tr>td", "value for name".to_s, 2)
    response.should have_tag("tr>td", "value for email".to_s, 2)
    response.should have_tag("tr>td", "value for language".to_s, 2)
  end
end
