package login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;


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
	    
	    public void setModel(Model model)
	    {
	    	  this. model=model;
	    	
	    	
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
