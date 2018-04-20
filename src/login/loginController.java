package login;
import java.awt.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {
	

	  @FXML
	    private Button login_but;

	    @FXML
	    private TextField userName_input;

	    @FXML
	    private PasswordField pass_input;

	    @FXML
	    private TextField serv_input;
	   
	    Model model;

	
	    
	    public void setModel(Model model)
	    {
	    	  this. model=model;
	    	
	    	
	    }
	    
	   
	    @FXML
	    void authenticate()
	    {
	    	  boolean foundUser=model.authenticate(userName_input,pass_input,serv_input,login_but);
	    	  if (foundUser)
	    	  {
	    		  
	    		  Main.showHomePage();
	    	  }
	    	
	    }

}

