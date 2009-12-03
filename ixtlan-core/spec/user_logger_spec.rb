require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'ixtlan/rails/session_timeout'
class Response
  attr_accessor :content_type
  def initialize
    @content_type = ""
  end
end

class Controller
  def response
    @response ||= Response.new
  end
end

describe Ixtlan::UserLogger do

  before :all do
    @controller = Controller.new
  end

  before :each do
    @controller.response.content_type = "text/html"
    @log = StringIO.new
    Slf4r::LoggerFacade4RubyLogger.file = @log
    @logger = Ixtlan::UserLogger.new(:root)
  end

  it 'should log no user - no message - no block' do
    @logger.log_user(nil)
    @log.string.should =~ /\[\?\?\?\]\s*$/
  end

  it 'should log user - no message - no block' do
    @logger.log_user("user")
    @log.string.should =~ /\[user\]\s*$/
  end

  it 'should log user - message - no block' do
    @logger.log_user("user", "message")
    @log.string.should =~ /\[user\] message\s*$/
  end

  it 'should log user - no message - block' do
    @logger.log_user("user") { "block" }
    @log.string.should =~ /\[user\] block\s*$/
  end

  it 'should log - no message - block' do
    @logger.log(@controller) { "block" }
    @log.string.should =~ /\[marvin\] block\s*$/
  end

  it 'should log - message - no block' do
    @logger.log(@controller, "message")
    @log.string.should =~ /\[marvin\] message\s*$/
  end

  it 'should log - no message - no block' do
    @logger.log(@controller)
    @log.string.should =~ /\[marvin\] \s*$/
  end

  it 'should log - no controller - no message - no block' do
    @logger.log(Object.new)
    @log.string.should =~ /\[\?\?\?\] \s*$/
  end

  it 'should log action - no resource variable' do
    @controller.params[:controller] = "resources"
    @controller.params[:action] = "index"
    @logger.log_action(@controller)
    @log.string.should =~ /\[marvin\] resources#index\s*$/

    @controller.response.content_type = "application/xml"
    @logger.log_action(@controller)
    @log.string.should =~ /\[marvin\] resources#index - xml\s*$/
  end

  it 'should log action - resource variable' do
    @controller.params[:controller] = "resources"
    @controller.params[:action] = "index"
    @controller.instance_variable_set(:@resource, @controller.current_user.groups[0])
    @logger.log_action(@controller)
    @log.string.should =~ /\[marvin\] resources#index .*Group\([0-9]\)\s*$/
    @controller.response.content_type = "application/xml"
    @logger.log_action(@controller)
    @log.string.should =~ /\[marvin\] resources#index .*Group\([0-9]\) - xml\s*$/
  end

  it 'should log action - resources variable' do
    @controller.params[:controller] = "resources"
    @controller.params[:action] = "index"
    @controller.instance_variable_set(:@resources, @controller.current_user.groups)
    size = @controller.current_user.groups.size
    @logger.log_action(@controller)
    @log.string.should =~ /\[marvin\] resources#index .*Groups\[#{size}\]\s*$/
    @controller.response.content_type = "application/xml"
    @logger.log_action(@controller)
    @log.string.should =~ /\[marvin\] resources#index .*Groups\[#{size}\] - xml\s*$/
  end

end
