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
import guiFiles.BPApplication;
import guiFiles.screens.HomeScreen;

public class SaveBPPopupBox implements PopupBoxInterface
{

	public static Stage saveBPPopupBox;
	public static BP businessPlan;

	public SaveBPPopupBox(ClientProxy client, BPApplication application)
	{
		// Create the stage to ask the user
		saveBPPopupBox = new Stage();
		saveBPPopupBox.setWidth(500.0);
		saveBPPopupBox.setHeight(350.0);
		saveBPPopupBox.initModality(Modality.APPLICATION_MODAL);
		saveBPPopupBox.setTitle("");
		saveBPPopupBox.setResizable(false);

		// Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();

			Label saveLabel = new Label("Would you like to save the Business Plan locally?");
			Label invalidSaveLabel = new Label("");
	
			// Create an HBox to store the buttons to save, cancel, and to discard changes.
			HBox buttons = new HBox(50);
			
			Button saveButton = new Button("Save");
			saveButton.setOnAction(e -> 
			{
				client.setBusinessPlan(businessPlan);
				client.saveLocalPlanXML();
				application.notify(HomeScreen.homeScreen);
				saveBPPopupBox.close();
			});
	
			Button discardButton = new Button("Discard");
			discardButton.setOnAction(e -> 
			{
				application.notify(HomeScreen.homeScreen);
				saveBPPopupBox.close();
			});
	
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e -> 
			{
				saveBPPopupBox.close();
			});
	
			buttons.getChildren().addAll(saveButton, discardButton, cancelButton);
	
			// Create a VBox to hold all of the elements in the scene
			VBox informationNode = new VBox(10);
			informationNode.getChildren().addAll(saveLabel, buttons);
			informationNode.setAlignment(Pos.CENTER);

		// Center the elements in the scene
		AnchorPane.setTopAnchor(informationNode, 100.0);
		AnchorPane.setRightAnchor(informationNode, 100.0);
		AnchorPane.setLeftAnchor(informationNode, 100.0);
		AnchorPane.setBottomAnchor(informationNode, 100.0);

		rootNode.getChildren().add(informationNode);

		Scene scene = new Scene(rootNode);
		saveBPPopupBox.setScene(scene);
	}
	
	public static void show(BP bp)
	{
		businessPlan = bp;
		saveBPPopupBox.show();
	}

	public void setBusinessPlan(BP bp)
	{
		businessPlan = bp;
	}

	@Override
	public Stage getPopupBox()
	{
		return saveBPPopupBox;
	}

}
