Guard::Guard.initialize(:locations, 
                 { :index => [:reg, :reg_admin], 
                   :show => [:reg, :reg_admin], 
                   :edit => [:reg_admin], 
                   :update => [:reg_admin], 
                   :new => [:reg_admin], 
                   :create => [:reg_admin], 
                   :destroy => [:reg_admin] })
