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
	private TextField planYearField;

	@FXML
	private TextField planIDField;

	@FXML
	private Button createPlanButton;

	@FXML
	private Button closePopupButton;
	
    @FXML
    public ChoiceBox<Department> departmentChoiceBox;

    @FXML
    public Label departmentChoiceBoxLabel;

	@FXML
	private Label errorText;

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

		if (id.replaceAll("\\s+", "").equals(""))
		{
			errorText.setText("Please enter a valid ID.");
			planIDField.setText("");
		} 
		else if (year.replaceAll("\\s+", "").equals(""))
		{
			errorText.setText("Please enter a valid year.");
			planYearField.setText("");
		} 
		else
		{
			model.retrieve(id + " " + year);
			if (model.getBusinessPlan() == null && !isInt(year))
			{
				if (!isClone)
				{
					if(!department.equals(""))
					{
						businessPlan = new BP(year, id, department);
						model.setBusinessPlan(businessPlan);
						model.closePopupBox();
						model.showCategoryPopupBox();
					}
				} else
				{
					businessPlan = model.getBusinessPlan();
					BP newBP = businessPlan.copy();
					newBP.setID(id + " " + year);
					newBP.setYear(year);
					model.setLocalCopy(businessPlan);
					model.showBusinessPlanScreen();
				}
				
				if(!department.equals(""))
				{
					planIDField.setText("");
					planYearField.setText("");
				}
			} else if (!isInt(year))
			{
				errorText.setText("Please enter a valid year.");
			} else
			{
				errorText.setText("A Business Plan already has that name and year.");
			}

		}
	}

	private boolean isInt(String integer)
	{
		try
		{
			Integer.parseInt(integer);
			return true;
		} catch (NumberFormatException e)
		{
		}
		return false;
	}

}
