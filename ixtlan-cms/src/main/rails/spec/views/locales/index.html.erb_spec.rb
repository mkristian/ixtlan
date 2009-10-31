require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/locales/index.html.erb" do
  include LocalesHelper

  before(:each) do
    assigns[:locales] = [
      stub_model(Locale,
        :code => "value for code"
      ),
      stub_model(Locale,
        :code => "value for code"
      )
    ]
  end

  it "renders a list of locales" do
    render
    response.should have_tag("tr>td", "value for code".to_s, 2)
  end
end
