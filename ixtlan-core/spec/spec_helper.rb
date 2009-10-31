require 'rubygems'
require 'dm-core'
require 'dm-validations'
require 'dm-serializer'
$LOAD_PATH << (Pathname(__FILE__).dirname.parent.expand_path + 'lib').to_s

require 'dm-timestamps'
require 'slf4r'
require 'ixtlan' / 'user_logger'
require 'ixtlan' / 'modified_by'
require 'ixtlan' / 'user'
require 'ixtlan' / 'group_locale_user'
require 'ixtlan' / 'locale'
require 'ixtlan' / 'group'
require 'ixtlan' / 'group_user'
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
  attr_accessor :headers, :method, :content_type
  def initialize
    @headers = {}
    @method = :get
    @content_type = 'text/html'
  end
end
class Controller
  include ActionController::Base
  def initialize
    @params = {}
    u = Ixtlan::User.first
    if u.nil? 
      u = Ixtlan::User.new(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
      if(u.respond_to? :created_by_id)
      u.created_by_id = 1
      u.updated_by_id = 1
      end
      u.save!
    end
    @password = u.reset_password
    u.save!
    g = Ixtlan::Group.first(:name => :admin) || Ixtlan::Group.create(:name => :admin, :current_user => u)
    # clear up old relations
    Ixtlan::GroupUser.all.destroy!

    u.groups << g
    g.locales << Ixtlan::Locale.first_or_create(:code => "DEFAULT")
    g.locales << Ixtlan::Locale.first_or_create(:code => "en")
    g.save
    @user = u
  end

  attr_reader :params, :password, :user, :rendered

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

