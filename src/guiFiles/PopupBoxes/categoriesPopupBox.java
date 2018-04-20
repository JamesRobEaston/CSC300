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

import clientServerPackage.*;
import businessPlanClasses.*;
import guiFiles.BPApplication;
import guiFiles.screens.*;

public class categoriesPopupBox implements PopupBoxInterface
{

	public static Stage categoriesPopupBox;
	
	private static VBox categoriesNode;
	private static ArrayList<TextField> categoryInputs;
	private static BP businessPlan;
	
	public categoriesPopupBox(ClientProxy client, BPApplication application)
	{
		categoriesPopupBox = new Stage();
		categoriesPopupBox.setWidth(500.0);
		categoriesPopupBox.setHeight(350.0);
		categoriesPopupBox.initModality(Modality.APPLICATION_MODAL);
		categoriesPopupBox.setTitle("Create Categories");
		categoriesPopupBox.setResizable(false);
		
		BorderPane rootNode = new BorderPane();
		categoriesNode = new VBox(10);
		categoryInputs = new ArrayList<TextField>();
		resetCategoriesNode(client, application, businessPlan);
		
		ScrollPane scroller = new ScrollPane(categoriesNode);
		
		rootNode.setCenter(scroller);
		rootNode.setAlignment(scroller, Pos.CENTER);
		Scene scene = new Scene(rootNode);
		categoriesPopupBox.setScene(scene);
	}
	
	private static void resetCategoriesNode(ClientProxy client, BPApplication application, BP bp)
	{
		categoriesNode.getChildren().clear();
		categoryInputs.clear();
		Label categoriesLabel = new Label("Please enter your list of categories:");
		
		HBox buttons = new HBox(30);
		
		Button newCategoryButton = new Button("New Category");
		newCategoryButton.setOnAction(e ->
		{
			addNewTextField("Category name", true);
		});
		
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e ->
		{
			for(int i = 0; i < categoryInputs.size(); i++)
			{
				TextField categoryInput = categoryInputs.get(i);
				String categoryName = categoryInput.getText();
				PlanDesign design = businessPlan.getDesign();
				design.addCategory(categoryInput.getText(), i + 1, 0, 10000000);
			}
			businessPlan.setCategoryList();
			new ViewBPScreen(businessPlan, client, application);
			categoriesPopupBox.close();
		});
		
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e ->
		{
			categoriesPopupBox.close();
			resetCategoriesNode(client, application, businessPlan);
		});
		
		buttons.getChildren().addAll(newCategoryButton, submitButton, cancelButton);
		
		HBox presetPlans = new HBox(20);
		Button centreButton = new Button("Centre");
		centreButton.setOnAction(e -> centreModel());
		Button VMOSAButton = new Button("VMOSA");
		VMOSAButton.setOnAction(e -> VMOSAModel());
		presetPlans.getChildren().addAll(centreButton, VMOSAButton);
		
		categoriesNode.getChildren().addAll(categoriesLabel, buttons, presetPlans);
	}
	
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
	
	private static void centreModel()
	{
		addNewTextField("Department", false);
		addNewTextField("Student Learning Objective", false);
	}
	
	private static void VMOSAModel()
	{
		addNewTextField("Objective", false);
		addNewTextField("Strategy", false);
		addNewTextField("Action Plan", false);
	}
	
	@Override
	public Stage getPopupBox()
	{
		return categoriesPopupBox;
	}
	
	public static void show(BP bp)
	{
		businessPlan = bp;
		categoriesPopupBox.show();
	}

}
