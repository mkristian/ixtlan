# inspired by http://www.rowtheboat.com/archives/32
###################################################

# get all datamapper related gems
gem 'addressable', :lib => 'addressable/uri'

# assume sqlite3 to be database
gem 'do_sqlite3'

# serialization, validations and timestamps in your models
gem 'dm-validations'
gem 'dm-timestamps'
gem 'dm-migrations'
gem 'dm-serializer' # to allow xml interface to work

# assume you prefer rspec over unit tests
gem 'rspec', :lib => false
gem 'rspec-rails', :lib => false

# this pulls in rails_datamapper and rack_datamapper
gem 'datamapper4rails'

# logging
gem 'slf4r'
gem 'logging'

# ixtlan gems
gem 'ixtlan', :lib => 'guard'
gem 'ixtlan', :lib => 'unrestful_authentication'
gem 'ixtlan', :lib => 'session_timeout'
gem 'ixtlan', :lib => 'audit'
gem 'ixtlan', :lib => 'optimistic_persistence'
gem 'ixtlan', :lib => 'error_handling'
gem 'ixtlan', :lib => 'models'

# install all gems
rake 'gems:install'

# install specs rake tasks
generate('rspec', '-f')

# install datamapper rake tasks
generate('datamapper_install')

# fix config files to work with datamapper instead of active_record
environment ''
environment 'config.frameworks -= [ :active_record ]'
environment '# deactive active_record'
gsub_file 'spec/spec_helper.rb', /^\s*config[.]/, '  #\0'
gsub_file 'test/test_helper.rb', /^[^#]*fixtures/, '  #\0'

file 'spec/support/datamapper.rb', <<-CODE
require 'datamapper4rails/rspec'
CODE

# add middleware
def middleware(name)
  log 'middleware', name
  environment "config.middleware.use '#{name}'"
end

environment ''
middleware 'DataMapper::RestfulTransactions'
middleware 'DataMapper::IdentityMaps'
middleware 'Rack::Deflater'
environment "#config.middleware.use 'Ixtlan::CmsScript'"
environment '# add middleware'

# init a session store
initializer 'datamapper_store.rb', <<-CODE
# init a session store which uses a memory cache and drops the user object 
# and the flash which results into a very thin session and hardly any 
# database updates !
# cleanup can be a problem. jruby uses soft-references for the cache so
# memory cleanup with jruby is no problem.
require 'ixtlan/session'
ActionController::Base.session_store = :datamapper_store
ActionController::Base.session = {
  :cache       => true,
  :session_class => Ixtlan::Session
}
CODE

# gzip fix for jruby
initializer 'monkey_patches.rb', <<-CODE
if RUBY_PLATFORM =~ /java/
  require 'zlib'
  class Zlib::GzipWriter
    def <<(arg)
      write(arg)
    end
  end
end
# fix with rails development mode and class reloading
# not sure where the exact problem is :-(
module Extlib
  module Assertions
    def assert_kind_of(name, value, *klasses)
      # be less strict and allow matching class names to OK as well
      klasses.each { |k| return if value.kind_of?(k) or value.class.name == k.name }     
      raise ArgumentError, "+\#{name}+ should be \#{klasses.map { |k| k.name } * ' or '}, but was \#{value.class.name}", caller(2)
    end
  end
end
CODE

rake 'db:automigrate', "-r config/environment.rb"

# logger config
initializer '01_loggers.rb', <<-CODE
require 'ixtlan/logger_config' if ENV['RAILS_ENV']
CODE

# load the guard config
initializer '02_guard.rb', <<-CODE
# load the guard config files from RAILS_ROOT/app/guards
Ixtlan::Guard.load(Slf4r::LoggerFacade.new(Ixtlan::Guard))
CODE

# setup permissions controller
file "app/controllers/permissions_controller.rb", <<-CODE
class PermissionsController < ApplicationController

  def index
     render :xml => Ixtlan::Guard.export_xml
  end

end
CODE
route "map.resources :permissions"

file "db/migrate/1_create_root_user.rb", <<-CODE
migration 1, :create_root_user do
  up do
    Ixtlan::User.auto_migrate!
    Ixtlan::Locale.auto_migrate!
    Ixtlan::Group.auto_migrate!
    Ixtlan::GroupUser.auto_migrate!

    u = Ixtlan::User.new(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :language => 'en', :id => 1)
    #u.current_user = u
    u.created_at = DateTime.now
    u.updated_at = u.created_at
    u.created_by_id = 1
    u.updated_by_id = 1
    u.reset_password
    u.save!
    g = Ixtlan::Group.create(:name => 'root', :current_user => u)
    u.groups << g
    u.save
    STDERR.puts "\#{u.login} \#{u.password}"
  end

  down do
  end
end
CODE

file "db/migrate/2_create_configuration.rb", <<-CODE
migration 2, :create_configuration do
  up do
    Ixtlan::Configuration.auto_migrate!
    Ixtlan::Configuration.create(:session_idle_timeout => 10, :keep_audit_logs => 3, :current_user => Ixtlan::User.first)
  end

  down do
  end
end
CODE

file "app/views/sessions/login.html.erb", <<-CODE
<form method="post">
 
  <p>
    <label for="login">login</label><br />
    <input type="text" name="login" />
  </p>
  <p>
    <label for="password">password</label><br />
    <input type="password" name="password" />
  </p>
  <p>
    <input type="submit" value="login" />
  </p>
</form>
CODE

file 'app/guards/permissions_guard.rb', <<-CODE
Ixtlan::Guard.initialize(:permissions, {:index => []})
CODE

gsub_file 'app/controllers/application_controller.rb', /^\s*helper.*/, <<-CODE
  filter_parameter_logging :password, :login
  before_filter :check_session_expiry

  # override default to use nice User (without namespace)
  def login_from_params
    User.authenticate(params[:login], params[:password])
  end

  def login_from_session
    User.get(session[:user_id])
  end

  # override default to use value from configuration
  def new_session_timeout
    Ixtlan::Configuration.instance.session_idle_timeout.minutes.from_now
  end

  # needs 'optimistic_persistence'
  rescue_from DataMapper::StaleResourceError, :with => :stale_resource

  # needs 'guard'
  rescue_from Ixtlan::GuardException, :with => :page_not_found
  rescue_from Ixtlan::PermissionDenied, :with => :page_not_found

  # rest is standard rails or datamapper
  rescue_from DataMapper::ObjectNotFoundError, :with => :page_not_found
  rescue_from ActionController::RoutingError, :with => :page_not_found
  rescue_from ActionController::UnknownAction, :with => :page_not_found
  rescue_from ActionController::MethodNotAllowed, :with => :page_not_found
  rescue_from ActionController::NotImplemented, :with => :page_not_found

  # have nice stacktraces in development mode
  unless consider_all_requests_local
    rescue_from ActionView::MissingTemplate, :with => :internal_server_error
    rescue_from ActionView::TemplateError, :with => :internal_server_error
  end
CODE

file 'app/views/errors/error.html.erb', <<-CODE
<h1><%= @notice %></h1>
CODE

file 'app/views/errors/stale.html.erb', <<-CODE
<h1>stale resource</h1>

<p>please reload resource and change it again</p>
CODE

file 'config/preinitializer.rb', <<-CODE
require 'yaml'
require 'erb'
module Ixtlan
  class Configurator

    def self.symbolize_keys(h, compact)
      result = {}
      
      h.each do |k, v|
        v = ' ' if v.nil?
        if v.is_a?(Hash)
          result[k.to_sym] = symbolize_keys(v, compact) unless compact and v.size == 0
        else
          result[k.to_sym] = v unless compact and k.to_sym == v.to_sym
        end
      end
      
      result
    end
    
    def self.load(dir, file, compact = true)
      symbolize_keys(YAML::load(ERB.new(IO.read(File.join(dir, file))).result), compact)
    end
  end
end

CONFIG = Ixtlan::Configurator.load(File.dirname(__FILE__), 'global.yml')
CODE

file 'config/global.yml', <<-CODE
# possible example to have a file with all the (production) passwords
# outside GIT/SUBVERSION/CVS/etc !!!
mysql:
  database: example
  host: mysql.example.com
  username: rails
  password: run_it

smtp:
  address: smtp.gmail.com
  port: 587
  domain: example.com
  authentication: plain
  user_name: mail@example.com
  password: mail_it
CODE

append_file 'config/environments/production.rb', <<-CODE

config.action_mailer.delivery_method = :smtp

require "smtp_tls"

ActionMailer::Base.smtp_settings = {
:address => CONFIG[:smtp][:address],
:port => CONFIG[:smtp][:port],
:domain => CONFIG[:smtp][:domain],
:authentication => CONFIG[:smtp][:authentication],
:user_name => CONFIG[:smtp][:user_name],
:password => CONFIG[:smtp][:password]
} 
CODE

file 'prepare_jruby.sh', <<-CODE
#!/bin/bash

echo
echo "shall freeze rails and fix a bug which prevents rails to use certain"
echo "java gems like the dataobjects drivers !!"
echo

mvn --version
if [ $? -ne 0 ] ; then

        echo "please install maven >= 2.0.9 from maven.apache.org"
        exit -1
fi

mvn de.saumya.mojo:rails-maven-plugin:rails-freeze-gems de.saumya.mojo:rails-maven-plugin:gems-install

echo
echo "you can run rails with (no need to install jruby !!)"
echo
echo "\tmvn de.saumya.mojo:rails-maven-plugin:server"
echo
echo "more info on"
echo "\thttp://github.org/mkristian/rails-maven-plugin"
echo
echo
CODE

generate 'ixtlan_datamapper_rspec_scaffold', '--skip-migration', 'User', 'login:string', 'name:string', 'email:string', 'language:string'
file 'app/models/user.rb', <<-CODE
class User < Ixtlan::User; end
CODE
gsub_file 'spec/models/user_spec.rb', /.*:name => "sc'?r&?ipt".*/, ''
gsub_file 'spec/models/user_spec.rb', /value for login/, 'valueForLogin'
gsub_file 'spec/models/user_spec.rb', /value for email/, 'value@for.email'
gsub_file 'spec/models/user_spec.rb', /value for language/, 'vl'

generate 'ixtlan_datamapper_rspec_scaffold', '--skip-migration', 'Group', 'name:string'
file 'app/models/group.rb', <<-CODE
class Group < Ixtlan::Group; end
CODE


generate 'ixtlan_datamapper_rspec_scaffold', '--skip-migration', '--skip-modified-by', 'Locale', 'code:string'
file 'app/models/locale.rb', <<-CODE
class Locale < Ixtlan::Locale; end
CODE
gsub_file 'spec/models/locale_spec.rb', /value for code/, 'vc'
file 'spec/support/locale.rb', <<-CODE
module Ixtlan
  class Locale
    property :id, Integer
  end
end
CODE

rake 'db:migrate:down VERSION=0'
rake 'db:migrate'
