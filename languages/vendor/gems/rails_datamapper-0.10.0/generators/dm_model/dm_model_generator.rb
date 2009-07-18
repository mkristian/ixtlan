require 'rails_generator/generators/components/model/model_generator'
require 'active_record'

require File.dirname(__FILE__) + '/../overlay'

class DmModelGenerator < ModelGenerator

  def manifest
    overlay_dirs << source_root
    super
  end

end
