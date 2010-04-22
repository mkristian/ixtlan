# inspired by http://www.rowtheboat.com/archives/32
###################################################

# helper to add middleware
def middleware(name)
  log 'middleware', name
  environment "config.middleware.use '#{name}'"
end
def ixtlan_model(name)
  file "app/models/#{name}.rb", <<-CODE
class #{name.camelcase} < Ixtlan::Models::#{name.camelcase}; end
CODE
end
def ixtlan_controller(name, guards = {:actions => [:index,:show,:new,:create,:edit,:update,:destroy], :default => nil}, singleton = nil)
  if guards
    file "app/guards/#{name}_guard.rb", <<-CODE
Ixtlan::Guard.initialize(:#{name}, {
#{guards[:actions].collect {|g| ":#{g} => [#{guards[:default]}]" }.join(",\n") }
})
CODE
  end
  file "app/controllers/#{name}_controller.rb", <<-CODE
class #{name.camelize}Controller < ApplicationController
  include Ixtlan::Controllers::#{name.camelize}Controller
end
CODE
  if singleton
    route "map.resource :#{singleton}"
  else
    route "map.resources :#{name}"
  end
end

# ixtlan gems
gem 'ixtlan'#, :version => '0.2.2'

# this pulls in rack_datamapper
#gem 'datamapper4rails', :version => '0.4.0'

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
# auto require to load needed libraries . . .
require 'datamapper4rails'
require 'slf4r'
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

    u = User.new(:login => 'root', :email => 'root@exmple.com', :name => 'Superuser', :id => 1)
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

    a = User.create(:login => 'admin', :email => 'admin@exmple.com', :name => 'Administrator', :id => 2, :current_user => u)
    a.reset_password
    a.save!
    users = Group.create(:name => 'users', :current_user => u)
    a.groups << users
    locales = Group.create(:name => 'locales', :current_user => u)
    a.groups << locales
    a.save

    File.open("root", 'w') { |f| f.puts "root\n\#{u.password}\nadmin\n\#{a.password}\n" }
  end

  down do
  end
end
CODE

file "db/migrate/2_create_configuration.rb", <<-CODE
migration 2, :create_configuration do
  up do
    Configuration.auto_migrate!
    Ixtlan::Models::ConfigurationLocale.auto_migrate!
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

    # root user has access to ALL locales
    Ixtlan::Models::GroupLocaleUser.create(:group => Group.first, :user => User.first, :locale => Locale.every)
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
class WordBundlesController < ApplicationController
  # no guard since everyone needs to load the bundles
  skip_before_filter :guard
  include Ixtlan::Controllers::WordBundlesController
end
CODE
route "map.resources :word_bundles"

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
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-resources-plugin</artifactId>
        <version>2.4.1</version>
        <configuration>
          <encoding>UTF-8</encoding>
        </configuration>
      </plugin>
      <plugin>
        <groupId>de.saumya.mojo</groupId>
        <artifactId>rails-maven-plugin</artifactId>
 <version>${jruby.plugins.version}</version>
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
    <jruby.plugins.version>0.8.0</jruby.plugins.version>
    <jruby.fork>false</jruby.fork>
  </properties>
</project>
CODE

if ENV['GWT'] == 'true' || (!ENV['GWT'] && yes?("install GWT interface ?"))
  gwt_prefix = "gwt_"
  run("mvn archetype:generate -DarchetypeArtifactId=gui -DarchetypeGroupId=de.saumya.gwt.translation -DarchetypeVersion=0.3.1 -DartifactId=#{File.basename(root)} -DgroupId=com.example -Dversion=0.1.0-SNAPSHOT -B")

  file '.classpath', <<-CODE
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
 <classpathentry kind="src" output="war/WEB-INF/classes" path="src/main/java"/>
 <classpathentry excluding="**" kind="src" output="war/WEB-INF/classes" path="src/main/resources"/>
 <classpathentry kind="src" output="target/test-classes" path="src/test/java"/>
 <classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/J2SE-1.5"/>
 <classpathentry kind="con" path="org.maven.ide.eclipse.MAVEN2_CLASSPATH_CONTAINER"/>
 <classpathentry kind="output" path="war/WEB-INF/classes"/>
</classpath>
CODE
  file '.project', <<-CODE
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
 <name>ixtlan-test</name>
 <comment></comment>
 <projects>
 </projects>
 <buildSpec>
  <buildCommand>
   <name>org.eclipse.jdt.core.javabuilder</name>
   <arguments>
   </arguments>
  </buildCommand>
  <buildCommand>
   <name>org.maven.ide.eclipse.maven2Builder</name>
   <arguments>
   </arguments>
  </buildCommand>
 </buildSpec>
 <natures>
  <nature>org.maven.ide.eclipse.maven2Nature</nature>
  <nature>org.eclipse.jdt.core.javanature</nature>
 </natures>
</projectDescription>
CODE
  file '.settings/org.maven.ide.eclipse.prefs', <<-CODE
activeProfiles=
eclipse.preferences.version=1
fullBuildGoals=process-test-resources
includeModules=false
resolveWorkspaceProjects=true
resourceFilterGoals=process-resources resources\:testResources
skipCompilerPlugin=true
version=1
CODE
  file '.settings/org.eclipse.jdt.core.prefs', <<-CODE
eclipse.preferences.version=1
org.eclipse.jdt.core.compiler.codegen.targetPlatform=1.6
org.eclipse.jdt.core.compiler.compliance=1.6
org.eclipse.jdt.core.compiler.problem.forbiddenReference=warning
org.eclipse.jdt.core.compiler.source=1.6
CODE
else
  gwt_prefix = nil
end

# user model/controller
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', 'User', 'login:string', 'name:string', 'email:string', 'language:string'
ixtlan_model 'user'
gsub_file 'spec/models/user_spec.rb', /.*:name => "sc'?r&?ipt".*/, ''
gsub_file 'spec/models/user_spec.rb', /value for login/, 'valueForLogin'
gsub_file 'spec/models/user_spec.rb', /value for email/, 'value@for.email'
gsub_file 'spec/models/user_spec.rb', /value for language/, 'vl'

# group model/controller
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', 'Group', 'name:string'
ixtlan_model 'group'

# i18n stuff: i18n model, phrases controller
ixtlan_model 'i18n_text'

# TODO rename TextsController to PhraseController i.e. make a new one
ixtlan_controller "phrases"

# locale model/controller
# TODO add to controller
#  skip_before_filter :guard, :only => :show
#  skip_before_filter :authenticate, :only => :show
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', '--skip-modified-by', 'Locale', 'code:string'
ixtlan_model "locale"
gsub_file 'spec/models/locale_spec.rb', /value for code/, 'vc'
#file 'spec/support/locale.rb', <<-CODE
#module Ixtlan
#  module Models
#    class Locale
#      property :id, Integer
#    end
#  end
#end
#CODE

# configuration guard/model/controller
file 'app/models/configuration.rb', <<-CODE
class Configuration < Ixtlan::Models::Configuration
  def self.instance
    Ixtlan::Models::Configuration.instance
  end
end
CODE
ixtlan_controller 'configurations', {:actions => [:show, :edit, :update]}, 'configuration'

# authentication model/controller
ixtlan_model "authentication"

file 'app/controllers/authentications_controller.rb', <<-CODE
class AuthenticationsController < ApplicationController
  skip_before_filter :guard
  skip_before_filter :authenticate, :only => :destroy
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

  #standard rails or datamapper/dataobjects
  rescue_from DataObjects::SQLError, :with => :internal_server_error
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

logger.info
logger.info
logger.info "info mavenized rails application"
logger.info "\thttp://github.org/mkristian/rails-maven-plugin"
logger.info
logger.info "if you want to run jruby please first uninstall"
logger.info "the native extension of do_sqlite3"
logger.info "\truby -S gem uninstall do_sqlite3"
logger.info "and the install it with java extension"
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
if gwt_prefix
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
