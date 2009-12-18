# inspired by http://www.rowtheboat.com/archives/32
###################################################

# ixtlan gems
gem 'ixtlan', :version => '0.2.1'

# this pulls in rack_datamapper
gem 'datamapper4rails', :version => '0.4.0'

# assume sqlite3 to be database
gem 'do_sqlite3'
DM_VERSION='0.10.2'
# serialization, validations and timestamps in your models
gem 'dm-validations', :version => DM_VERSION
gem 'dm-timestamps', :version => DM_VERSION
gem 'dm-migrations', :version => DM_VERSION
gem 'dm-core', :version => DM_VERSION

# get all datamapper related gems
gem 'addressable', :lib => 'addressable/uri'

# assume you prefer rspec over unit tests
gem 'rspec', :lib => false
gem 'rspec-rails', :lib => false

# logging
gem 'slf4r'
gem 'logging'

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
environment "DM_VERSION='#{DM_VERSION}'"

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

# define locale model names
initializer '00_models.rb', <<-CODE
module Ixtlan
  module Models
    AUTHENTICATION = "Authentication"
    USER = "User"
    GROUP = "Group"
    LOCALE = "Locale"
    TEXT = "I18nText"
    CONFIGURATION = "Configuration"
  end
end
CODE

# load ixtlan classes
initializer '01_ixtlan.rb', <<-CODE
require 'ixtlan/modified_by'
if ENV['RAILS_ENV']
  require 'models'
  require 'ixtlan/rails/error_handling'
  require 'ixtlan/rails/audit'
  require 'ixtlan/rails/session_timeout'
  require 'ixtlan/rails/unrestful_authentication'
  require 'ixtlan/rails/guard'
  require 'ixtlan/rails/timestamps_modified_by_filter'
  require 'ixtlan/optimistic_persistence'
  require 'ixtlan/monkey_patches'
end
CODE

# logger config
initializer '02_loggers.rb', <<-CODE
require 'ixtlan/logger_config' if ENV['RAILS_ENV']
CODE

# load the guard config
initializer '03_guard.rb', <<-CODE
# load the guard config files from RAILS_ROOT/app/guards
Ixtlan::Guard.load(Slf4r::LoggerFacade.new(Ixtlan::Guard))
CODE

# define the date/time pattern for the xml
initializer 'time_formats.rb', <<-CODE
Time::DATE_FORMATS[:xml] = lambda { |time| time.utc.strftime("%Y-%m-%d %H:%M:%S") }
CODE

# setup migration for the main models
file "db/migrate/1_create_root_user.rb", <<-CODE
migration 1, :create_root_user do
  up do
    User.auto_migrate!
    Locale.auto_migrate!
    Group.auto_migrate!
    Ixtlan::Models::GroupUser.auto_migrate!
    Ixtlan::Models::GroupLocaleUser.auto_migrate!

    u = User.new(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :language => 'en', :id => 1)
    #u.current_user = u
    u.created_at = DateTime.now
    u.updated_at = u.created_at
    u.created_by_id = 1
    u.updated_by_id = 1
    u.reset_password
    u.save!
    g = Group.create(:name => 'root', :current_user => u)
    u.groups << g
    u.save
    File.open("root", 'w') { |f| f.puts "\#{u.password}" }
  end

  down do
  end
end
CODE

file "db/migrate/2_create_configuration.rb", <<-CODE
migration 2, :create_configuration do
  up do
    Configuration.auto_migrate!
    Configuration.create(:session_idle_timeout => 10, :keep_audit_logs => 3, :current_user => User.first)
  end

  down do
  end
end
CODE

file "db/migrate/3_create_locale.rb", <<-CODE
migration 3, :create_locale do
  up do
    Locale.auto_migrate!
    # get/create default locale
    Locale.default
    # get/create "every" locale
    Locale.every
  end

  down do
  end
end
CODE

file "db/migrate/4_create_text.rb", <<-CODE
migration 4, :create_text do
  up do
    I18nText.auto_migrate!
  end

  down do
  end
end
CODE

#some small html pages
file "app/views/sessions/login.html.erb", <<-CODE
<p style="color: darkgreen"><%= @notice %></p>
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

file 'app/views/errors/error.html.erb', <<-CODE
<h1><%= @notice %></h1>
CODE

file 'app/views/errors/stale.html.erb', <<-CODE
<h1>stale resource</h1>
<p>please reload resource and change it again</p>
CODE

# setup permissions controller
file 'app/guards/permissions_guard.rb', <<-CODE
Ixtlan::Guard.initialize(:permissions, {:index => []})
CODE

file "app/controllers/permissions_controller.rb", <<-CODE
class PermissionsController < ApplicationController
  # TODO the authenticate should NOT be there, i.e. it leaks too much info
  skip_before_filter :authenticate, :guard
  include Ixtlan::Controllers::PermissionsController
end
CODE
route "map.resources :permissions"

# setup word_bundles controller
file "app/controllers/word_bundles_controller.rb", <<-CODE
class PermissionsController < ApplicationController
  # no guard since everyone needs to load the bundles
  skip_before_filter :guard
  include Ixtlan::Controllers::WordBundlesController
end
CODE
route "map.resources :word_bundles"

# user model/controller
generate 'ixtlan_datamapper_rspec_scaffold', '--skip-migration', 'User', 'login:string', 'name:string', 'email:string', 'language:string'
file 'app/models/user.rb', <<-CODE
class User < Ixtlan::Models::User; end
CODE
gsub_file 'spec/models/user_spec.rb', /.*:name => "sc'?r&?ipt".*/, ''
gsub_file 'spec/models/user_spec.rb', /value for login/, 'valueForLogin'
gsub_file 'spec/models/user_spec.rb', /value for email/, 'value@for.email'
gsub_file 'spec/models/user_spec.rb', /value for language/, 'vl'

# group model/controller
generate 'ixtlan_datamapper_rspec_scaffold', '--skip-migration', 'Group', 'name:string'
file 'app/models/group.rb', <<-CODE
class Group < Ixtlan::Models::Group; end
CODE

# i18n stuff: i18n model, phrases controller
file 'app/models/i18n_text.rb', <<-CODE
class I18nText < Ixtlan::Models::Text; end
CODE
file "app/controllers/phrases_controller.rb", <<-CODE
PhrasesController < ApplicationController
include Ixtlan::Controllers::TextsController
end
CODE
route "map.resources :phrases"

# locale model/controller
generate 'ixtlan_datamapper_rspec_scaffold', '--skip-migration', '--skip-modified-by', 'Locale', 'code:string'
file 'app/models/locale.rb', <<-CODE
class Locale < Ixtlan::Models::Locale; end
CODE
gsub_file 'spec/models/locale_spec.rb', /value for code/, 'vc'
file 'spec/support/locale.rb', <<-CODE
module Ixtlan
  module Models
    class Locale
      property :id, Integer
    end
  end
end
CODE

# configuration guard/model/controller
file 'app/models/configuration.rb', <<-CODE
class Configuration < Ixtlan::Models::Configuration
  def self.instance
    Ixtlan::Models::Configuration.instance
  end
end
CODE
file 'app/guards/configurations_guard.rb', <<-CODE
Ixtlan::Guard.initialize(:configurations, 
                 { :show => [], 
                   :edit => [], 
                   :update => [] })
CODE
file 'app/controllers/configurations_controller.rb', <<-CODE
class ConfigurationsController < ApplicationController
  include Ixtlan::Controllers::ConfigurationsController
end
CODE
route "map.resource :configuration"

# authentication model/controller
file 'app/models/authentication.rb', <<-CODE
class Authentication < Ixtlan::Models::Authentication; end
CODE
file 'app/controllers/authentications_controller.rb', <<-CODE
class AuthenticationsController < ApplicationController
  skip_before_filter :guard
  include Ixtlan::Controllers::AuthenticationsController
end
CODE
route "map.resource :authentication"

# modify application controller as needed
gsub_file 'app/controllers/application_controller.rb', /^\s*helper.*/, <<-CODE
  filter_parameter_logging :password, :login
  before_filter :check_session_expiry

  # override default to use nice User (without namespace)
  #def login_from_params
  #  User.authenticate(params[:login], params[:password])
  #end

  #def login_from_session
  #  User.get(session[:user_id])
  #end

  # override default to use value from configuration
  #def session_timeout
  #  Configuration.instance.session_idle_timeout
  #end

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

# configuration before starting rails
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

# setup a pom.xml to enable the use of the rails-maven-plugin
file 'pom.xml', <<-CODE
<project xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                      http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.example</groupId>
  <artifactId>#{File.basename(root)}</artifactId>
  <packaging>war</packaging>
  <version>1.0-SNAPSHOT</version>
  <name>rails datamapper demo</name>
  <url>http://github.com/mkristian/rails-templates/blob/master/datamapper.rb</url>
  <pluginRepositories>
    <pluginRepository>
      <id>saumya</id>
      <name>Saumyas Plugins</name>
      <url>http://mojo.saumya.de</url>
    </pluginRepository>
  </pluginRepositories>
  <build>
    <!-- allow the gwt plugin to work with this pom -->
    <outputDirectory>war/WEB-INF/classes</outputDirectory> 
    <plugins>
      <plugin>
        <groupId>de.saumya.mojo</groupId>
        <artifactId>rails-maven-plugin</artifactId>
	<version>0.3.1</version>
      </plugin>
      <plugin>
        <groupId>de.saumya.mojo</groupId>
        <artifactId>jruby-maven-plugin</artifactId>
	<version>${jruby.plugins.version}</version>
      </plugin>
      <plugin>
        <groupId>de.saumya.mojo</groupId>
        <artifactId>gem-maven-plugin</artifactId>
	<version>${jruby.plugins.version}</version>
      </plugin>
    </plugins>
  </build>
  <properties>
    <jruby.plugins.version>0.5.0</jruby.plugins.version>
    <jruby.fork>false</jruby.fork>
  </properties>
</project>
CODE

logger.info 
logger.info 
logger.info "info mavenized rails application"
logger.info "\thttp://github.org/mkristian/rails-maven-plugin"
logger.info 
logger.info "if you want to run jruby please run after uninstalling"
logger.info "the native extension of do_sqlite3"
logger.info "\truby -S gem uninstall do_sqlite3"
logger.info "\tjruby -S rake gems:install"
logger.info "rake gems:unpack does NOT work with jruby due to a bug in rail <=2.3.5"
logger.info "you can try"
logger.info "\tmvn rails:rails-freeze-gems"
logger.info "which patches rails after freezing it"
logger.info 
logger.info 


# setup the database
rake 'db:migrate:down VERSION=0'
rake 'db:sessions:create'
rake 'db:migrate'
rake 'db:autoupgrade RAILS_ENV=development'
logger.info
logger.info "you find the root password in the file 'root'"
logger.info

# GWT GUI installation
logger.info "if you have maven installed you can preview the GWT interface"
if yes?("install GWT interface ?")
  run("mvn archetype:generate -DarchetypeArtifactId=gui -DarchetypeGroupId=de.saumya.gwt.translation -DarchetypeVersion=0.2.1 -DartifactId=#{File.basename(root)} -DgroupId=com.example -Dversion=1.0-SNAPSHOT -B")
  logger.info
  logger.info "first start rails in one console"
  logger.info "\tscript/server"
  logger.info
  logger.info "and then start the GWT gui in another console"
  logger.info "\tmvn gwt:run"
end

logger.info
logger.info
logger.info "for dm-core version 0.10.2 there are a lot of deprecated warnings but everything works as expected"
logger.info
logger.info
