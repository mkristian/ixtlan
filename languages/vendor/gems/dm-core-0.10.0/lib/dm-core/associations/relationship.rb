module DataMapper
  module Associations
    # Base class for relationships. Each type of relationship
    # (1 to 1, 1 to n, n to m) implements a subclass of this class
    # with methods like get and set overridden.
    class Relationship
      include Extlib::Assertions

      OPTIONS = [ :child_repository_name, :parent_repository_name, :child_key, :parent_key, :min, :max, :inverse ].to_set.freeze

      # Relationship name
      #
      # Example: for :parent association in
      #
      # class VersionControl::Commit
      #   include DataMapper::Resource
      #
      #   belongs_to :parent
      # end
      #
      # name is :parent
      #
      # @api semipublic
      attr_reader :name

      # Options used to set up association
      # of this relationship
      #
      # Example: for :author association in
      #
      # class VersionControl::Commit
      #   include DataMapper::Resource
      #
      #   belongs_to :author, :model => 'Person'
      # end
      #
      # options is a hash with a single key, :model
      #
      # @api semipublic
      attr_reader :options

      # @ivar used to store collection of child options
      # in parent
      #
      # Example: for :commits association in
      #
      # class VersionControl::Branch
      #   include DataMapper::Resource
      #
      #   has n, :commits
      # end
      #
      # instance variable name for parent will be
      # @commits
      #
      # @api semipublic
      attr_reader :instance_variable_name

      # Repository from where child objects
      # are loaded
      #
      # @api semipublic
      attr_reader :child_repository_name

      # Repository from where parent objects
      # are loaded
      #
      # @api semipublic
      attr_reader :parent_repository_name

      # Minimum number of child objects for
      # relationship
      #
      # Example: for :cores association in
      #
      # class CPU::Multicore
      #   include DataMapper::Resource
      #
      #   has 2..n, :cores
      # end
      #
      # minimum is 2
      #
      # @api semipublic
      attr_reader :min

      # Maximum number of child objects for
      # relationship
      #
      # Example: for :fouls association in
      #
      # class Basketball::Player
      #   include DataMapper::Resource
      #
      #   has 0..5, :fouls
      # end
      #
      # maximum is 5
      #
      # @api semipublic
      attr_reader :max

      # Returns query options for relationship.
      #
      # For this base class, always returns query options
      # has been initialized with.
      # Overriden in subclasses.
      #
      # @api private
      attr_reader :query

      # Returns a hash of conditions that scopes query that fetches
      # target object
      #
      # @returns [Hash]  Hash of conditions that scopes query
      #
      # @api private
      def source_scope(source)
        { inverse => source }
      end

      # Creates and returns Query instance that fetches
      # target resource(s) (ex.: articles) for given target resource (ex.: author)
      #
      # @api semipublic
      def query_for(source, other_query = nil)
        repository_name = relative_target_repository_name_for(source)

        DataMapper.repository(repository_name).scope do
          query = target_model.query.dup
          query.update(self.query)
          query.update(source_scope(source))
          query.update(other_query) if other_query
          query.update(:fields => query.fields | target_key)
        end
      end

      # Returns model class used by child side of the relationship
      #
      # @returns [DataMapper::Resource] Class of association child
      # @api private
      def child_model
        @child_model ||= (@parent_model || Object).find_const(child_model_name)
      rescue NameError
        raise NameError, "Cannot find the child_model #{child_model_name} for #{parent_model_name} in #{name}"
      end

      # TODO: document
      # @api private
      def child_model_name
        @child_model ? child_model.name : @child_model_name
      end

      # Returns a set of keys that identify child model
      #
      # @return   [DataMapper::PropertySet]  a set of properties that identify child model
      # @api private
      def child_key
        @child_key ||=
          begin
            repository_name = child_repository_name || parent_repository_name
            properties      = child_model.properties(repository_name)

            child_key = parent_key.zip(child_properties || []).map do |parent_property, property_name|
              property_name ||= "#{property_prefix}_#{parent_property.name}".to_sym

              properties[property_name] || begin
                # create the property within the correct repository
                DataMapper.repository(repository_name) do
                  child_model.property(property_name, parent_property.primitive, child_key_options(parent_property))
                end
              end
            end

            properties.class.new(child_key).freeze
          end
      end

      # Returns model class used by parent side of the relationship
      #
      # @returns [DataMapper::Resource] Class of association parent
      # @api private
      def parent_model
        @parent_model ||= (@child_model || Object).find_const(parent_model_name)
      rescue NameError
        raise NameError, "Cannot find the parent_model #{parent_model_name} for #{child_model_name} in #{name}"
      end

      # TODO: document
      # @api private
      def parent_model_name
        @parent_model ? parent_model.name : @parent_model_name
      end

      # Returns a set of keys that identify parent model
      #
      # @return [DataMapper::PropertySet]
      #   a set of properties that identify parent model
      #
      # @api private
      def parent_key
        @parent_key ||=
          begin
            repository_name = parent_repository_name || child_repository_name
            properties      = parent_model.properties(repository_name)

            if parent_properties
              parent_key = properties.values_at(*parent_properties)
              properties.class.new(parent_key).freeze
            else
              properties.key
            end
          end
      end

      # Loads and returns "other end" of the association.
      # Must be implemented in subclasses.
      #
      # @api semipublic
      def get(resource, other_query = nil)
        raise NotImplementedError, "#{self.class}#get not implemented"
      end

      # Gets "other end" of the association directly
      # as @ivar on given resource. Subclasses usually
      # use implementation of this class.
      #
      # @api semipublic
      def get!(resource)
        resource.instance_variable_get(instance_variable_name)
      end

      # Sets value of the "other end" of association
      # on given resource. Must be implemented in subclasses.
      #
      # @api semipublic
      def set(resource, association)
        raise NotImplementedError, "#{self.class}#set not implemented"
      end

      # Sets "other end" of the association directly
      # as @ivar on given resource. Subclasses usually
      # use implementation of this class.
      #
      # @api semipublic
      def set!(resource, association)
        resource.instance_variable_set(instance_variable_name, association)
      end

      # Checks if "other end" of association is loaded on given
      # resource.
      #
      # @api semipublic
      def loaded?(resource)
        assert_kind_of 'resource', resource, source_model

        resource.instance_variable_defined?(instance_variable_name)
      end

      ##
      # Test the resource to see if it is a valid target resource
      #
      # @param [Object] resource
      #   the resource to be tested
      #
      # @return [TrueClass, FalseClass]
      #   true if the resource is valid
      #
      # @api semipulic
      def valid?(resource)
        unless resource.kind_of?(target_model)
          return false
        end

        unless target_key.get!(resource).all?
          return false
        end

        true
      end

      ##
      # Compares another Relationship for equality
      #
      # @param [Relationship] other
      #   the other Relationship to compare with
      #
      # @return [TrueClass, FalseClass]
      #   true if they are equal, false if not
      #
      # @api public
      def eql?(other)
        if equal?(other)
          return true
        end

        unless instance_of?(other.class)
          return false
        end

        cmp?(other, :eql?)
      end

      ##
      # Compares another Relationship for equivalency
      #
      # @param [Relationship] other
      #   the other Relationship to compare with
      #
      # @return [TrueClass, FalseClass]
      #   true if they are equal, false if not
      #
      # @api public
      def ==(other)
        if equal?(other)
          return true
        end

        if other.kind_of?(inverse_class)
          return false
        end

        unless other.respond_to?(:cmp_repository?, true)
          return false
        end

        unless other.respond_to?(:cmp_model?, true)
          return false
        end

        unless other.respond_to?(:cmp_key?, true)
          return false
        end

        unless other.respond_to?(:query)
          return false
        end

        cmp?(other, :==)
      end

      ##
      # Get the inverse relationship from the target model
      #
      # @api semipublic
      def inverse
        @inverse ||= target_model.relationships(relative_target_repository_name).values.detect do |relationship|
          relationship.kind_of?(inverse_class)   &&
          cmp_repository?(relationship, :child)  &&
          cmp_repository?(relationship, :parent) &&
          cmp_model?(relationship, :child)       &&
          cmp_model?(relationship, :parent)      &&
          cmp_key?(relationship, :child)         &&
          cmp_key?(relationship, :parent)

          # TODO: match only when the Query is empty, or is the same as the
          # default scope for the target model
        end || invert
      end

      # TODO: document
      # @api private
      def relative_target_repository_name
        target_repository_name || source_repository_name
      end

      # TODO: document
      # @api private
      def relative_target_repository_name_for(source)
        target_repository_name || if source.respond_to?(:repository)
          source.repository.name
        else
          source_repository_name
        end
      end

      private

      # TODO: document
      # @api private
      attr_reader :child_properties

      # TODO: document
      # @api private
      attr_reader :parent_properties

      # Initializes new Relationship: sets attributes of relationship
      # from options as well as conventions: for instance, @ivar name
      # for association is constructed by prefixing @ to association name.
      #
      # Once attributes are set, reader and writer are created for
      # the resource association belongs to
      #
      # @api semipublic
      def initialize(name, child_model, parent_model, options = {})
        initialize_model_ivar('child_model',  child_model)
        initialize_model_ivar('parent_model', parent_model)

        @name                   = name
        @instance_variable_name = "@#{@name}".freeze
        @options                = options.dup.freeze
        @child_repository_name  = @options[:child_repository_name]
        @parent_repository_name = @options[:parent_repository_name]
        @child_properties       = @options[:child_key].try_dup.freeze
        @parent_properties      = @options[:parent_key].try_dup.freeze
        @min                    = @options[:min]
        @max                    = @options[:max]
        @inverse                = @options[:inverse]

        # TODO: normalize the @query to become :conditions => AndOperation
        #  - Property/Relationship/Path should be left alone
        #  - Symbol/String keys should become a Property, scoped to the target_repository and target_model
        #  - Extract subject (target) from Operator
        #    - subject should be processed same as above
        #  - each subject should be transformed into AbstractComparison
        #    object with the subject, operator and value
        #  - transform into an AndOperation object, and return the
        #    query as :condition => and_object from self.query
        #  - this should provide the best performance

        @query = @options.except(*self.class::OPTIONS).freeze

        create_reader
        create_writer
      end

      # Set the correct ivar if the value is a String or Model object
      #
      # @param [String]
      #   the name of the ivar to set
      # @param [Model, #to_str] model
      #   the Model or String to set in the ivar
      #
      # @return [Model, #to_str]
      #   the ivar value
      #
      # @raise [ArgumentError]
      #   when model is not a Model and does not respond to #to_str
      #
      # @api private
      def initialize_model_ivar(name, model)
        if model.kind_of?(Model)
          instance_variable_set("@#{name}", model)
          initialize_model_ivar(name, model.name)
        elsif model.respond_to?(:to_str)
          instance_variable_set("@#{name}_name", model.to_str.dup.freeze)
        else
          raise ArgumentError, "#{name} is not a Model and does not respond to #to_str"
        end
      end

      ##
      # Creates reader method for association.
      #
      # Must be implemented by subclasses.
      #
      # @api semipublic
      def create_reader
        raise NotImplementedError, "#{self.class}#create_reader not implemented"
      end

      ##
      # Creates both writer method for association.
      #
      # Must be implemented by subclasses.
      #
      # @api semipublic
      def create_writer
        raise NotImplementedError, "#{self.class}#create_writer not implemented"
      end

      # TODO: document
      # @api private
      def child_key_options(parent_property)
        parent_property.options.only(:length, :size, :precision, :scale).update(:index => property_prefix)
      end

      ##
      # Prefix used to build name of default child key
      #
      # Must be implemented by subclasses.
      #
      # @return [Symbol]
      #   The name to prefix the default child key
      #
      # @api semipublic
      def property_prefix
        raise NotImplementedError, "#{self.class}#property_prefix not implemented"
      end

      # TODO: document
      # @api private
      def invert
        inverse_class.new(
          inverse_name,
          child_model,
          parent_model,
          options.only(*OPTIONS - [ :min, :max ]).update(
            :child_key  => child_key.map  { |property| property.name },
            :parent_key => parent_key.map { |property| property.name },
            :inverse    => self
          )
        )
      end

      # TODO: document
      # @api private
      def cmp?(other, operator)
        unless cmp_repository?(other, :child, operator)
          return false
        end

        unless cmp_repository?(other, :parent, operator)
          return false
        end

        unless cmp_model?(other, :child, operator)
          return false
        end

        unless cmp_model?(other, :parent, operator)
          return false
        end

        unless cmp_key?(other, :child, operator)
          return false
        end

        unless cmp_key?(other, :parent, operator)
          return false
        end

        unless query.send(operator, other.query)
          return false
        end

        true
      end

      # TODO: document
      # @api private
      def cmp_repository?(other, type, operator = :==)
        method = "#{type}_repository_name"

        # if either repository is nil, then the relationship is relative,
        # and the repositories are considered equivalent
        unless repository_name = send(method)
          return true
        end

        unless other_repository_name = other.send(method)
          return true
        end

        unless repository_name.send(operator, other_repository_name)
          return false
        end

        true
      end

      # TODO: document
      # @api private
      def cmp_model?(other, type, operator = :==)
        method = "#{type}_model"

        unless send(method).send(operator, other.send(method))
          return false
        end

        true
      end

      # TODO: document
      # @api private
      def cmp_key?(other, type, operator = :==)
        method = "#{type}_key"

        unless send(method).send(operator, other.send(method))
          return false
        end

        true
      end
    end # class Relationship
  end # module Associations
end # module DataMapper
