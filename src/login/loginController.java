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
import model.ModelInterface;

public class loginController {

    @FXML
    private AnchorPane login_view;

    @FXML
    public Button login_but;

    @FXML
    public PasswordField pass_input;

    @FXML
    public TextField serv_input;

    @FXML
	public TextField userName_input;

	   
	    ModelInterface model;
	    
	    public void setModel(ModelInterface model)
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

