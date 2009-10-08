Guard::Guard.initialize(:groups, 
                 { :index => [:admin, :useradmin, :users], 
                   :show => [:admin, :useradmin, :users], 
                   :edit => [:admin], 
                   :update => [:admin], 
                   :new => [:admin], 
                   :create => [:admin], 
                   :destroy => [:admin] })
