require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/users/edit.html.erb" do
  include UsersHelper

  before(:each) do
    assigns[:user] = @user = stub_model(User,
      :new_record? => false,
      :login => "value for login",
      :name => "value for name",
      :email => "value for email",
      :language => "value for language"
    )
  end

  it "renders the edit user form" do
    render

    response.should have_tag("form[action=#{user_path(@user.key)}][method=post]") do
      with_tag('input#user_login[name=?]', "user[login]")
      with_tag('input#user_name[name=?]', "user[name]")
      with_tag('input#user_email[name=?]', "user[email]")
      with_tag('input#user_language[name=?]', "user[language]")
    end
  end
end
