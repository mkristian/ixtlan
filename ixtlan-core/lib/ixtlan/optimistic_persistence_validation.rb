require 'ixtlan/optimistic_persistence_module'

module DataMapper

  class StaleResource < StandardError; end
   
  module OptimisticPersistenceValidation
    
    def self.included(base)
      base.send(:include, ::Ixtlan::OptimisticPersistenceModule)
      base.validates_with_block :stale do
        if(stale?)
          [false, "stale resource, please reload the resource"]
        else
          true
        end
      end
    end
    Model.append_inclusions self
  end
end
