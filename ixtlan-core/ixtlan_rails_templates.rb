# inspired by http://www.rowtheboat.com/archives/32
###################################################

# --------------
# HELPER METHODS
# --------------

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

def add_gem(name, version, options = {})
  gem name, options
  java = name =~ /^do_/ ? "<classifier>java</classifier>\n" : ""
  gsub_file 'pom.xml', /<\/dependencies>/, "<dependency>\n<groupId>rubygems</groupId>\n<artifactId>#{name}</artifactId>\n<version>#{version}</version>\n<type>gem</type>\n#{java}</dependency>\n</dependencies>"
end

def migration(model)
  @__index ||= 0
  @__index = @__index + 1
  file "db/migrate/#{@__index}_create_#{model}.rb", <<-CODE
require 'config/initializers/00_models.rb'
require 'ixtlan/rails/migrations'
migration #{@__index}, :create_#{model} do
  up do
    Ixtlan::Rails::Migrations.create_#{model}
  end

  down do
  end
end
CODE
end

# --------
# VERSIONS
# --------

JRUBY_PLUGINS_VERSION='0.12.0'
DM_VERSION='0.10.2'

# -----------
# MAVEN SETUP
# -----------

# setup a pom.xml
inside("..") do
  run("mvn archetype:generate -DarchetypeArtifactId=rails-maven-archetype -DarchetypeGroupId=de.saumya.mojo -DarchetypeVersion=#{JRUBY_PLUGINS_VERSION} -DartifactId=#{File.basename(root)} -DgroupId=com.example -Dversion=0.1.0-SNAPSHOT -B")
end
File.delete('lib/tasks/jdbc.rake')
File.delete('config/initializers/jdbc.rb')
gsub_file 'pom.xml', /<version>1.5.0<\/version>/, "<version>1.4.1<\/version>"

# ---------------------
# GWT AND ECLIPSE SETUP
# ---------------------

#if ENV['GWT'] == 'true' || (!ENV['GWT'] && yes?("install GWT interface ?"))
  gwt_prefix = "gwt_"
  File.rename("src/main/webapp/WEB-INF/web.xml", 
              "src/main/webapp/WEB-INF/web.xml.rails")
  inside("..") do
    run("mvn archetype:generate -DarchetypeArtifactId=gui -DarchetypeGroupId=de.saumya.gwt.translation -DarchetypeVersion=0.3.1 -DartifactId=#{File.basename(root)} -DgroupId=com.example -Dversion=0.1.0-SNAPSHOT -B")
  end
  File.rename("src/main/webapp/WEB-INF/web.xml.rails", 
              "src/main/webapp/WEB-INF/web.xml")

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
#else
#  gwt_prefix = nil
#end

# --------------------
# ADD GEM DEPENDENCIES
# --------------------

# ixtlan gems
add_gem 'rack', '1.0.1'
add_gem 'rack-datamapper', '0.2.5'
add_gem 'ixtlan', '0.2.5'

# assume sqlite3 to be database
add_gem 'do_sqlite3', '0.10.1.1'

# serialization, validations and timestamps in your models
add_gem 'dm-validations', DM_VERSION
add_gem 'dm-timestamps', DM_VERSION
add_gem 'dm-migrations', DM_VERSION
add_gem 'dm-aggregates', DM_VERSION
add_gem 'dm-core', DM_VERSION

# get all datamapper related gems
# gem 'addressable', :lib => 'addressable/uri'

# assume you prefer rspec over unit tests
add_gem 'rspec', '[1.3.0,1.4.0]', :lib => false
add_gem 'rspec-rails', '1.3.0', :lib => false

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

# ----------
# MIDDLEWARE
# ----------

environment ''
middleware 'DataMapper::RestfulTransactions'
middleware 'DataMapper::IdentityMaps'
middleware 'Rack::Deflater'
environment "#config.middleware.use 'Ixtlan::CmsScript'"
environment '# add middleware'

# ------------
# INITIALIZERS
# ------------

# define locale model names
initializer '00_models.rb', <<-CODE
module Ixtlan
  module Models
    AUTHENTICATION = "Authentication"
    USER = "User"
    GROUP = "Group"
    LOCALE = "Locale"
    DOMAIN = "Domain"
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
end
require 'ixtlan/monkey_patches'
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

# init a session store
initializer '04_datamapper_store.rb', <<-CODE
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

# define the date/time pattern for the xml
initializer '05_time_formats.rb', <<-CODE
Time::DATE_FORMATS[:xml] = lambda { |time| time.utc.strftime("%Y-%m-%d %H:%M:%S") }
CODE

# ----------
# MIGRATIONS
# ----------

migration("user")
migration("configuration")
migration("locale")
migration("domain")
migration("text")

# -----
# VIEWS
# -----

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


# ----------------------
# MODELS AND CONTROLLERs
# ----------------------

# setup permissions controller
ixtlan_controller("permissions", {:actions => [:index]})

# user model/controller
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', 'User', 'login:string', 'name:string', 'email:string', 'language:string'
gsub_file 'spec/models/user_spec.rb', /.*:name => "sc'?r&?ipt".*/, ''
gsub_file 'spec/models/user_spec.rb', /value for login/, 'valueForLogin'
gsub_file 'spec/models/user_spec.rb', /value for email/, 'value@for.email'
gsub_file 'spec/models/user_spec.rb', /value for language/, 'vl'
ixtlan_model 'user'
ixtlan_controller 'users'

# group model/controller
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', 'Group', 'name:string'
ixtlan_model 'group'
ixtlan_controller 'groups'

# domain model/controller
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', 'Domain', 'name:string'
ixtlan_model 'domain'
ixtlan_controller 'domains'

# i18n stuff: i18n model, phrases, word_bundles controller
ixtlan_model 'i18n_text'
ixtlan_controller "phrases"
ixtlan_controller "word_bundles"

# locale model/controller
generate "ixtlan_datamapper_rspec_scaffold", '--skip-migration', '--skip-modified-by', 'Locale', 'code:string'
gsub_file 'spec/models/locale_spec.rb', /value for code/, 'vc'
ixtlan_model "locale"
ixtlan_controller "locales"

# configuration guard/model/controller
file 'app/models/configuration.rb', <<-CODE
class Configuration < Ixtlan::Models::Configuration
  def self.instance
    get!(1)
  end
end
CODE
ixtlan_controller 'configurations', {:actions => [:show, :edit, :update]}, 'configuration'

# authentication model/controller
ixtlan_model "authentication"
ixtlan_controller "authentications", {:actions => [:create]}, 'authentication'

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

  def render_error_page_with_session(status)
    render :template => "errors/error_with_session", :status => status
  end

  def render_error_page(status)
    render :template => "errors/error", :status => status
  end

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
  rescue_from ActionController::InvalidAuthenticityToken, :with => :stale_resource

  # have nice stacktraces in development mode
  unless consider_all_requests_local
    rescue_from ActionView::MissingTemplate, :with => :internal_server_error
    rescue_from ActionView::TemplateError, :with => :internal_server_error
  end

  protect_from_forgery # See ActionController::RequestForgeryProtection for details
CODE

# --------------------------------
# GLOBAL CONFIG VIA PREINITIALIZER
# --------------------------------

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

# ------
# EPILOG
# ------

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

# -----------------------
# SETUP AND SEED DATABASE
# -----------------------

rake 'db:migrate:down VERSION=0'
rake 'db:sessions:create'
run('rake db:migrate --trace')
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
