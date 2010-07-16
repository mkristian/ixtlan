require 'rubygems'
lib_path = (Pathname(__FILE__).dirname.parent.expand_path + 'lib').to_s
$LOAD_PATH.unshift lib_path unless $LOAD_PATH.include?(lib_path)

require 'dm-core'
require 'dm-validations'
require 'dm-serializer'
require 'dm-timestamps'

require 'slf4r'

require 'ixtlan' / 'models'
require 'ixtlan' / 'user_logger'
require 'ixtlan' / 'modified_by'
require 'ixtlan' / 'models' / 'audit'
require 'ixtlan' / 'models' / 'domain'
require 'ixtlan' / 'models' / 'locale'
require 'ixtlan' / 'models' / 'group'
require 'ixtlan' / 'models' / 'user'
require 'ixtlan' / 'models' / 'configuration'
require 'ixtlan' / 'models' / 'group_user'
require 'ixtlan' / 'models' / 'group_locale_user'
require 'ixtlan' / 'models' / 'permission'
require 'ixtlan' / 'models' / 'role'
require 'ixtlan' / 'models' / 'i18n_text'
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
    u = Ixtlan::Models::User.first(:login => :marvin)
    if u.nil?
      u = Ixtlan::Models::User.new(:login => :marvin, :name => 'marvin the robot', :email=> "marvin@universe.example.com", :language => "xx", :id => 1, :created_at => DateTime.now, :updated_at => DateTime.now)
      if(u.respond_to? :created_by_id)
        u.created_by_id = 1
        u.updated_by_id = 1
      end
      u.save!
    end
    @password = u.reset_password
    u.save!
    g = Ixtlan::Models::Group.first(:name => :admin) || Ixtlan::Models::Group.create(:name => :admin, :current_user => u)
#p g.errors
#gg = Ixtlan::Models::Group.new(:name => :admin2, :current_user => u)
#gg.save
#p gg.errors
    # clear up old relations
    Ixtlan::Models::GroupUser.all.destroy!
    Ixtlan::Models::GroupLocaleUser.all.destroy!
    u.groups << g
    g.locales << Ixtlan::Models::Locale.default
    g.locales << Ixtlan::Models::Locale.first_or_create(:code => "en")
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
    if clazz =~ /::/
      clazz.sub!(/^::/, '')
      ref = ref.const_get(clazz.sub(/::.*/, ''))
      self.full_const_get(clazz.sub(/[^:]*::/, ''), ref)
    else
      ref.const_get(clazz)
    end
  end
end
