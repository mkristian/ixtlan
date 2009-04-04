class Views::Sessions::Login < Erector::Widget
  def render
    instruct
    html :xmlns => "http://www.w3.org/1999/xhtml" do
      head do
        title "users login" 
        css "/users.css"
      end
      body :id => :login do
        
        fieldset do
          legend "login"
          
          if flash[:notice]
            div(flash[:notice], :class => :message)
          end

          form :method => :post do

            div :class => :input do
              text "User Name: "
              text_field_tag :username
            end
            
            div :class => :input do
              text "Password: "
              password_field_tag :password
            end
            
            div :class => :input do
              submit_tag("Login", :class => :action_button)
            end
          end
        end
      end
      
      fieldset do
        legend "password forgotten ?"
     
        if flash[:reset_password_notice]
          div(flash[:reset_password_notice], :class => [:reset_password, :message])
          flash.clear
        end

        form_for(:reset_password, :url => reset_passwords_path.to_s ) do |f| 
          div :class => :input do
            text "Email Address: "
            text_field_tag :email
          end
          rawtext helpers.hidden_field_tag "reset_password[success_url]", helpers.request.path
          
          div :class => :input do
            submit_tag("Send Password", :class => :action_button)
          end
        end
      end
    end
  end
end
