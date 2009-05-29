Guard::Guard.initialize(:users, 
                 { :index => [:useradmin, :users], 
                   :show => [:useradmin, :users], 
                   :edit => [:useradmin], 
                   :update => [:useradmin], 
                   :new => [:useradmin], 
                   :create => [:useradmin], 
                   :destroy => [:useradmin] })
