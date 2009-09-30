require 'ixtlan/optimistic_persistence'
require 'dm-core'
module DataMapper

  class StaleResourceError < StandardError; end
   
  module OptimisticPersistence
    
    def self.included(base)
      base.send(:include, ::Ixtlan::OptimisticPersistence)
      base.before :update do
        raise StaleResourceError.new(model.name + "(#{key}) was stale") if stale?
      end
    end
    Model.append_inclusions self
  end
end
