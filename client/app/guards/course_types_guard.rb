Guard::Guard.initialize(:course_types, 
                 { :index => [:reg, :reg_admin], 
                   :show => [:reg, :reg_admin], 
                   :edit => [], 
                   :update => [], 
                   :new => [], 
                   :create => [], 
                   :destroy => [] })
