
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'ixtlan/controllers/texts_controller'

require 'ixtlan/models/text'
require 'ixtlan/models/word'
require 'ixtlan/models/translation'

describe "Ixtlan::Models::TextCollection" do
  
  len = 6

  before(:all) do
    Controller.send(:include, Ixtlan::Controllers::TextsController)
    @controller = Controller.new
    Ixtlan::Models::Text.all.destroy!
    (1..len).each do |i|
      locale = Ixtlan::Models::Locale.first_or_create(:code => "c#{(96 +i).chr}")
      text = Ixtlan::Models::Text.create(:code => "code_#{i}", :text => "text_#{i}",
:current_user => @controller.current_user, :locale => Ixtlan::Models::Locale.default, :updated_at => DateTime.now, :updated_by => @controller.current_user)
      text.approve(:current_user => @controller.current_user)
      (1..len).each do |j|
        text = Ixtlan::Models::Text.create(:code => "code_#{i}", :text => "text_#{i}_#{j}",
:current_user => @controller.current_user, :locale => locale, :updated_at => DateTime.now, :updated_by => @controller.current_user)
        
        text.approve(:current_user => @controller.current_user) unless j == len
      end
    end
  end

  it "should have #{len} second latest approved" do
    set = Ixtlan::Models::Text.second_latest_approved
    set.size.should == len
    set.each do |t|
      t.version.should_not be_nil
      t.current.should be_false
      t.previous.should be_true
    end
  end

  it "should have #{len * 2} latest approved" do
    set = Ixtlan::Models::Text.latest_approved
    set.size.should == len * 2
    set.each do |t|
      t.version.should_not be_nil
      t.current.should be_true
      t.previous.should be_false
    end
  end

  it "should have #{len} not approved" do
    set = Ixtlan::Models::Text.not_approved
    set.size.should == len
    set.each do |t|
      t.version.should be_nil
      t.current.should be_false
      t.previous.should be_false
    end
  end

  it "should have #{len * len} approved" do
    Ixtlan::Models::Text.approved.size.should == len * len
  end

  it "should have #{len * (len - 3)} old approved" do
    Ixtlan::Models::Text.all(:current => false, :previous => false, :version.not => nil).size.should == len * (len - 3)
  end

  it "should have right number of approved when filterd by locale " do
    locale = Ixtlan::Models::Locale.first_or_create(:code => "cb")
    Ixtlan::Models::Text.approved(:locale => locale).size.should == len - 1
    Ixtlan::Models::Text.latest_approved(:locale => locale).size.should == 1
    Ixtlan::Models::Text.not_approved(:locale => locale).size.should == 1
  end

  it 'should setup the translation map' do
    locale = Ixtlan::Models::Locale.first_or_create(:code => "cb")
    map = Ixtlan::Models::Translation.map_for(:locale => locale)
    map.size.should == 1
    map.values[0].to_xml.gsub(/ type='[a-z:]*'/, '').gsub(/[0-9-]+T[0-9:]+\+[0-9:]+/, 'date').should == "<translation><text>text_2_5</text><previous_text>text_2_4</previous_text><approved_at>date</approved_at><approved_by_id>1</approved_by_id></translation>"
  end
end