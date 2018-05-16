package newBPPopup;

import java.io.IOException;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import applicationFiles.BPApplication;
import businessPlanClasses.BusinessPlan;
import businessPlanView.BusinessPlanScreenController;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import clientServerPackage.Department;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import model.ModelInterface;

public class NewBPPopupBoxController
{

	Stage popupStage;
	FXMLLoader loader;
	Scene newBPScene;
	boolean isClone;
	BP businessPlan;
	ModelInterface model;
	public String department;

	@FXML
	public TextField planYearField;

	@FXML
	public TextField planIDField;

	@FXML
	private Button createPlanButton;

	@FXML
	private Button closePopupButton;
	
    @FXML
    public ChoiceBox<Department> departmentChoiceBox;

    @FXML
    public Label departmentChoiceBoxLabel;

	@FXML
	public Label errorText;

	public void setModel(ModelInterface model)
	{
		this.model = model;
	}

	public void setIsClone(boolean isClone)
	{
		this.isClone = isClone;
	}

	@FXML
	void closePopup(ActionEvent event)
	{
		model.closePopupBox();
	}

	@FXML
	void createPlan(ActionEvent event)
	{
		String id = planIDField.getText();
		String year = planYearField.getText();
		if(model.isAdmin())
		{
			Model.currDepartment = departmentChoiceBox.getValue();
		}
		
		model.createNewBP(id, year, Model.currDepartment.getDepartmentName(), isClone, this);
	}

}
