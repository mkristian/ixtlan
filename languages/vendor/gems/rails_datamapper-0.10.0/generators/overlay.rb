require 'rails_generator/base'

Rails::Generator::NamedBase.class_eval do

  def overlay_dirs
    options[:overlay_dirs] ||= []
  end

  def add_options!(opt)
    super
    opt.on("--overlay-dir DIR",
           "overlay") do |v| 
      overlay_dirs << v
    end
  end
  
  def source_path(relative_source)  # Check whether we're referring to another generator's file.
    name, path = relative_source.split(':', 2)
  
    if dirs = options[:overlay_dirs] 
      file = path.nil? ? name : path
      dirs.reverse.each do |dir| 
        if (f = File.join(dir, file)) and File.exists?(f)
          return f
        end
      end
    end

    # If not, return the full path to our source file.
    if path.nil?
      File.join(source_root, name)
      
      # Otherwise, ask our referral for the file.
    else
      # FIXME: this is broken, though almost always true.  Others'
      # source_root are not necessarily the templates dir.
      File.join(self.class.lookup(name).path, 'templates', path)
    end
  end
end