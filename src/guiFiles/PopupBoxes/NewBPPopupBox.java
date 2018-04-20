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
import guiFiles.screens.*;
import businessPlanClasses.*;
import guiFiles.BPApplication;

public class NewBPPopupBox implements PopupBoxInterface
{
	public static Stage newBPPopupBox;
	public static BP businessPlan;
	
	public NewBPPopupBox(ClientProxy client, BPApplication application, boolean isClone)
	{
		//Create the stage to ask the user
		newBPPopupBox = new Stage();
		newBPPopupBox.setWidth(500.0);
		newBPPopupBox.setHeight(350.0);
		newBPPopupBox.initModality(Modality.APPLICATION_MODAL);
		newBPPopupBox.setTitle("Create New Business Plan");
		newBPPopupBox.setResizable(false);
				
		//Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();
		
			//Create input to ask the user for the ID of the BP
			Label bpIDLabel = new Label("Input the ID of the business plan:");
			TextField bpIDInput = new TextField();
			
			//Create input to ask the user for the year of the new BP
			Label bpYearLabel = new Label("Input the year of the business plan:");
			TextField bpYearInput = new TextField();
			
			//Create a label to notify the user if the ID has already been taken
			Label invalidIDLabel = new Label("");
			
			//Create buttons to create the new BP or to cancel
			Button createBPButton = new Button("Create Business Plan");
			createBPButton.setOnAction(e ->
			{
				String id = bpIDInput.getText();
				String year = bpYearInput.getText();
				
				if(id.replaceAll("\\s+","").equals("")) 
				{
					invalidIDLabel.setText("Please enter a valid ID.");
					bpIDInput.setText("");
				}
				else if(year.replaceAll("\\s+","").equals("")) 
				{
					invalidIDLabel.setText("Please enter a valid year.");
					bpYearInput.setText("");
				}
				else 
				{
					client.retrieve(id + " " + year);
					if(client.getLocalCopy() == null && isInt(year))
					{
						if(!isClone)
						{
							categoriesPopupBox.show(new BP(year, id));
						}
						else
						{
							BP newBP = businessPlan.copy();
							newBP.setID(id + " " +year);
							newBP.setYear(year);
							new ViewBPScreen(newBP, client, application);
						}
						newBPPopupBox.close();
						bpIDInput.setText("");
						bpYearInput.setText("");
					}
					else if(!isInt(year))
					{
						invalidIDLabel.setText("Please enter a valid year.");
					}
					else
					{
						invalidIDLabel.setText("A Business Plan already has that name and year.");
					}

				}
			});
			
			Button cancelButton = new Button("Cancel");
			cancelButton.setOnAction(e ->
			{
				bpIDInput.setText("");
				bpYearInput.setText("");
				newBPPopupBox.close();
			});
			
			HBox buttons = new HBox(50);
			buttons.getChildren().addAll(createBPButton, cancelButton);
			
			//Put all of the user controls into a VBox
			VBox informationNode = new VBox(10);
			informationNode.getChildren().addAll(bpIDLabel, bpIDInput, bpYearLabel, bpYearInput, invalidIDLabel, buttons);
			informationNode.setAlignment(Pos.CENTER);
			
		//Center the elements in the scene
		AnchorPane.setTopAnchor(informationNode, 100.0);
		AnchorPane.setRightAnchor(informationNode, 100.0);
		AnchorPane.setLeftAnchor(informationNode, 100.0);
		AnchorPane.setBottomAnchor(informationNode, 100.0);
		
		rootNode.getChildren().add(informationNode);
				
		Scene scene = new Scene(rootNode);
		newBPPopupBox.setScene(scene);
		
	}
	
	public static void setBusinessPlan(BP bp)
	{
		businessPlan = bp;
	}
	
	public static void show(BP bp)
	{
		businessPlan = bp;
		newBPPopupBox.show();
	}

	@Override
	public Stage getPopupBox()
	{
		return newBPPopupBox;
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
