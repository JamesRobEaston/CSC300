package addUserPopup;

import java.io.IOException;
import java.util.ArrayList;

import applicationFiles.BPApplication;
import applicationFiles.DepartmentConverter;
import clientServerPackage.ClientProxy;
import clientServerPackage.Department;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;

public class AddNewUserPopupBoxController
{

	Stage popupStage;
	FXMLLoader loader;
	Scene newUserScene;
	Model model;

	@FXML
	private CheckBox newIsAdmin;

	@FXML
	public ChoiceBox<Department> newDepartment;

	@FXML
	private TextField newPassword;

	@FXML
	private TextField newUsername;

	@FXML
	private Button createUserButton;

	@FXML
	private Button newUserCancel;
	
	private Label errorText;

	@FXML
	void closePopup(ActionEvent event)
	{
		model.closePopupBox();
	}

	@FXML
	void createNewUser(ActionEvent event)
	{
		//Extract the new user's information from the boxes
		String username = newUsername.getText();
		String password = newPassword.getText();
		Department department = newDepartment.getValue();
		boolean isAdmin = newIsAdmin.isSelected();
		boolean successfullyAddedUser = false;
		
		//Ensure that the entered information is valid
		if(username == null || username.replaceAll("\\s+","").equals(""))
		{
			errorText.setText("Please enter a valid username.");
			newUsername.setText("");
		}
		else if(password == null || password.replaceAll("\\s+","").equals(""))
		{
			errorText.setText("Please enter a valid password.");
			newPassword.setText("");
		}
		else if(department == null)
		{
			errorText.setText("Please enter a valid department.");
		}
		
		//If adminBox is checked, create an admin, otherwise create a regular client.
		else 
		{
			if(isAdmin)
			{
				try
				{
					successfullyAddedUser = model.addUser(username, password, department.getName(), true);
				} catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
			else
			{
				try
				{
					successfullyAddedUser = model.addUser(username, password, department.getName(),false);
				} catch (Exception e1)
				{
					e1.printStackTrace();
				}
			}
			
			//If the user was not added correctly, notify the user. Otherwise, close the popup box.
			if(!successfullyAddedUser)
			{
				errorText.setText("A user with that username already exists.");
			}
			else
			{
				model.closePopupBox();
			}
		}
	}

	@FXML
	void showAdminDept(ActionEvent event)
	{
		if (newIsAdmin.isSelected())
		{
			newDepartment.getItems().add(model.getAdminDepartment());
			newDepartment.setValue(model.getAdminDepartment());
		} else
		{
			newDepartment.getItems().remove(model.getAdminDepartment());
			newDepartment.setValue(null);
		}
	}
	
	public void setModel(Model model)
	{
		this.model = model;
	}

}
