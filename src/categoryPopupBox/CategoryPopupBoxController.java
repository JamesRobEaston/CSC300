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
    private static VBox categoriesNode;
	private static ArrayList<TextField> categoryInputs;
	private static BP businessPlan;
	public static Stage categoriesPopupBox;
	
	BPApplication application;
	ClientProxy client;

	public CategoryPopupBoxController() {}
	
	public CategoryPopupBoxController(ClientProxy client, BPApplication application)
	{
		this.application = application;
		this.client = client;
		
		categoriesPopupBox = new Stage();
		categoriesPopupBox.setWidth(500.0);
		categoriesPopupBox.setHeight(350.0);
		categoriesPopupBox.initModality(Modality.APPLICATION_MODAL);
		categoriesPopupBox.setTitle("Create Categories");
		categoriesPopupBox.setResizable(false);
		
		categoryInputs = new ArrayList<TextField>();
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BusinessPlanScreenController.class.getResource("businessPlanView/BusinessPlanScreen.fxml"));
		
		try
		{
			categoriesPopupBox.setScene(new Scene(loader.load()));
		} catch (IOException e1)
		{
			e1.printStackTrace();
			categoriesPopupBox.setScene(new Scene(new VBox()));
		}
	}

	@FXML
	void VMOSAModel(ActionEvent event)
	{
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
		categoriesPopupBox.close();
	}

	@FXML
	void centreModel(ActionEvent event)
	{
		addNewTextField("Department", false);
		addNewTextField("Student Learning Objective", false);
	}

	@FXML
	void submit(ActionEvent event)
	{
		for(int i = 0; i < categoryInputs.size(); i++)
		{
			TextField categoryInput = categoryInputs.get(i);
			String categoryName = categoryInput.getText();
			PlanDesign design = businessPlan.getDesign();
			design.addCategory(categoryName, i + 1, 0, 10000000);
		}
		businessPlan.setCategoryList();
		BusinessPlanScreenController bpScreen = new BusinessPlanScreenController(businessPlan, client, application);
		categoriesPopupBox.close();
		application.notify(bpScreen.getScene());
	}
	
	//Helper method
	private static void addNewTextField(String text, boolean isPromptText)
	{
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
		categoriesNode.getChildren().add(categoriesNode.getChildren().size() - 2, newTextFieldBox);
	}

	public void show()
	{
		categoriesPopupBox.show();
	}
	
}
