module Ixtlan
  module Controllers
    module GroupsController
      
      def self.included(base)
        base.send(:include, Ixtlan::Controllers::SearchQuery)
        # do not want to expose groups on filesystem cache
        base.cache_headers :private
      end

      private

      GROUP = Object.full_const_get(::Ixtlan::Models::GROUP)
      
      public

      # GET /groups
      # GET /groups.xml
      def index
        @groups = query(GROUP, :name)

        # restrict the groups to the groups of the current user
        # unless the current user is allowed to create groups
        # and need to see all
        unless allowed(:create)
          allowed_group_ids = current_user.groups.collect {|g| g.id }
          @groups.delete_if do |g|
            ! allowed_group_ids.member?(g.id)
          end
        end

        respond_to do |format|
          format.html
          format.xml  { render :xml => @groups }
        end
      end

      # GET /groups/1
      # GET /groups/1.xml
      def show
        @group = GROUP.first_or_get!(params[:id])

        # restrict the groups to the groups of the current user
        # unless the current user is allowed to create groups
        # and need to see all
        unless allowed(:create)
          allowed_groups = current_user.groups
        end

        respond_to do |format|
          format.html # show.html.erb
          format.xml  { render :xml => @group }
        end
      end

      # GET /groups/new
      # GET /groups/new.xml
      def new
        @group = GROUP.new

        respond_to do |format|
          format.html # new.html.erb
          format.xml  { render :xml => @group }
        end
      end

      # GET /groups/1/edit
      def edit
        @group = GROUP.first_or_get!(params[:id])
      end

      # POST /groups
      # POST /groups.xml
      def create
        group = params[:group] || {}
        group.delete(:locales)
        group.delete(:domains)
        @group = GROUP.new(group)
        @group.current_user = current_user

        respond_to do |format|
          if @group.save
            flash[:notice] = 'Group was successfully created.'
            format.html { redirect_to(group_url(@group.id)) }
            format.xml  { render :xml => @group, :status => :created, :location => group_url(@group.id) + ".xml" }
          else
            format.html { render :action => "new" }
            format.xml  { render :xml => @group.errors, :status => :unprocessable_entity }
          end
        end
      end

      # PUT /groups/1
      # PUT /groups/1.xml
      def update
        @group = GROUP.first_or_get!(params[:id])
        @group.current_user = current_user

        @group.update_children((params[:group] || {}).delete(:locales), :locale)

        respond_to do |format|
          if @group.update(params[:group]) or not @group.dirty?
            flash[:notice] = 'Group was successfully updated.'
            format.html { redirect_to(group_url(@group.id)) }
            format.xml  { render :xml => @group }
          else
            format.html { render :action => "edit" }
            format.xml  { render :xml => @group.errors, :status => :unprocessable_entity }
          end
        end
      end

      # DELETE /groups/1
      # DELETE /groups/1.xml
      def destroy
        @group = GROUP.first_or_get(params[:id])
        @group.current_user = current_user
        @group.destroy if @group

        respond_to do |format|
          flash[:notice] = 'Group was successfully deleted.'
          format.html { redirect_to(groups_url) }
          format.xml  { head :ok }
        end
      end
    end
  end
end
