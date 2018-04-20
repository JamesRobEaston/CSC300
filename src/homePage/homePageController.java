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
import model.Model;
import newBPPopup.NewBPPopupBoxController;
import viewAllBPView.ViewAllBPScreenController;

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

	public homePageController() {}
	
	public homePageController(Model model)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BPApplication.class.getResource("HomePageView.fxml"));
		try
		{
			homePage = new Scene(loader.load());
			this.setModel(model);
		} catch (IOException e)
		{

			e.printStackTrace();
		}
	}

	public void setModel(Model model)
	{
		this.model = model;

	}

	@FXML
	void addDept(ActionEvent event)
	{
		new AddDepartmentPopupBoxController(model);
	}

	@FXML
	void addUser(ActionEvent event) throws IOException
	{
		new AddNewUserPopupBoxController(model);
	}

	@FXML
	void loadLocalCopy(ActionEvent event)
	{
		new BusinessPlanScreenController(model);
	}

	@FXML
	void newBP(ActionEvent event) throws IOException
	{
		new NewBPPopupBoxController(model, false);
	}

	@FXML
	void viewAllPlans(ActionEvent event)
	{
		new ViewAllBPScreenController(model);
	}
	
	public Scene getScene()
	{
		return homePage;
	}

}
