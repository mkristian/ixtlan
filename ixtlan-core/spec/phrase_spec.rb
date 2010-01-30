require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require Pathname(__FILE__).dirname  + '../lib/dm-serializer/to_xml'


require 'ixtlan/controllers/texts_controller'

require 'ixtlan/models/i18n_text'
require 'ixtlan/models/word'
require 'ixtlan/models/translation'
require 'ixtlan/models/phrase'

def setup(code)
  len = 6
  Controller.send(:include, Ixtlan::Controllers::TextsController)
  @controller2 = Controller.new
  @en =  Ixtlan::Models::Locale.first_or_create(:code => "en")
  @en_in =  Ixtlan::Models::Locale.first_or_create(:code => "en_IN")
  (1..len).each do |j|
    text = Ixtlan::Models::I18nText.create(:code => code, 
                                       :text => "text_#{j}",
                                       :current_user => @controller2.current_user, 
                                       :locale => Ixtlan::Models::Locale.default, 
                                       :updated_at => DateTime.now, 
                                       :updated_by => @controller2.current_user)
    
    text.approve(:current_user => @controller2.current_user)
  end
end

#describe Ixtlan::Models::Phrase do


describe "with default locale" do
  before(:each) do
    setup("code_1")
  end
  it "should xml-serialize" do
    #Ixtlan::Models::Text.all.each {|t| p t}
    Ixtlan::Models::Phrase.all(:code => "code_1").to_xml.cleanup.should == "<phrases><phrase><code>code_1</code><text>text_6</text><current_text>text_6</current_text><updated_at>date</updated_at><updated_by_id>1</updated_by_id><locale><code>DEFAULT</code><created_at>date</created_at></locale></phrase></phrases>" 
  end
end

describe "with default locale" do
  before(:each) do
    setup("code_2")
  end
  it "should xml-serialize with edited text" do
    Ixtlan::Models::I18nText.create(:code => "code_2", 
                                :text => "text_edited",
                                :current_user => @controller2.current_user, 
                                :locale => Ixtlan::Models::Locale.default, 
                                :updated_at => DateTime.now, 
                                :updated_by => @controller2.current_user)
    # Ixtlan::Models::Text.all.each {|t| p t}
    Ixtlan::Models::Phrase.all(:code => "code_2").to_xml.cleanup.should == "<phrases><phrase><code>code_2</code><text>text_edited</text><current_text>text_6</current_text><updated_at>date</updated_at><updated_by_id>1</updated_by_id><locale><code>DEFAULT</code><created_at>date</created_at></locale></phrase></phrases>" 
  end

end
describe "with default locale" do
  before(:each) do
    setup("code_3")
    Ixtlan::Models::I18nText.all.destroy!
  end
  
  it "should xml-serialize with edited text and not approved" do
    Ixtlan::Models::I18nText.create(:code => "code_3", 
                                :text => "text_edited",
                                :current_user => @controller2.current_user, 
                                :locale => Ixtlan::Models::Locale.default, 
                                :updated_at => DateTime.now, 
                                :updated_by => @controller2.current_user)
    #Ixtlan::Models::Text.all.each {|t| p t}
    Ixtlan::Models::Phrase.all(:code => "code_3").to_xml.cleanup.should == "<phrases><phrase><code>code_3</code><text>text_edited</text><current_text>text_edited</current_text><updated_at>date</updated_at><updated_by_id>1</updated_by_id><locale><code>DEFAULT</code><created_at>date</created_at></locale></phrase></phrases>" 
  end
end

describe "with 'de' locale" do
  before(:each) do
    setup("de_code")
  end
  it "should xml-serialize" do
    #Ixtlan::Models::Text.all.each {|t| p t}
    Ixtlan::Models::Phrase.all(:code => "de_code", :locale => Ixtlan::Models::Locale.first_or_create(:code => 'de')).to_xml.cleanup.should == "<phrases><phrase><code>de_code</code><text>text_6</text><current_text>text_6</current_text><updated_at>date</updated_at><updated_by_id>1</updated_by_id><default_translation><text>text_6</text><previous_text>text_5</previous_text><approved_at>date</approved_at><approved_by_id>1</approved_by_id></default_translation><locale><code>de</code><created_at>date</created_at></locale></phrase></phrases>" 
  end
end

describe "with 'en' locale" do
  before(:each) do
    setup("en_code")

  Ixtlan::Models::I18nText.create(:code => "en_code", 
                              :text => "en_text_edited",
                              :current_user => @controller2.current_user, 
                              :locale => @en, 
                              :updated_at => DateTime.now, 
                              :updated_by => @controller2.current_user)
  Ixtlan::Models::I18nText.create(:code => "en_code", 
                              :text => "en_in_text_edited",
                              :current_user => @controller2.current_user, 
                              :locale => @en_in, 
                              :updated_at => DateTime.now, 
                              :updated_by => @controller2.current_user)
  end
  
  it "should xml-serialize with edited text" do
    #Ixtlan::Models::Text.all(:code => "en_code").each {|t| puts "code=#{t.code} locale=#{t.locale.code} version=#{t.version} current=#{t.current} previous=#{t.previous}"}
    Ixtlan::Models::Phrase.all(:code => "en_code", :locale => @en).to_xml.cleanup.should == "<phrases><phrase><code>en_code</code><text>en_text_edited</text><current_text>en_text_edited</current_text><updated_at>date</updated_at><updated_by_id>1</updated_by_id><default_translation><text>text_6</text><previous_text>text_5</previous_text><approved_at>date</approved_at><approved_by_id>1</approved_by_id></default_translation><locale><code>en</code><created_at>date</created_at></locale></phrase></phrases>" 
  end

end


#end
