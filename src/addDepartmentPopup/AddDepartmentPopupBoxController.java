package addDepartmentPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import applicationFiles.BPApplication;
import clientServerPackage.ClientProxy;
import clientServerPackage.Department;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;

public class AddDepartmentPopupBoxController {

	Stage popupStage;
	FXMLLoader loader;
	Scene newDepartmentScene;
	Model model;
	
    @FXML
    private TextField deptNameField;

    @FXML
    private Button createDepartmentButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorText;

    public AddDepartmentPopupBoxController() {}
    
    public AddDepartmentPopupBoxController(Model model) throws IOException {
    	this.model = model;
		loader = new FXMLLoader();
		loader.setLocation(AddDepartmentPopupBoxController.class.getResource("AddDepartmentPopupBox.fxml"));
		newDepartmentScene = new Scene(loader.load());
		
		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Add Department");
		popupStage.setResizable(false);
		popupStage.setScene(newDepartmentScene);
    }
    
    @FXML
    void closePopup(ActionEvent event) {
    	popupStage.close();
    }

    @FXML
    void createNewDepartment(ActionEvent event) {
    	//Extract the new department's information from the boxes
		String departmentName = deptNameField.getText();
		boolean successfullyAddedDepartment = false;
		
		//Ensure that the entered information is valid
		if(departmentName == null || departmentName.replaceAll("\\s+","").equals(""))
		{
			errorText.setText("Please enter a valid department name.");
			deptNameField.setText("");
		}
		
		//If the entered information was valid, create the department
		else 
		{
			//Try to create the department
			try
			{
				successfullyAddedDepartment = model.addDepartment(departmentName);
			} catch (Exception e1)
			{
				e1.printStackTrace();
			}
			
			//If the department was not added correctly, notify the user.
			if(!successfullyAddedDepartment)
			{
				errorText.setText("A department with that name already exists.");
			}
			//Otherwise, reset the box and notify AddUserPopupBox of the new department.
			else
			{
				deptNameField.setText("");
				ArrayList<Department> departments = model.getAllDepartments();
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
				}
				popupStage.close();
			}
		}
    }
    
    public void show()
    {
    	popupStage.show();
    }

}
