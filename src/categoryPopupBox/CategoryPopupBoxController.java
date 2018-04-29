package categoryPopupBox;

import java.io.IOException;
import java.util.ArrayList;

import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import applicationFiles.BPApplication;
import businessPlanClasses.PlanDesign;
import businessPlanView.BusinessPlanScreenController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.ModelInterface;

public class CategoryPopupBoxController
{

	@FXML
	private Button newCategoryButton;

	@FXML
	private Button submitButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Button centreButton;

	@FXML
	private Button VMOSAButton;
	
    @FXML
    private VBox categoriesNode;
    
	public ArrayList<TextField> categoryInputs;
	private BP businessPlan;
	public Stage categoriesPopupBox;

	ModelInterface model;

	@FXML
	void VMOSAModel(ActionEvent event)
	{
		addNewTextField("Vision", false);
		addNewTextField("Mission", false);
		addNewTextField("Objective", false);
		addNewTextField("Strategy", false);
		addNewTextField("Action Plan", false);
	}

	@FXML
	void addNewCategory(ActionEvent event)
	{
		addNewTextField("Category Name", true);
	}

	@FXML
	void cancel(ActionEvent event)
	{
		model.closePopupBox();
	}

	@FXML
	void centreModel(ActionEvent event)
	{
		addNewTextField("Organization", false);
		addNewTextField("Department", false);
		addNewTextField("Goal", false);
		addNewTextField("Student Learning Objective", false);
	}

	@FXML
	void submit(ActionEvent event)
	{
		businessPlan = model.getBusinessPlan();
		for(int i = 0; i < categoryInputs.size(); i++)
		{
			TextField categoryInput = categoryInputs.get(i);
			String categoryName = categoryInput.getText();
			PlanDesign design = businessPlan.getDesign();
			design.addCategory(categoryName, i + 1, 0, 10000000);
		}
		businessPlan.setCategoryList();
		model.setLocalCopy(businessPlan);
		model.showBusinessPlanScreen();
		model.closePopupBox();
	}
	
	//Helper method
	private void addNewTextField(String text, boolean isPromptText)
	{
		if(categoryInputs==null)
		{
			categoryInputs = new ArrayList<TextField>();
		}
		HBox newTextFieldBox = new HBox(20);
		TextField newTextField = new TextField();
		if(isPromptText)
		{
			newTextField.setPromptText(text);
		}
		else
		{
			newTextField.setText(text);
		}
		
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(e ->
		{
			categoriesNode.getChildren().remove(newTextFieldBox);
			categoryInputs.remove(newTextField);
		});
		
		newTextFieldBox.getChildren().addAll(newTextField, deleteButton);
		
		categoryInputs.add(newTextField);
		categoriesNode.getChildren().add(categoriesNode.getChildren().size() - 2,newTextFieldBox);
	}

	public void show()
	{
		categoriesPopupBox.show();
	}
	
	public void setModel(ModelInterface model)
	{
		this.model = model;
	}
	
}
