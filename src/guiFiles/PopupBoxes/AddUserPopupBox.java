package guiFiles.PopupBoxes;

import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;

import clientServerPackage.*;
import guiFiles.BPApplication;
import guiFiles.DepartmentConverter;

public class AddUserPopupBox implements PopupBoxInterface
{

	public static Stage addUserPopupBox;
	
	//A drop down box to display the departments.
	//This variable needs to be global so that whenever a new department has been added,
	//the list of available departments displayed by this drop down box can be updated.
	private static ChoiceBox<Department> dropDownMenu;
	
	Department adminDepartment;
	
	public AddUserPopupBox(ClientProxy client, BPApplication application)
	{
		//Create the stage to add the new user
		addUserPopupBox = new Stage();
		addUserPopupBox.setWidth(500.0);
		addUserPopupBox.setHeight(350.0);
		addUserPopupBox.initModality(Modality.APPLICATION_MODAL);
		addUserPopupBox.setTitle("Add New User");
		addUserPopupBox.setResizable(false);
		
		//Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();
	
			GridPane informationPane = new GridPane();
			//Create a VBox to store the labels in
			VBox labelBox = new VBox(10);
			
			Label usernameLabel = new Label("Username:");
			Label passwordLabel = new Label("Password:");
			Label adminLabel = new Label("Admin:");
			
			usernameLabel.setAlignment(Pos.CENTER_LEFT);
			passwordLabel.setAlignment(Pos.CENTER_LEFT);
			adminLabel.setAlignment(Pos.CENTER_LEFT);
			
			labelBox.getChildren().addAll(usernameLabel, passwordLabel, adminLabel);
			informationPane.add(usernameLabel, 0, 0);
			informationPane.add(passwordLabel, 0, 1);
			informationPane.add(adminLabel, 0, 2);
			
			//Create a VBox to allow the user to enter information
			VBox inputBox = new VBox(10);
			
			TextField usernameInput = new TextField();
			TextField passwordInput = new TextField();
			CheckBox adminBox = new CheckBox();
			
			inputBox.getChildren().addAll(usernameInput, passwordInput, adminBox);
			informationPane.add(usernameInput, 1, 0);
			informationPane.add(passwordInput, 1, 1);
			informationPane.add(adminBox, 1, 2);
			
			//Create an HBox to hold all of the input information
			HBox inputInformationBox = new HBox(10);
			inputInformationBox.getChildren().addAll(labelBox, inputBox);
			
			//Create a ChoiceBox to allow the user to choose their department.
			ArrayList<Department> departments = client.getAllDepartments();
			dropDownMenu = new ChoiceBox<Department>();
			dropDownMenu.setConverter(new DepartmentConverter<Department>());
			adminDepartment = null;
			
			for(Department department : departments)
			{
				if(!department.getName().equals("Admin"))
				{
					dropDownMenu.getItems().add(department);
				}
				else
				{
					adminDepartment = department;
				}
			}
			
			adminBox.setOnAction(e ->
			{
				if(adminBox.isSelected()) {
					dropDownMenu.getItems().add(adminDepartment);
					dropDownMenu.setValue(adminDepartment);
				}
				else
				{
					dropDownMenu.getItems().remove(adminDepartment);
					dropDownMenu.setValue(null);
				}
			});
			
			//Create an empty label to notify the user if the login information is invalid
			Label invalidUserLabel = new Label();
			invalidUserLabel.setAlignment(Pos.CENTER);
			
			//Create an HBox to store the buttons to create a new user and to cancel.
			HBox buttons = new HBox(50);
			
			Button createNewUserButton = new Button("Create User");
			createNewUserButton.setOnAction(e -> 
			{
				//Extract the new user's information from the boxes
				String username = usernameInput.getText();
				String password = passwordInput.getText();
				Department department = dropDownMenu.getValue();
				boolean isAdmin = adminBox.isSelected();
				boolean successfullyAddedUser = false;
				
				//Ensure that the entered information is valid
				if(username == null || username.replaceAll("\\s+","").equals(""))
				{
					invalidUserLabel.setText("Please enter a valid username.");
					usernameInput.setText("");
				}
				else if(password == null || password.replaceAll("\\s+","").equals(""))
				{
					invalidUserLabel.setText("Please enter a valid password.");
					passwordInput.setText("");
				}
				else if(department == null)
				{
					invalidUserLabel.setText("Please enter a valid department.");
				}
				
				//If adminBox is checked, create an admin, otherwise create a regular client.
				else 
				{
					if(isAdmin)
					{
						try
						{
							successfullyAddedUser = client.addUser(username, password, department.getName(), true);
						} catch (Exception e1)
						{
							e1.printStackTrace();
						}
					}
					else
					{
						try
						{
							successfullyAddedUser = client.addUser(username, password, department.getName(),false);
						} catch (Exception e1)
						{
							e1.printStackTrace();
						}
					}
					
					//If the user was not added correctly, notify the user. Otherwise, close the popup box.
					if(!successfullyAddedUser)
					{
						invalidUserLabel.setText("A user with that username already exists.");
					}
					else
					{
						addUserPopupBox.close();
						invalidUserLabel.setText("");
					}
				}
			});
			
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e ->
			{
				addUserPopupBox.close();
				invalidUserLabel.setText("");
				usernameInput.setText("");
				passwordInput.setText("");
			});
			
			buttons.getChildren().addAll(createNewUserButton, cancelButton);
			buttons.setAlignment(Pos.CENTER);
			
			//Create a VBox to hold all of the elements in the scene
			VBox informationNode = new VBox(10);
			informationNode.getChildren().addAll(dropDownMenu, invalidUserLabel, buttons);
			informationNode.setAlignment(Pos.CENTER);
			
			informationPane.add(informationNode, 0, 4);
			GridPane.setColumnSpan(informationNode, 2);
			GridPane.setMargin(usernameLabel, new Insets(5, 15, 10, 15));
			GridPane.setMargin(passwordLabel, new Insets(5, 15, 10, 15));
			GridPane.setMargin(adminLabel, new Insets(5, 15, 5, 15));
				
		AnchorPane.setTopAnchor(informationPane, 100.0);
		AnchorPane.setRightAnchor(informationPane, 100.0);
		AnchorPane.setLeftAnchor(informationPane, 100.0);
		AnchorPane.setBottomAnchor(informationPane, 100.0);
		
		rootNode.getChildren().add(informationPane);
		
		Scene scene = new Scene(rootNode);
		addUserPopupBox.setScene(scene);
	}
	
	@Override
	public Stage getPopupBox()
	{
		return addUserPopupBox;
	}
	
	//To be called by AddDepartmentPopupBox whenever a new department has been added.
	public static void addDepartment(Department department)
	{
		dropDownMenu.getItems().add(department);
	}
}

