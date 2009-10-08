require 'pathname'
require Pathname(__FILE__).dirname + 'spec_helper.rb'

require 'session_timeout'

describe Ixtlan::SessionTimeout do

  before :each do
    @controller = Controller.new
    @log = StringIO.new
    Slf4r::LoggerFacade4RubyLogger.file = @log
  end

  it 'should expire due to idle timeout' do
    @controller.send(:check_session_expiry).should be_true
    @controller.session.empty?.should be_false
    @controller.rendered.should be_nil
    @log.string.size.should == 0
    sleep 1
    @controller.send(:check_session_expiry).should be_false
    @controller.session.empty?.should be_true
    @controller.rendered.should be_true
    @log.string.size.should_not == 0
    @log.string.should =~ /session timeout/
  end

  it 'should expire due to IP change' do
    @controller.request.headers['REMOTE_ADDR'] = "first.ip"
    @controller.send(:check_session_ip_binding).should be_true
    @controller.session.empty?.should be_false
    @controller.rendered.should be_nil
    @log.string.size.should == 0
    @controller.request.headers['REMOTE_ADDR'] = "second.ip"
    @controller.send(:check_session_ip_binding).should be_false
    @controller.session.empty?.should be_true
    @controller.rendered.should be_true
    @log.string.size.should_not == 0
    @log.string.should =~ /IP changed/
  end

  it 'should expire due to IP change' do
    @controller.request.headers['HTTP_USER_AGENT'] = "mozilla"
    @controller.send(:check_session_browser_signature).should be_true
    @controller.session.empty?.should be_false
    @controller.rendered.should be_nil
    @log.string.size.should == 0
    @controller.request.headers['HTTP_USER_AGENT'] = "iron"
    @controller.send(:check_session_browser_signature).should be_false
    @controller.session.empty?.should be_true
    @controller.rendered.should be_true
    @log.string.size.should_not == 0
    @log.string.should =~ /browser signature changed/
  end
end
