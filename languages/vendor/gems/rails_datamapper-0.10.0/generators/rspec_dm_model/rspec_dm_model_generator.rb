require 'rails_generator/generators/components/model/model_generator'
require 'active_record'
require File.dirname(__FILE__) + '/../overlay'

class RspecDmModelGenerator < RspecModelGenerator

  def manifest
    overlay_dirs << source_root
    overlay_dirs << File.join(self.class.lookup("dm_model").path, 'templates')
    super
  end

end
