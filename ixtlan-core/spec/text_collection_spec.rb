
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'ixtlan/models/i18n_text'
require 'ixtlan/models/word'
require 'ixtlan/models/translation'

describe "Ixtlan::Models::TextCollection" do

  len = 6

  before(:all) do
    current_user = User.first
    Locale.all.destroy!
    Locale.create(:id => 1, :code => "DEFAULT", :current_user => current_user)
    I18nText.all.destroy!
    (1..len).each do |i|
      locale = Locale.first(:id => 10 + len + i, :code => "c#{(96 +i).chr}") || Locale.create(:id => 10 + len + i, :code => "c#{(96 +i).chr}", :current_user => current_user) 
      text = I18nText.create(:code => "code_#{i}",
                                             :text => "text_#{i}",
                                             :current_user => current_user,
                                             :locale => Locale.default,
                                             :updated_at => DateTime.now,
                                             :updated_by => current_user)
      text.approve(:current_user => current_user)
      (1..len).each do |j|
        text = I18nText.create(:code => "code_#{i}", :text => "text_#{i}_#{j}",
:current_user => current_user, :locale => locale, :updated_at => DateTime.now, :updated_by => current_user)
        text.approve(:current_user => current_user) unless j == len
      end
    end
  end

  it "should have #{len} second latest approved" do
    set = I18nText.second_latest_approved
    set.size.should == len
    set.each do |t|
      t.version.should_not be_nil
      t.current.should be_false
      t.previous.should be_true
    end
  end

  it "should have #{len * 2} latest approved" do
    set = I18nText.latest_approved
    set.size.should == len * 2
    set.each do |t|
      t.version.should_not be_nil
      t.current.should be_true
      t.previous.should be_false
    end
  end

  it "should have #{len} not approved" do
    set = I18nText.not_approved
    set.size.should == len
    set.each do |t|
      t.version.should be_nil
      t.current.should be_false
      t.previous.should be_false
    end
  end

  it "should have #{len * len} approved" do
    I18nText.approved.size.should == len * len
  end

  it "should have #{len * (len - 3)} old approved" do
    I18nText.all(:current => false, :previous => false, :version.not => nil).size.should == len * (len - 3)
  end

  it "should have right number of approved when filterd by locale " do
    locale = Locale.first_or_create(:code => "cb")
    I18nText.approved(:locale => locale).size.should == len - 1
    I18nText.latest_approved(:locale => locale).size.should == 1
    I18nText.not_approved(:locale => locale).size.should == 1
  end

  it 'should setup the translation map' do
    locale = Locale.first_or_create(:code => "cb")
    map = Ixtlan::Models::Translation.map_for(:locale => locale)
    map.size.should == 1
    map.values[0].to_xml.gsub(/ type='[a-z:]*'/, '').gsub(/[0-9-]+T[0-9:]+\+[0-9:]+/, 'date').should == "<translation><text>text_2_5</text><previous_text>text_2_4</previous_text><approved_at>date</approved_at><approved_by_id>1</approved_by_id></translation>"
  end
end
