require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'


require 'dm-timestamps'
require 'ixtlan' / 'user'
require 'ixtlan' / 'group'
require 'ixtlan' / 'group_user'
require 'ixtlan' / 'locale'
require 'ixtlan' / 'permission'
require 'ixtlan' / 'role'

require 'logger'

module ActionController
  module Base
  end
end

module ActionView
  module Base
  end
end
module Erector
  class Widget
    
    attr_writer :controller
    
    class Helpers
      attr_accessor :controller
    end
    
    def helpers
      @helpers ||= Helpers.new
      @helpers.controller = @controller
      @helpers
    end
  end
end

require 'ixtlan' / 'guard'

class Controller 
  include ActionController::Base
end

describe Ixtlan::Permission do

  before :each do
    @role = Ixtlan::Role.new :name => 'root'
    @permission = Ixtlan::Permission.new :resource => "permissions", :action => "index"
    @permission.roles << @role
  end

  it "should produce xml" do
    @permission.to_xml.should == "<permission><resource>permissions</resource><action>index</action><roles type='array'><role><name>root</name></role></roles></permission>"
  end

end

class Controller 

  def initialize
    @params = {}
  end

  attr_reader :params

  def current_user
    u = Ixtlan::User.create(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "all" )
    g = Ixtlan::Group.create(:name => :admin)
    u.groups << g
    g.locales << Ixtlan::Locale.first_or_create(:code => "DEFAULT")
    g.locales << Ixtlan::Locale.first_or_create(:code => "en")
    g.save
p Ixtlan::Group.all[0].locales
    u
  end

end

describe Ixtlan::Guard do

  before :all do
    # datamapper needs a default configured !!
    DataMapper.setup(:default, :adapter => :in_memory)
  
    Ixtlan::Guard.load( Logger.new(STDOUT), :root, (Pathname(__FILE__).dirname + 'guards').expand_path )

    @controller = Controller.new
    @widget = Erector::Widget.new
    @widget.controller = @controller
  end

  it 'should export permissions' do
    Ixtlan::Guard.export_xml.should == "<permissions type='array'><permission><resource>configurations</resource><action>edit</action><roles type='array'><role><name>root</name></role></roles></permission><permission><resource>configurations</resource><action>show</action><roles type='array'><role><name>admin</name></role></roles></permission><permission><resource>configurations</resource><action>update</action><roles type='array'><role><name>root</name></role></roles></permission><permission><resource>permissions</resource><action>destroy</action><roles type='array'><role><name>root</name></role></roles></permission><permission><resource>permissions</resource><action>edit</action><roles type='array'><role><name>root</name></role><role><name>admin</name></role></roles></permission><permission><resource>permissions</resource><action>show</action><roles type='array'><role><name>guest</name></role></roles></permission><permission><resource>permissions</resource><action>update</action><roles type='array'><role><name>root</name></role><role><name>admin</name></role></roles></permission></permissions>"
  end

  it 'should allow' do
    Ixtlan::Guard.check(@controller, :permissions, :update).should be_true
  end

  it 'should disallow' do
    Ixtlan::Guard.check(@controller, :configurations, :update).should be_false
  end

  it 'should allow with locale' do
    Ixtlan::Guard.check(@controller, :permissions, :update, Ixtlan::Locale.first_or_create(:code => "en")).should be_true
  end

  it 'should disallow with locale' do
    Ixtlan::Guard.check(@controller, :configurations, :update, Ixtlan::Locale.first_or_create(:code => "en")).should be_false
  end

  it 'should raise GuardException on unknown controller' do
    lambda { Ixtlan::Guard.check(@controller, :unknown_resources, :update) }.should raise_error(Ixtlan::GuardException)
  end

  it 'should raise GuardException on unknown action' do
    lambda { Ixtlan::Guard.check(@controller, :permissions, :unknown_action) }.should raise_error(Ixtlan::GuardException)
  end

  it 'should pass controller guard' do
    @controller.params[:action] = :update
    @controller.params[:controller] = :permissions

    @controller.send(:guard).should be_true
  end

  it 'should controller guard deny permission' do
    @controller.params[:action] = :update
    @controller.params[:controller] = :configurations

    lambda {@controller.send(:guard)}.should raise_error( Ixtlan::PermissionDenied)
 end

  it 'should pass controller guard with locale' do
    @controller.params[:action] = :update
    @controller.params[:controller] = :permissions

    @controller.send(:guard, Ixtlan::Locale.first_or_create(:code => "en")).should be_true
  end

  it 'should controller guard deny permission with locale' do
    @controller.params[:action] = :update
    @controller.params[:controller] = :configurations

    lambda {@controller.send(:guard, Ixtlan::Locale.first_or_create(:code => "en"))}.should raise_error( Ixtlan::PermissionDenied)
  end

  it 'should controller guard deny permission with locale' do
    @controller.params[:action] = :update
    @controller.params[:controller] = :permissions

    lambda {@controller.send(:guard, Ixtlan::Locale.first_or_create(:code => "de"))}.should raise_error( Ixtlan::PermissionDenied)
 end

  it 'should allow with locale' do
    @widget.send(:allowed, :permissions, :update, Ixtlan::Locale.first_or_create(:code => "en")).should be_true
  end

  it 'should deny permission with locale' do
    @widget.send(:allowed, :configurations, :update, Ixtlan::Locale.first_or_create(:code => "en")).should be_false
  end
end
