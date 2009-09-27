require 'rubygems'
require 'dm-core'
require 'dm-validations'
require 'dm-serializer'
$LOAD_PATH << (Pathname(__FILE__).dirname.parent.expand_path + 'lib').to_s

require 'dm-timestamps'
require 'slf4r'
require 'ixtlan' / 'user_logger'
require 'ixtlan' / 'user'
require 'ixtlan' / 'group'
require 'ixtlan' / 'group_user'
require 'ixtlan' / 'locale'
require 'ixtlan' / 'permission'
require 'ixtlan' / 'role'
require 'ixtlan' / 'passwords'
require 'ixtlan' / 'digest'

#hide log output
Slf4r::LoggerFacade4RubyLogger.file = StringIO.new

module ActiveSupport
  class SecureRandom
    def self.random_number(max)
      rand(max)
    end
  end
end

module ActionController
  module Base
    def self.before_filter(filter)
      @filters ||= []
      @filters << filter
    end

    def self.filters
      @filters
    end
  end
end
class Request
  attr_accessor :headers, :method
  def initialize
    @headers = {}
    @method = :get
  end
end
class Controller
  include ActionController::Base

  def initialize
    @params = {}
    u = Ixtlan::User.first_or_create(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx")
    @password = u.reset_password
    u.save
    g = Ixtlan::Group.first_or_create(:name => :admin)
    u.groups << g
    g.locales << Ixtlan::Locale.first_or_create(:code => "DEFAULT")
    g.locales << Ixtlan::Locale.first_or_create(:code => "en")
    g.save
    @user = u
  end

  attr_reader :params, :password, :user

  def current_user
    @user
  end

  def render_session_timeout
    @rendered = true
  end

  def new_session_timeout
    DateTime.now
  end

  def session
    @session ||= {}
  end

  def request
    @request ||= Request.new
  end
end
# datamapper needs a default configured !!
DataMapper.setup(:default, :adapter => :in_memory)

