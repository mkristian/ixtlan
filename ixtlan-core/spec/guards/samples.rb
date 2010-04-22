Ixtlan::Guard.initialize(:permissions,
                         { :show => [:guest],
                           :edit => [:root, :admin],
                           :update => [:root, :admin],
                           :destroy => [:root] }
                         )

Ixtlan::Guard.initialize(:configurations,
                         { :show => [:admin],
                           :edit => [:root],
                           :update => [:root] }
                         )
