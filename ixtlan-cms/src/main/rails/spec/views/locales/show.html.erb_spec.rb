require File.expand_path(File.dirname(__FILE__) + '/../../spec_helper')

describe "/locales/show.html.erb" do
  include LocalesHelper
  before(:each) do
    assigns[:locale] = @locale = stub_model(Locale,
      :code => "value for code"
    )
  end

  it "renders attributes in <p>" do
    render
    response.should have_text(/value\ for\ code/)
  end
end
