package homePage;

import java.io.IOException;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import addUserPopup.AddNewUserPopupBoxController;
import applicationFiles.BPApplication;
import businessPlanView.BusinessPlanScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import model.ModelInterface;
import newBPPopup.NewBPPopupBoxController;
import viewAllBPView.ViewAllBPScreenController;

public class homePageController
{
	ModelInterface model;

	@FXML
	private Button logout_but;

	@FXML
	private Button addUser_but;

	@FXML
	private Button addDept_but;

	@FXML
	private Button newBP_but;
	
    @FXML
    public Label departmentLabel;

	@FXML
	private Button viewAll_but;

	@FXML
	private Button loadlc_but;

	private AnchorPane homePage_view;

	public void setModel(ModelInterface model)
	{
		this.model = model;

	}

	@FXML
	void addDept(ActionEvent event)
	{
		model.showAddDepartmentPopupBox();
	}

	@FXML
	void addUser(ActionEvent event) throws IOException
	{
		model.showAddUserPopupBox();
	}

	@FXML
	void loadLocalCopy(ActionEvent event)
	{
		model.loadLocalCopy();
		model.showBusinessPlanScreen();
	}

	@FXML
	void newBP(ActionEvent event)
	{
		model.showNewBPPopupBox(false);
	}

	@FXML
	void viewAllPlans(ActionEvent event)
	{
		model.showViewAllBPScreen();
	}

	@FXML
	void logout(ActionEvent event)
	{
		model.showLogin();
	}

}
