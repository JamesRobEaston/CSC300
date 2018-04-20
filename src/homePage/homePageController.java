package homePage;

import java.io.IOException;

import applicationFiles.BPApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import model.Model;


public class homePageController
{
	Model model;

	    @FXML
	    private Button logout_but;

	    @FXML
	    private Button addUser_but;

	    @FXML
	    private Button addDept_but;

	    @FXML
	    private Button newBP_but;

	    @FXML
	    private Button viewAll_but;

	    @FXML
	    private Button loadlc_but;
	    
	    public Scene homePage;
	    
	    public homePageController(Model model)
	    {
	    	FXMLLoader loader = new FXMLLoader();
			loader.setLocation(BPApplication.class.getResource("HomePageView.fxml"));
			try 
			{
				homePage = new Scene(loader.load());
				this.setModel(model);
				model.notify(homePage);
			}
			catch (IOException e) 
			{
				
				e.printStackTrace();
			}
	    }
	    
	    public homePageController() {}
	    
	    public void setModel(Model model)
	    {
	    	  this.model=model;
	    	
	    	
	    }

	    @FXML	    
	    void addDept(ActionEvent event) {

	    }

	    @FXML
	    void addUser(ActionEvent event) {

	    }

	    @FXML
	    void loadLocalCopy(ActionEvent event) {

	    }

	    @FXML
	    void newBP(ActionEvent event) {

	    }

	    @FXML
	    void viewAllPlans(ActionEvent event) {

	    }
	

}
