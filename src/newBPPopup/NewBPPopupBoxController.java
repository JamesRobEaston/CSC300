package newBPPopup;

import java.io.IOException;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import applicationFiles.BPApplication;
import businessPlanClasses.BusinessPlan;
import businessPlanView.BusinessPlanScreenController;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
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

public class NewBPPopupBoxController {

	Stage popupStage;
	FXMLLoader loader;
	Scene newBPScene;
	boolean isClone;
	BP businessPlan;
	Model model;
	
    @FXML
    private TextField planYearField;

    @FXML
    private TextField planIDField;

    @FXML
    private Button createPlanButton;

    @FXML
    private Button closePopupButton;
    
    @FXML
    private Label errorText;

    public NewBPPopupBoxController() {}
    
    public NewBPPopupBoxController(Model model, boolean isClone) throws IOException {
    	this.model = model;
		this.isClone = isClone;
		loader = new FXMLLoader();
		loader.setLocation(AddDepartmentPopupBoxController.class.getResource("AddDepartmentPopupBox.fxml"));
		newBPScene = new Scene(loader.load());
		
		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("New Business Plan");
		popupStage.setResizable(false);
		popupStage.setScene(newBPScene);
    }
    
    @FXML
    void closePopup(ActionEvent event) {

    }

    @FXML
    void createPlan(ActionEvent event) {
    	String id = planIDField.getText();
		String year = planYearField.getText();
		
		if(id.replaceAll("\\s+","").equals("")) 
		{
			errorText.setText("Please enter a valid ID.");
			planIDField.setText("");
		}
		else if(year.replaceAll("\\s+","").equals("")) 
		{
			errorText.setText("Please enter a valid year.");
			planYearField.setText("");
		}
		else 
		{
			model.retrieve(id + " " + year);
			if(model.getBusinessPlan() == null && isInt(year))
			{
				if(!isClone)
				{
					businessPlan = new BP(year, id);
					popupStage.show();
				}
				else
				{
					businessPlan = model.getBusinessPlan();
					BP newBP = businessPlan.copy();
					newBP.setID(id + " " +year);
					newBP.setYear(year);
					model.setLocalCopy(businessPlan);
					new BusinessPlanScreenController(model);
				}
				popupStage.close();
				planIDField.setText("");
				planYearField.setText("");
			}
			else if(!isInt(year))
			{
				errorText.setText("Please enter a valid year.");
			}
			else
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
		}
		catch(NumberFormatException e)
		{
		}
		return false;
	}

}
