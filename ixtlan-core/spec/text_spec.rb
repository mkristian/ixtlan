
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'ixtlan/controllers/texts_controller'

require 'ixtlan/models/i18n_text'
require 'ixtlan/models/word'
require 'ixtlan/models/translation'

describe Ixtlan::Models::I18nText do

  before(:each) do
    @controller = Controller.new
    Ixtlan::Models::Text.all.destroy!
    @approved = Ixtlan::Models::I18nText.create(:code => "code", :text => "other text", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.default)
    @approved.approve(:current_user => @controller.current_user)
    @text = Ixtlan::Models::Text.create(:code => "code", :text => "text", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.default)
  end
  
  it "should create default" do
    @text.new?.should_not be_nil
    @text.id.should_not be_nil
    @text.updated_at.should_not be_nil
    @text.updated_by.should == @controller.current_user
    @text.version.should be_nil
    @text.current.should be_false
    @text.approved_at.should be_nil
    @text.approved_by.should be_nil
  end

  it 'should create translation' do
    Ixtlan::Models::I18nText.create(:code => "code", :text => "text_en", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.first_or_create(:code => 'en')).new?.should be_false
  end

  it 'should not create' do
     Ixtlan::Models::I18nText.create(:code => "code2", :text => "text", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.first_or_create(:code => 'en')).new?.should be_true
  end

  it 'should update' do
    text = Ixtlan::Models::I18nText.create(:code => "update_code", :text => "text", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.default)
    text.update(:text => 'new text', :current_user => @controller.current_user)
    text.should be_true
    text.version.should be_nil
    text.current.should be_false
    text.approved_at.should be_nil
    text.approved_by.should be_nil
  end

  it 'should approve' do
    text = Ixtlan::Models::I18nText.create(:code => "apporved_code", :text => "text", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.default)
    text.approved?.should be_false
    text.approve(:current_user => @controller.current_user).should be_true
    text.version.should_not be_nil
    text.approved?.should be_true
    text.current.should be_true
    text.approved_at.should_not be_nil
    text.approved_by.should_not be_nil
  end

  it 'should not update unapproved when changing text and version' do
    @text.attributes = {:text => 'new text', :version => 1, :current_user => @controller.current_user}
    @text.save.should be_false
    @text.errors[:invariant].first.should == 'can not approve and change text at the same time'
  end

  it 'should not update approved on text change' do

    @approved.version.should_not be_nil
    @approved.approved_at.should_not be_nil
    @approved.approved_by.should_not be_nil

    @approved.update(:text => 'new text', :current_user => @controller.current_user).should be_false
    @approved.errors[:invariant].should_not be_nil
  end

  it 'should not update approved on version change' do
    @approved.version.should_not be_nil
    @approved.approved_at.should_not be_nil
    @approved.approved_by.should_not be_nil

    @approved.update(:version => 11, :current_user => @controller.current_user).should be_false
    @approved.errors[:invariant].should_not be_nil
  end

  it 'should not update approved on code change' do
    @approved.version.should_not be_nil
    @approved.approved_at.should_not be_nil
    @approved.approved_by.should_not be_nil

    @approved.update(:code => "new code", :current_user => @controller.current_user).should be_false
    @approved.errors[:invariant].should_not be_nil
  end

  it 'should not update on code change' do
    @text.update(:code => "new code", :current_user => @controller.current_user).should be_false
    @text.errors[:invariant].should_not be_nil
  end

  it 'should produce xml via the Word model' do
    text = Ixtlan::Models::Text.create(:code => "word", :text => "word text", :current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.default)
    Ixtlan::Models::Word.first(:code => "word").to_xml.should == "<word><code>word</code><text>word text</text></word>"
    Ixtlan::Models::Word.all(:id => @approved.id).to_xml(:collection_element_name => "words").should == "<words type='array'><word><code>code</code><text>other text</text></word></words>"
  end
end
