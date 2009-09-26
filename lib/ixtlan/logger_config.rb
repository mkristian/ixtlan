# use logging logger for slf4r logger facade
require 'slf4r/logging_logger'

require 'ixtlan/rolling_file'

module Ixtlan
  class LoggerConfig
    def self.rolling_appender(name)
      appender = Ixtlan::RollingFile.new(name, 
                                         :filename_base => log_filebase(name), 
                                         :keep => 2, 
                                         :date_pattern => '%Y-%m')
      appender.layout = Logging::Layouts::Pattern.new(:pattern => "%d [%-l] (%c) %m\n")
      appender
    end

    def self.logger(appender, category, level = :warn)
      logger = Logging::Logger.new(category)
      logger.add_appenders(appender)
      logger.level = log_level(level)
      logger
    end

    def self.log_filebase(name)
      Rails.root.join('log', name).to_s
    end

    def self.log_level(level = :warn)
      ENV['RAILS_ENV'] == 'production' ? level : :debug 
    end

    Logging.init :debug, :info, :warn, :error unless Logging.const_defined? 'MAX_LEVEL_LENGTH'

    # setup root logger
    appender = Logging::Appenders::File.new('default', 
                                            :filename => log_filebase(RAILS_ENV) + ".log")
    appender.layout = Logging::Layouts::Pattern.new(:pattern => "%-20c\t- %m\n")
    
    logger = logger(appender, :root)
    logger.info "initialized logger ..."
    
    # datamapper + dataobject logger
    appender = rolling_appender('sql')
  
    DataMapper.logger = logger(appender, DataMapper)
    
    
    #TODO not working!!!!
    DataObjects.logger = logger(appender, DataObjects)
    #TODO not working!!!!
    
    #DataObjects::Logger.new("#{RAILS_ROOT}/log/sqls.log", ENV['RAILS_ENV'] == 'production' ? :debug : :debug, " - ", true)
    
    # configure audit logger
    Ixtlan::AuditConfig.configure(Ixtlan::Configuration.instance.keep_audit_logs, 
                                  log_filebase('audit'), 
                                  [
                                   Ixtlan::User, 
                                   Ixtlan::Audit, 
                                   Ixtlan::SessionTimeout, 
                                   Ixtlan::UnrestfulAuthentication
                                  ] )
    
    # keep the guard messages in a separate file as well
    logger(rolling_appender('guard'), Ixtlan::Guard, :info)
  end

end
