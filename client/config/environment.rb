# Be sure to restart your server when you modify this file

# Specifies gem version of Rails to use when vendor/rails is not present
RAILS_GEM_VERSION = '2.3.2' unless defined? RAILS_GEM_VERSION

# Bootstrap the Rails environment, frameworks, and default configuration
require File.join(File.dirname(__FILE__), 'boot')

Rails::Initializer.run do |config|
  config.gem 'guard'
  config.gem 'erector'
  config.gem 'erector_widgets'
  config.gem 'datamapper4rail', :lib => 'datamapper4rails/adapters/restful_adapter'
  config.gem 'datamapper4rail', :lib => 'datamapper4rails'
  config.gem 'rspec-rails', :lib => false
  config.gem 'rspec', :lib => false
  config.gem 'extlib'
  config.gem 'dm-core'
  config.gem 'dm-constraints'
  config.gem 'dm-serializer'
  config.gem 'json_pure', :lib => 'json'
  config.gem 'dm-timestamps'
  config.gem 'dm-validations'
  config.gem 'dm-ldap-adapter', :lib => 'ldap/version'
  config.gem 'data_objects'
  config.gem 'do_sqlite3' unless RAILS_ENV == 'production'
  config.gem 'do_mysql' if RAILS_ENV == 'production'
  config.gem 'addressable', :lib => 'addressable/uri'
  config.gem 'logging'
  config.gem 'ixtlan'  

  # needed to build extensions !!
#  config.gem 'polyglot'
#  config.gem 'rubyforge'

  # Settings in config/environments/* take precedence over those specified here.
  # Application configuration should go into files in config/initializers
  # -- all .rb files in that directory are automatically loaded.

  # Add additional load paths for your own custom dirs
  # config.load_paths += %W( #{RAILS_ROOT}/extras )

  # Specify gems that this application depends on and have them installed with rake gems:install
  # config.gem "bj"
  # config.gem "hpricot", :version => '0.6', :source => "http://code.whytheluckystiff.net"
  # config.gem "sqlite3-ruby", :lib => "sqlite3"
  # config.gem "aws-s3", :lib => "aws/s3"

  # Only load the plugins named here, in the order given (default is alphabetical).
  # :all can be used as a placeholder for all plugins not explicitly named
  # config.plugins = [ :exception_notification, :ssl_requirement, :all ]

  # Skip frameworks you're not going to use. To use Rails without a database,
  # you must remove the Active Record framework.
  config.frameworks -= [ :active_record ]

  # Activate observers that should always be running
  # config.active_record.observers = :cacher, :garbage_collector, :forum_observer

  # Set Time.zone default to the specified zone and make Active Record auto-convert to this zone.
  # Run "rake -D time" for a list of tasks for finding time zone names.
  config.time_zone = 'UTC'

  # The default locale is :en and all translations from config/locales/*.rb,yml are auto loaded.
  # config.i18n.load_path += Dir[Rails.root.join('my', 'locales', '*.{rb,yml}')]
  # config.i18n.default_locale = :de
end
