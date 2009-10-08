require 'logging'

module Ixtlan
  
  class RollingFile < ::Logging::Appenders::RollingFile
    
    def current_logfile
      "#{@filename_base}_#{Time.now.strftime(@date_pattern)}.#{@extension}"
    end

    def initialize( name, opts = {} )
      @date_pattern = opts.getopt(:date_pattern, '%Y-%m-%d')
      @extension = opts.getopt(:filename_extension, 'log')
      @filename_base = opts.getopt(:filename_base, 'log')
      opts.delete(:age)
      opts[:truncate] = false
      opts[:filename] = current_logfile
      super(name, opts)
      roll_files
    end

    def roll_required?( str = nil )
      not ::File.exist?(current_logfile)
    end

    def roll_files
      @fn = current_logfile
      files = Dir.glob("#{@filename_base}_*.#{@extension}").sort
      if (files.size > @keep)
        files[0..(files.size - 1 - @keep)].each do |file|
          ::File.delete file
        end
      end
    end 

    def write( event )
      str = event.instance_of?(::Logging::LogEvent) ?
      @layout.format(event) : event.to_s
      return if str.empty?

      check_logfile

      if roll_required?(str)
        return roll unless @lockfile

        @lockfile.lock {
          check_logfile
          roll if roll_required?
        }
      end
      super(str)
    end

  end
end
