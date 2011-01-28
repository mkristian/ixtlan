require 'rubygems'
lib_path = (Pathname(__FILE__).dirname.parent.expand_path + 'lib').to_s
$LOAD_PATH.unshift lib_path unless $LOAD_PATH.include?(lib_path)

require 'extlib'
require 'dm-core'
require 'dm-validations'
require 'dm-serializer'
require 'dm-timestamps'

require 'slf4r'
require 'slf4r/ruby_logger'

require 'ixtlan/models'
require 'ixtlan/modified_by'
require 'ixtlan/passwords'
require 'ixtlan/digest'

require 'ixtlan/models/locale'
class Locale
  include Ixtlan::Models::Locale
end
require 'ixtlan/models/domain'
class Domain
  include Ixtlan::Models::Domain
end
require 'ixtlan/models/group'
class Group
  include Ixtlan::Models::Group
end
require 'ixtlan/models/user'
class User
  include Ixtlan::Models::User
end
require 'ixtlan/models/configuration'
class Configuration
  include Ixtlan::Models::Configuration
end
require 'ixtlan/models/audit'
class Audit
  include Ixtlan::Models::Audit
end
require 'ixtlan/models/group_user'
require 'ixtlan/models/domain_group_user'
require 'ixtlan/models/group_locale_user'
require 'ixtlan/models/role'
require 'ixtlan/models/permission'
require 'ixtlan/models/i18n_text'
class I18nText
  include Ixtlan::Models::I18nText
end

#hide log output
Slf4r::LoggerFacade4RubyLogger.file = StringIO.new unless defined?(JRUBY_VERSION)


require 'ixtlan/user_logger'

module ActiveSupport
  class SecureRandom
    def self.random_number(max)
      rand(max)
    end
  end
end

module ActionController
  module Base

    def self.prepend_before_filter(*args)
    end

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
    u = User.first(:login => :marvin)
    if u.nil?
      u = User.new(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 111, :created_at => DateTime.now, :updated_at => DateTime.now)
      if(u.respond_to? :created_by_id)
        u.created_by_id = 111
        u.updated_by_id = 111
      end
      u.save!
    end
    @password = u.reset_password
    u.save!
    g = Group.first(:name => :admin) || Group.create(:name => :admin, :current_user => u)
#p g.errors
#gg = Ixtlan::Models::Group.new(:name => :admin2, :current_user => u)
#gg.save
#p gg.errors
    # clear up old relations
    Ixtlan::Models::GroupUser.all.destroy!
    Ixtlan::Models::GroupLocaleUser.all.destroy!
    u.groups << g
    g.locales << Locale.default
    g.locales << (Locale.first(:code => "en") || Locale.create(:code => "en", :current_user => u))
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

  def cache_headers(*args)
    p *args
  end

  def session_timeout
    a = Object.new
    def a.minutes
      b = Object.new
      def b.from_now
        DateTime.now
      end
      b
    end
    a
  end

  def session
    @session ||= {}
  end

  def request
    @request ||= Request.new
  end
end

DataMapper.setup(:default, :adapter => :in_memory)
root  = User.new(:login => "marvin2", :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
if(root.respond_to? :created_by_id)
  root.created_by_id = 1
  root.updated_by_id = 1
end
root.save!

Locale.create(:code => "DEFAULT", :current_user => root)
Locale.create(:code => "ALL", :current_user => root)
Domain.create(:name => "ALL", :current_user => root)

class String
  def cleanup
    gsub(/ type='[a-z:]*'/, '').gsub(/[0-9-]+T[0-9:]+\+[0-9:]+/, 'date')
  end
end

if RUBY_PLATFORM =~ /java/
  module DataMapper
    module Validate
      class NumericValidator

        def validate_with_comparison(value, cmp, expected, error_message_name, errors, negated = false)
          return if expected.nil?
          if cmp == :=~
              return value =~ expected
          end
          comparison = value.send(cmp, expected)
          return if negated ? !comparison : comparison

          errors << ValidationErrors.default_error_message(error_message_name, field_name, expected)
        end
      end
    end
  end
end

class DateTime

  alias :to_s_old :to_s
  def to_s(format = nil)
    to_s_old
  end

end

class Object
  def self.full_const_get(clazz, ref = Object)
    clazz = clazz.dup
    if clazz =~ /::/
      clazz.sub!(/^::/, '')
      ref = ref.const_get(clazz.sub(/::.*/, ''))
      self.full_const_get(clazz.sub(/[^:]*::/, ''), ref)
    else
      ref.const_get(clazz)
    end
  end
end
