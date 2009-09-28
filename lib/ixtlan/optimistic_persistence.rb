module Ixtlan
  module OptimisticPersistence    
    def stale?
      if(prop = properties[:updated_at])
        q = query
        q.update(:updated_at => original_attributes[prop]|| properties[:updated_at].get!(self))
        dirty? and self.model.first(q) == nil
      else
        false
      end
    end
  end
end
