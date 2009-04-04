require 'dummy_ldap_resource'

require 'slf4r/logging_logger'

Logging.init :debug, :info, :warn, :error

appender = Logging::Appender.stdout
appender.layout = Logging::Layouts::Pattern.new(:pattern => "[%d] %-l (%c) %m\n")
logger = Logging::Logger.new(:root)
logger.add_appenders(appender)
logger.level = :debug
logger.info "initialized logger . . ."

guard_logger = Slf4r::LoggerFacade.new(Guard::Guard)
Guard::Guard.load(guard_logger)
guard_logger.info("guard loaded . . .")


module DatamapperStore
  class Session
 
    def data=(data)
      d = data.dup
      @user = d.delete(:user)
      attribute_set(:data, ::Base64.encode64(Marshal.dump(d)))
    end

    def data
      Marshal.load(::Base64.decode64(attribute_get(:data))).merge({:user => @user})
    end
  end
end

