module Ixtlan
  module OptimisticPersistenceModule
    def stale?
      if(!new? && prop = properties[:updated_at] && dirty?)
        updated_at = original_attributes[prop] || properties[:updated_at].get!(self)
        qu = {}
        c = model.key_conditions(repository, key)
        c.each {|p,v| qu[p.name] = v}
        
        s = self.model.first(qu)
        if s.nil? 
          false 
        else
          # use to_s to get it to work in both MRI and JRuby
          s.updated_at.to_s != updated_at.to_s
        end
      else
        false
      end
    end
  end
end
