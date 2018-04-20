package guiFiles.PopupBoxes;

import javafx.stage.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.rmi.RemoteException;
import java.util.HashMap;

import clientServerPackage.*;
import businessPlanClasses.*;
import guiFiles.screens.*;
import guiFiles.BPApplication;

public class EditOrCloneBPPopupBox implements PopupBoxInterface
{

	public static Stage editOrCloneBPPopupBox;
	public static BP businessPlan;
	
	public EditOrCloneBPPopupBox(ClientProxy client, BPApplication application)
	{
		//Create the stage to ask the user
		editOrCloneBPPopupBox = new Stage();
		editOrCloneBPPopupBox.setWidth(500.0);
		editOrCloneBPPopupBox.setHeight(350.0);
		editOrCloneBPPopupBox.initModality(Modality.APPLICATION_MODAL);
		editOrCloneBPPopupBox.setTitle("");
		editOrCloneBPPopupBox.setResizable(false);
		
		//Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();
		
		Label editOrCloneLabel = new Label("Would you like to edit or clone the Business Plan?");
		
			//Create an HBox to store the buttons to create a new department and to cancel.
			HBox buttons = new HBox(50);
			
			Button copyButton = new Button("Edit");
			copyButton.setOnAction(e -> 
			{
				new ViewBPScreen(businessPlan, client, application);
				editOrCloneBPPopupBox.close();
			});
			
			Button cloneButton = new Button("Clone");
			cloneButton.setOnAction(e ->
			{
				new NewBPPopupBox(client, application, true);
				NewBPPopupBox.show(businessPlan);
				editOrCloneBPPopupBox.close();
			});
			
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e ->
			{
				editOrCloneBPPopupBox.close();
			});
			
			buttons.getChildren().addAll(copyButton, cloneButton, cancelButton);
			
			//Create a VBox to hold all of the elements in the scene
			VBox informationNode = new VBox(10);
			informationNode.getChildren().addAll(editOrCloneLabel, buttons);
			informationNode.setAlignment(Pos.CENTER);
			
		//Center the elements in the scene
		AnchorPane.setTopAnchor(informationNode, 100.0);
		AnchorPane.setRightAnchor(informationNode, 100.0);
		AnchorPane.setLeftAnchor(informationNode, 100.0);
		AnchorPane.setBottomAnchor(informationNode, 100.0);
		
		rootNode.getChildren().add(informationNode);
				
		Scene scene = new Scene(rootNode);
		editOrCloneBPPopupBox.setScene(scene);
	}
	
	
	public static void setBusinessPlan(BP bp)
	{
		businessPlan = bp;
	}
	
	public static void show(BP bp)
	{
		businessPlan = bp;
		editOrCloneBPPopupBox.show();
	}
	
	@Override
	public Stage getPopupBox()
	{
		return editOrCloneBPPopupBox;
	}

}
