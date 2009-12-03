require 'ixtlan/audit'

Logging.init :debug, :info, :warn, :error unless Logging.const_defined? 'MAX_LEVEL_LENGTH'

module Ixtlan
  class AuditConfig

    @logger = Slf4r::LoggerFacade.new(self)

    def self.configure(keep, file, categories)
      @@categories = categories
      reconfigure(keep, file)
    end

    def self.reconfigure(keep, file)
      audit_appender = 
        RollingFile.new('audit', 
                        { :filename_base => file, 
                          :keep => keep, 
                          :safe => true, 
                          :layout => Logging::Layouts::Pattern.new(:pattern => "%d %m\n") }
                        )

      @@categories.each do |category|
        logger = Logging::Logger[category]
        logger.remove_appenders('audit')
        logger.add_appenders(audit_appender)
        @logger.debug("setup logger for #{category}")
      end
      @logger.info("initialized audit log . . .")
    end

  end

end
