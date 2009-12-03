
require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'ixtlan' / 'models' / 'authentication'
require 'ixtlan' / 'rails' / 'unrestful_authentication'

class Controller

  include Ixtlan::Rails::UnrestfulAuthentication

  attr_reader :rendered

  def render_successful_login
    @rendered = :login
  end

  def render_access_denied
    @rendered = :access_denied
  end

  def render_login_page
    @rendered = :login_page
  end

  def render_logout_page
    @rendered = :logout_page
  end

  def reset
    @params.clear
    @rendered = nil
  end
end

describe Ixtlan::Models::Authentication do

  describe "login" do

    before :all do
      @controller = Controller.new
    end

    before :each do
      @log = StringIO.new
      Slf4r::LoggerFacade4RubyLogger.file = @log
      @controller.reset
    end

    it 'should show login page' do
      @controller.send(:authenticate).should be_false
      @controller.session.empty?.should be_true
      @controller.rendered.should == :login_page
    end

    it 'should show access denied page - via put' do
      @controller.request.method = :put
      @controller.send(:authenticate).should be_false
      @controller.session.empty?.should be_true
      @controller.rendered.should == :access_denied
    end

    it 'should show access denied page - via delete' do
      @controller.request.method = :delete
      @controller.send(:authenticate).should be_false
      @controller.session.empty?.should be_true
      @controller.rendered.should == :access_denied
    end

    it 'should show access denied page - via post - no login - no password' do
      @controller.request.method = :post
      @controller.send(:authenticate).should be_false
      @controller.session.empty?.should be_true
      @controller.rendered.should == :access_denied
    end

    it 'should show access denied page - via post - no login' do
      @controller.request.method = :post
      @controller.params[:password] = @controller.password
      @controller.send(:authenticate).should be_false
#      @log.string.should =~ /\[\?\?\?\] unknown login from IP/
      @controller.session.empty?.should be_true
      @controller.rendered.should == :access_denied
    end

    it 'should show access denied page - via post - no password' do
      @controller.request.method = :post
      @controller.params[:login] = @controller.user.login
      @controller.send(:authenticate).should be_false
#      @log.string.should =~ /\[#{@controller.user.login}\] wrong password from IP/
        @controller.session.empty?.should be_true
      @controller.rendered.should == :access_denied
    end

    it 'should login' do
      @controller.request.method = :post
      @controller.params[:password] = @controller.password
      @controller.params[:login] = @controller.user.login
      @controller.send(:authenticate).should be_false
      @log.string.should =~ /\[#{@controller.user.login}\] logged in/
      @controller.session.empty?.should be_false
      @controller.rendered.should == :login
    end
  end

  describe 'logout' do

    before :all do
      @controller = Controller.new
    end

    before :each do
      @log = StringIO.new
      Slf4r::LoggerFacade4RubyLogger.file = @log
      @controller.request.method = :post
      @controller.params[:password] = @controller.password
      @controller.params[:login] = @controller.user.login
      @controller.send :authenticate
      @controller.reset
    end

    it 'should do nothing' do
      @controller.request.method = :get
      @controller.send(:authenticate).should be_true
      @controller.request.method = :post
      @controller.send(:authenticate).should be_true
      @controller.request.method = :put
      @controller.send(:authenticate).should be_true
      @controller.request.method = :delete
      @controller.send(:authenticate).should be_true
      @log.string.should == ''
      @controller.rendered.should be_nil
    end

    it 'should logout' do
      @controller.request.method = :delete
      @controller.params[:login] = @controller.user.login
      @controller.send(:authenticate).should be_false
      @controller.rendered.should == :logout_page
      @log.string.should =~ /\[#{@controller.user.login}\] logged out/
    end
  end
end
