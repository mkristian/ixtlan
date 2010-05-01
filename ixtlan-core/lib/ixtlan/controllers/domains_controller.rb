module Ixtlan
  module Controllers
    module DomainsController

      def self.included(base)
        base.send(:include, Ixtlan::Controllers::SearchQuery)
      end

      public

      # GET /domains
      # GET /domains.xml
      def index
        @domains = Domain.all(simple_query(:name))

        respond_to do |format|
          format.html
          format.xml  { render :xml => @domains }
        end
      end

      # GET /domains/1
      # GET /domains/1.xml
      def show
        @domain = Domain.first_or_get!(params[:id])

        respond_to do |format|
          format.html # show.html.erb
          format.xml  { render :xml => @domain }
        end
      end

      # GET /domains/new
      # GET /domains/new.xml
      def new
        @domain = Domain.new

        respond_to do |format|
          format.html # new.html.erb
          format.xml  { render :xml => @domain }
        end
      end

      # GET /domains/1/edit
      def edit
        @domain = Domain.first_or_get!(params[:id])
      end

      # POST /domains
      # POST /domains.xml
      def create
        @domain = Domain.new(params[:domain])
        @domain.current_user = current_user

        respond_to do |format|
          if @domain.save
            flash[:notice] = 'Domain was successfully created.'
            format.html { redirect_to(domain_url(@domain.id)) }
            format.xml  { render :xml => @domain, :status => :created, :location => domain_url(@domain.id) + ".xml" }
          else
            format.html { render :action => "new" }
            format.xml  { render :xml => @domain.errors, :status => :unprocessable_entity }
          end
        end
      end

      # PUT /domains/1
      # PUT /domains/1.xml
      def update
        @domain = Domain.first_or_get!(params[:id])
        @domain.current_user = current_user

        respond_to do |format|
          if @domain.update(params[:domain]) or not @domain.dirty?
            flash[:notice] = 'Domain was successfully updated.'
            format.html { redirect_to(domain_url(@domain.id)) }
            format.xml  { render :xml => @domain }
          else
            format.html { render :action => "edit" }
            format.xml  { render :xml => @domain.errors, :status => :unprocessable_entity }
          end
        end
      end

      # DELETE /domains/1
      # DELETE /domains/1.xml
      def destroy
        @domain = Domain.first_or_get(params[:id])
        @domain.destroy if @domain

        respond_to do |format|
          flash[:notice] = 'Domain was successfully deleted.'
          format.html { redirect_to(domains_url) }
          format.xml  { head :ok }
        end
      end
    end
  end
end
