require 'dummy_ldap_resource'

require 'slf4r/logging_logger'

Logging.init :debug, :info, :warn, :error

#appender = Logging::Appender.stdout
appender = Logging::Appenders::File.new('default', :filename => "#{RAILS_ROOT}/log/#{RAILS_ENV}.log")
appender.layout = Logging::Layouts::Pattern.new(:pattern => "[%d] %-l (%c) %m\n")
logger = Logging::Logger.new(:root)
logger.add_appenders(appender)
logger.level = :debug
logger.info "initialized logger ..."

guard_logger = Slf4r::LoggerFacade.new(Guard::Guard)
Guard::Guard.load(guard_logger)
guard_logger.info("guard loaded ...")

require 'audit'

AuditConfig.reconfigure(Configuration.instance.keep_audit_logs, 
                        Rails.root.join('log').to_s + '/audit.log')

DataMapper::Logger.new("#{RAILS_ROOT}/log/sql.log", ENV['RAILS_ENV'] == 'production' ? :warn : :debug)
DataObjects::Logger.new("#{RAILS_ROOT}/log/sql.log", ENV['RAILS_ENV'] == 'production' ? :warn : :debug)
