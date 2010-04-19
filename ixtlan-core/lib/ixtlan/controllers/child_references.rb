module Ixtlan
  module Controllers
    module ChildReferences

      def setup_children(new_children, old_children, model)
        new_children = if new_children.nil?
                         []
                       else
                         new_children[new_children.keys[0]]
                       end
        new_children = [new_children] unless new_children.is_a? Array
        new_ids = new_children.collect { |v| v[:id].to_i }
        old_children.select do |g|
          !(new_ids.member? g.id)
        end.each do |g|
          old_children.delete(g)
        end
        old_ids = old_children.collect { |g| g.id }
        new_ids.each do |gid|
          old_children << model.get!(gid)  unless old_ids.member? gid
        end
      end
    end
  end
end
