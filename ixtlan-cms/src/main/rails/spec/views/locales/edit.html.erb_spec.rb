require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/locales/edit.html.erb" do
  include LocalesHelper

  before(:each) do
    assigns[:locale] = @locale = stub_model(Locale,
      :new_record? => false,
      :code => "value for code"
    )
  end

  it "renders the edit locale form" do
    render

    response.should have_tag("form[action=#{locale_path(@locale.key)}][method=post]") do
      with_tag('input#locale_code[name=?]', "locale[code]")
    end
  end
end
