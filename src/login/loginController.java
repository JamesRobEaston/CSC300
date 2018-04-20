package login;
import java.awt.event.ActionEvent;
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

	    Scene loginScreen;
	    
	   public loginController(Model model)
	   {
		    FXMLLoader loader = new FXMLLoader();
			loader.setLocation(loginController.class.getResource("/login/LoginView.fxml"));
			try 
			{
				loginScreen = new Scene(loader.load());
				this.setModel(model);
				model.notify(loginScreen);
			}
			catch (Exception e) 
			{
				
				e.printStackTrace();
			}
	   }
	   
	   public loginController()
	   {
		   
	   }
	    
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

		public Scene getScene()
		{
			return loginScreen;
		}

}

