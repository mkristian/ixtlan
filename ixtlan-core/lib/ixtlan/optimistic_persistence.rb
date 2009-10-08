module Ixtlan
  module OptimisticPersistence    
    def stale?
      if(prop = properties[:updated_at] and dirty?)
        updated_at = original_attributes[prop]|| properties[:updated_at].get!(self)
        c = model.key_conditions(repository, key)
        qu = {}#:updated_at => updated_at}
        c.each {|p,v| qu[p.name] = v}
        
        s = self.model.first(qu)
        # HACK for jruby with sqlite3 (and maybe others)
        # ignore timezone by cutting it off
        s.nil? ? true : s.updated_at.to_s.sub(/\+.*/,'') != updated_at.to_s.sub(/\+.*/,'')

        #self.model.first(qu).nil?
      else
        false
      end
    end
  end
end
