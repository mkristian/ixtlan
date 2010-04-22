require 'ixtlan/optimistic_persistence_module'
require 'dm-core'
module DataMapper

  class StaleResourceError < StandardError; end

end

module Ixtlan
  module OptimisticPersistence

    def self.included(base)
      base.send(:include, ::Ixtlan::OptimisticPersistenceModule)
      base.before :valid? do
        raise ::DataMapper::StaleResourceError.new(model.name + "(#{key}) was stale") if stale?
      end
    end
    ::DataMapper::Model.append_inclusions self
  end
end
