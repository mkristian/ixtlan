class AuditConfig

  def self.reconfigure(keep, file)
    audit_appender = 
      Logging::Appenders::RollingFile.new('audit', 
                                          { :filename => file, 
                                            :age => 'weekly', 
                                            :keep => keep, 
                                            :safe => true, 
                                            :layout => Logging::Layouts::Pattern.new(:pattern => "%d %m\n") }
                                          )
    logger = Logging::Logger[Audit::AuditFilter]
    logger.remove_appenders('audit')
    logger.add_appenders(audit_appender)

    # cleanup obsolete audit files
    log_dir = Dir.new(file.sub(/\/[^\/]*$/, ''))
    prefix = file.sub(/^.*\//,'').sub(/[.].*$/, '')
    postfix = file.sub(/^.*[.]/, '')
    log_dir.entries.each do |entry|
      if entry =~ /^#{prefix}[.][0-9]*[.]#{postfix}$/
        number = entry.sub(/^#{prefix}./, "").sub(/.#{postfix}$/, "")
        if number.to_i > keep
          File.delete("#{log_dir.path}/#{entry}")        
        end
      end
    end
  end
end
