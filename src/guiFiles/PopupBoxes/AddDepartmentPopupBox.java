package guiFiles.PopupBoxes;

import javafx.stage.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import clientServerPackage.*;
import guiFiles.BPApplication;

public class AddDepartmentPopupBox implements PopupBoxInterface
{
	
	public static Stage addDepartmentPopupBox;
	
	public AddDepartmentPopupBox(ClientProxy client, BPApplication application)
	{
		//Create the stage to add the new department
		addDepartmentPopupBox = new Stage();
		addDepartmentPopupBox.setWidth(500.0);
		addDepartmentPopupBox.setHeight(350.0);
		addDepartmentPopupBox.initModality(Modality.APPLICATION_MODAL);
		addDepartmentPopupBox.setTitle("Add New Department");
		addDepartmentPopupBox.setResizable(false);
		
		//Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();
		
			//Create an HBox to store the input information in
			HBox inputBox = new HBox(10);
			
			Label departmentNameLabel = new Label("Department Name:");
			departmentNameLabel.setAlignment(Pos.CENTER);
			
			TextField departmentNameInput = new TextField();
			departmentNameInput.setAlignment(Pos.CENTER);
					
			inputBox.getChildren().addAll(departmentNameLabel, departmentNameInput);
					
			//Create an empty label to notify the user if the department is invalid
			Label invalidDepartmentLabel = new Label();
			invalidDepartmentLabel.setAlignment(Pos.CENTER);
					
			//Create an HBox to store the buttons to create a new department and to cancel.
			HBox buttons = new HBox(50);
				
			Button createNewDepartmentButton = new Button("Create Department");
			createNewDepartmentButton.setOnAction(e -> 
			{
				//Extract the new department's information from the boxes
				String departmentName = departmentNameInput.getText();
				boolean successfullyAddedDepartment = false;
				
				//Ensure that the entered information is valid
				if(departmentName == null || departmentName.replaceAll("\\s+","").equals(""))
				{
					invalidDepartmentLabel.setText("Please enter a valid department name.");
					departmentNameInput.setText("");
				}
				
				//If the entered information was valid, create the department
				else 
				{
					//Try to create the department
					try
					{
						successfullyAddedDepartment = client.addDepartment(departmentName);
					} catch (Exception e1)
					{
						e1.printStackTrace();
					}
					
					//If the department was not added correctly, notify the user.
					if(!successfullyAddedDepartment)
					{
						invalidDepartmentLabel.setText("A department with that name already exists.");
					}
					//Otherwise, reset the box and notify AddUserPopupBox of the new department.
					else
					{
						departmentNameInput.setText("");
						ArrayList<Department> departments = client.getAllDepartments();
						if(departments != null)
						{
							Iterator<Department> iterator = departments.iterator();
							boolean found = false;
							Department department = null;
							while(!found && iterator.hasNext())
							{
								department = iterator.next();
								if(department.getName().equals(departmentName))
								{
									found = true;
								}
							}
							if(found)
							{
								AddUserPopupBox.addDepartment(department);
						
							}
						}
						addDepartmentPopupBox.close();
					}
				}
			});
			
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e ->
			{
				departmentNameInput.setText("");
				addDepartmentPopupBox.close();
			});
			
			buttons.getChildren().addAll(createNewDepartmentButton, cancelButton);
			buttons.setAlignment(Pos.CENTER);
			
			//Create a VBox to hold all of the elements in the scene
			VBox informationNode = new VBox(10);
			informationNode.getChildren().addAll(inputBox, invalidDepartmentLabel, buttons);
			informationNode.setAlignment(Pos.CENTER);
			
		//Center the elements in the scene
		AnchorPane.setTopAnchor(informationNode, 100.0);
		AnchorPane.setRightAnchor(informationNode, 100.0);
		AnchorPane.setLeftAnchor(informationNode, 100.0);
		AnchorPane.setBottomAnchor(informationNode, 100.0);
		
		rootNode.getChildren().add(informationNode);
				
		Scene scene = new Scene(rootNode);
		addDepartmentPopupBox.setScene(scene);
					
	}

	@Override
	public Stage getPopupBox()
	{
		return addDepartmentPopupBox;
	}

}
