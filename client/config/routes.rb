ActionController::Routing::Routes.draw do |map|
  map.resources :locations, :member => {:edit => [:post,:delete]}
  map.connect ':locations', :controller => ':locations', :conditions => { :method => [:delete] }
  map.connect ':locations/:id', :controller => ':locations', :conditions => { :method => [:post] }

  map.resources :course_type_locations, :member => {:edit => [:post,:delete]}
  map.connect ':course_type_locations', :controller => ':course_type_locations', :conditions => { :method => [:delete] }
  map.connect ':course_type_locations/:id', :controller => ':course_type_locations', :conditions => { :method => [:post] }

  map.resources :course_types, :member => {:edit => [:post,:delete]}
  map.connect ':course_types', :controller => ':course_types', :conditions => { :method => [:delete] }
  map.connect ':course_types/:id', :controller => ':course_types', :conditions => { :method => [:post] }

  map.resources :locations

  map.resources :course_type_locations

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_types

  map.resources :course_type_locations

  map.resources :course_type_locations

  map.resources :course_types

  map.resources :course_typeses

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resources :locations

  map.resource :configuration#, :member => {:edit => [:post,:delete]}

  map.resource :welcome

  map.resources :dispatches

  # The priority is based upon order of creation: first created -> highest priority.

  # Sample of regular route:
  #   map.connect 'products/:id', :controller => 'catalog', :action => 'view'
  # Keep in mind you can assign values other than :controller and :action

  # Sample of named route:
  #   map.purchase 'products/:id/purchase', :controller => 'catalog', :action => 'purchase'
  # This route can be invoked with purchase_url(:id => product.id)

  # Sample resource route (maps HTTP verbs to controller actions automatically):
  #   map.resources :products

  # Sample resource route with options:
  #   map.resources :products, :member => { :short => :get, :toggle => :post }, :collection => { :sold => :get }

  # Sample resource route with sub-resources:
  #   map.resources :products, :has_many => [ :comments, :sales ], :has_one => :seller
  
  # Sample resource route with more complex sub-resources
  #   map.resources :products do |products|
  #     products.resources :comments
  #     products.resources :sales, :collection => { :recent => :get }
  #   end

  # Sample resource route within a namespace:
  #   map.namespace :admin do |admin|
  #     # Directs /admin/products/* to Admin::ProductsController (app/controllers/admin/products_controller.rb)
  #     admin.resources :products
  #   end

  # You can have the root of your site routed with map.root -- just remember to delete public/index.html.
  # map.root :controller => "welcome"

  # See how all your routes lay out with "rake routes"

  # Install the default routes as the lowest priority.
  # Note: These default routes make all actions in every controller accessible via GET requests. You should
  # consider removing the them or commenting them out if you're using named routes and resources.
  map.connect ':controller/:action/:id'
  map.connect ':controller/:action/:id.:format'
end
