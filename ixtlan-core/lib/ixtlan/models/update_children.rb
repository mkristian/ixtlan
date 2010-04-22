module Ixtlan
  module Models
    module UpdateChildren

      def update_children(new_children, old_children_sym)
        # get the dm-core/collection and its model
        old_children = send(old_children_sym)
        model = old_children.model

        # make sure we have an array
        new_children = if new_children.nil?
                         []
                       else
                         new_children[new_children.keys[0]]
                       end
        new_children = [new_children] unless new_children.is_a? Array

        # ids of new children
        new_ids = new_children.collect { |v| v[:id].to_i }

        # delete obsolete old children
        old_children.select do |g|
          !(new_ids.member? g.id)
        end.each do |g|
          old_children.delete(g)
        end

        # add missing new children
        old_ids = old_children.collect { |g| g.id }
        new_ids.each do |gid|
          old_children << model.get!(gid)  unless old_ids.member? gid
        end
      end

      def update_restricted_children(new_children, old_children_sym, allowed_children)
        # get the dm-core/collection and its model
        old_children = send(old_children_sym)
        model = old_children.model

        # make sure we have an array
        new_children = if new_children.nil?
                         []
                       else
                         new_children[new_children.keys[0]]
                       end
        new_children = [new_children] unless new_children.is_a? Array

        # ids of new children
        new_ids = new_children.collect { |v| v[:id].to_i }
        # ids of allowed children
        allowed_ids = allowed_children.collect { |g| g.id }

        # delete obsolete and allowed old children
        set = old_children.select do |g|
          !(new_ids.member? g.id) && (allowed_ids.member? g.id)
        end
        set.each do |g|
          old_children.delete(g)
        end

        # add missing and allowed new children
        old_ids = old_children.collect { |g| g.id }
        new_ids.each do |gid|
          old_children << model.get!(gid) if !old_ids.member?(gid) && allowed_ids.member?(gid)
        end
      end
    end
  end
end
