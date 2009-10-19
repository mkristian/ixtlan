require 'rails_generator/generators/components/model/model_generator'
require 'active_record'
require 'datamapper4rails/overlay'

class IxtlanDatamapperRspecModelGenerator < DatamapperRspecModelGenerator

  def manifest
    overlay_dirs.add_generator("ixtlan_datamapper_model")
    super
  end
  
  def add_options!(opt)
    opt.separator ''
    opt.separator 'Options:'
    opt.on("--skip-timestamps",
           "Don't add timestamps for this model") { |v| options[:skip_timestamps] = v }
    opt.on("--skip-modified-by",
           "Don't add modified_by references for this model") { |v| options[:skip_modified_by] = v }
  end
end
