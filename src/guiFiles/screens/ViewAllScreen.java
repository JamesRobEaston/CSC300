package guiFiles.screens;

import clientServerPackage.*;
import businessPlanClasses.*;
import guiFiles.BPApplication;
import guiFiles.PopupBoxes.*;
import guiFiles.DepartmentConverter;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.*;

import javafx.scene.paint.*;
import java.util.*;

public class ViewAllScreen implements ScreenInterface{

	public static Scene viewAllScreen;
	
	//The node that displays the business plans. This needs to be global so it can be updated dynamically.
	private ScrollPane viewAllBPNode;
	
	String[][] businessPlans;
	ArrayList<String[]> validPlans;
	Button resetButton;
	
	public static Department currDepartment;
	
	public ViewAllScreen(ClientProxy client, BPApplication application)
	{
		currDepartment = client.getDepartment();
		
		//Copy allPlans into a new list of plans that will be displayed
		updateValidPlans(client, client.getDepartment());
		
		//Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();
		
			HBox topLeftCorner = new HBox(20);
			
			//Create a back button
			Button backButton = new Button("Back");
			backButton.setOnAction(e -> 
			{
				application.notify(HomeScreen.homeScreen);
				resetButton.fire();
			});
			
			topLeftCorner.getChildren().add(backButton);
			
			if(client.isAdmin())
			{
				ChoiceBox<Department> departmentDropDownMenu = new ChoiceBox<Department>();
				ArrayList<Department> departments = client.getAllDepartments();
				for(int i = 0; i < departments.size(); i++)
				{
					departmentDropDownMenu.getItems().add(departments.get(i));
				}
				departmentDropDownMenu.setConverter(new DepartmentConverter<Department>());
				
				departmentDropDownMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) ->
				{
					currDepartment = departmentDropDownMenu.getItems().get(newIndex.intValue());
					updateValidPlans(client, currDepartment);
					updateViewAllBPNode(client, validPlans);
				});
				
				departmentDropDownMenu.setValue(ConcreteServer.adminDepartment);
				
				topLeftCorner.getChildren().add(departmentDropDownMenu);
			}
			
			//Create a node to hold all of the search functionality in the top-right corner
			VBox searchNode = new VBox(10);
			
			Label yearSearchLabel = new Label("Search by year:");
			Label nameSearchLabel = new Label("Search by ID:");
			
			VBox labelNode = new VBox(10);
			labelNode.getChildren().addAll(yearSearchLabel, nameSearchLabel);
			
			TextField yearInput = new TextField();
			TextField nameInput = new TextField();
			
			VBox searchInputNode = new VBox(10);
			searchInputNode.getChildren().addAll(yearInput, nameInput);
			
			HBox inputNode = new HBox(10);
			inputNode.getChildren().addAll(labelNode, searchInputNode);
			
			Button goButton = new Button("Go!");
			goButton.setOnAction(e ->
			{
				//Search by year
				String year = yearInput.getText();
				if(year != "");
				{
					String[] bp;
					for(int i = 0; i < validPlans.size(); i++)
					{
						bp = validPlans.get(i);
						if(!bp[2].contains(year))
						{
							validPlans.remove(bp);
						}
						
					}
				}
				
				//Search by name
				String name = nameInput.getText();
				if(name != "");
				{
					String[] bp;
					for(int i = 0; i < validPlans.size(); i++)
					{
						bp = validPlans.get(i);
						if(!bp[0].contains(name))
						{
							validPlans.remove(bp);
						}
						
					}
				}
				
				updateViewAllBPNode(client, validPlans);
			});
			goButton.setAlignment(Pos.CENTER_RIGHT);
			
			//Create a button to undo the search
			resetButton = new Button("Reset");
			resetButton.setOnAction(e ->
			{
				validPlans.clear();
				
				for(String[] bp : businessPlans)
				{
					validPlans.add(bp);
				}
				
				yearInput.setText("");
				nameInput.setText("");
				
				updateViewAllBPNode(client, validPlans);
			});
			resetButton.setAlignment(Pos.CENTER_RIGHT);
			
			Button newButton = new Button("New");
			newButton.setOnAction(e -> 
			{
				new NewBPPopupBox(client, application, false);
				NewBPPopupBox.newBPPopupBox.show();
			});
			
			searchNode.getChildren().addAll(inputNode, goButton, resetButton, newButton);
			searchNode.setAlignment(Pos.CENTER_RIGHT);
			
			//Create the node to view all of the Business Plans
			viewAllBPNode = new ScrollPane();
			updateViewAllBPNode(client, validPlans);
			
		//Anchor all elements appropriately
		AnchorPane.setTopAnchor(topLeftCorner, 20.0);
		AnchorPane.setLeftAnchor(topLeftCorner, 20.0);
		
		AnchorPane.setTopAnchor(searchNode, 20.0);
		AnchorPane.setRightAnchor(searchNode, 20.0);

		AnchorPane.setTopAnchor(viewAllBPNode, 100.0);
		AnchorPane.setLeftAnchor(viewAllBPNode, 100.0);
		AnchorPane.setRightAnchor(viewAllBPNode, 100.0);
		AnchorPane.setBottomAnchor(viewAllBPNode, 100.0);
		
		rootNode.getChildren().addAll(topLeftCorner, searchNode, viewAllBPNode);
		
		viewAllScreen = new Scene(rootNode);
	}
	
	void updateViewAllBPNode(ClientProxy client, ArrayList<String[]> validPlans)
	{
		viewAllBPNode.setContent(null);
		FlowPane rootNode = new FlowPane();
		for(String[] bp : validPlans)
		{	
			//Create the Button
			Button bpButton = new Button(bp[0]);
			bpButton.setOnAction(e ->
			{
				if(client.isAdmin())
				{
					client.retrieve(bp[0], currDepartment);
				}
				else
				{
					client.retrieve(bp[0]);
				}
				EditOrCloneBPPopupBox.show(client.getLocalCopy());
			});

			StackPane buttonMargins = new StackPane(bpButton);
			StackPane.setMargin(bpButton, new Insets(10, 10, 10, 10));
			rootNode.getChildren().add(buttonMargins);
			viewAllBPNode.setContent(rootNode);
		}
	}
	
	void updateValidPlans(ClientProxy client, Department dept)
	{
		validPlans = new ArrayList<String[]>();
		String[][] bps = new String[0][0];
		
		if(!client.isAdmin())
		{
			//Get all of the Business Plans
			bps = client.view();
		}
		else
		{
			bps = client.view(dept);
			if(bps == null)
			{
				bps = new String[0][0];
			}
		}

		for(String[] bp : bps)
		{
			validPlans.add(bp);
		}
		
		businessPlans = bps;
		
	}
	
	@Override
	public Scene getScene()
	{
		return viewAllScreen;
	}

}
