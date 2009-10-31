require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/locales/new.html.erb" do
  include LocalesHelper

  before(:each) do
    assigns[:locale] = stub_model(Locale,
      :new_record? => true,
      :code => "value for code"
    )
  end

  it "renders new locale form" do
    render

    response.should have_tag("form[action=?][method=post]", locales_path) do
      with_tag("input#locale_code[name=?]", "locale[code]")
    end
  end
end
