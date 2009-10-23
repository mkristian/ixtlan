module Ixtlan
  class CmsScript
    def initialize(app, rootpath = "/ixtlan")
      @app = app
      @rootpath = rootpath
    end
    
    def call(env)
      dup._call(env)
    end
    
    def _call(env)
      if(env['REQUEST_PATH'] =~ /^#{@rootpath}\//)
        
        file = Rails.public_path.to_s + env['REQUEST_PATH'].sub(/^#{@rootpath}/, '')
        @file = File.open(file)
        [@status, {}, self]
      else
        @app.call(env)
      end
    end
    
    def each(&block)
      @file.each do |line|
        block.call(line.sub(/<\/head>/, "<link type='text/css' rel='stylesheet' href='#{@rootpath}/embed.css'></link><script type='text/javascript' language='javascript' src='#{@rootpath}/embed.nocache.js'></script></head>"))
      end
    end
  end
end
