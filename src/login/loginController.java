package login;
import java.awt.event.ActionEvent;

import homePage.homePageController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.Model;

public class loginController {

    @FXML
    private AnchorPane login_view;

    @FXML
    private Button login_but;

    @FXML
    private PasswordField pass_input;

    @FXML
    private TextField serv_input;

    @FXML
    private TextField userName_input;

	   
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
	    		  model.showHome();
	    	  }
	    	
	    }

}

