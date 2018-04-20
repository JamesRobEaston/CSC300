package guiFiles.screens;

import clientServerPackage.*;
import guiFiles.BPApplication;
import guiFiles.PopupBoxes.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

public class HomeScreen implements ScreenInterface
{
	
	public static Scene homeScreen;
	
	public HomeScreen(ClientProxy client, BPApplication application)
	{
		AnchorPane rootNode = new AnchorPane();
		
		//Create the logout button
		Button logoutButton = new Button("Log out");
		logoutButton.setOnAction(e -> 
		{
			application.notify(LoginScreen.loginScreen);
		});
		
		//Create a label to display the user's department
		Label departmentLabel = new Label("Department: " + client.getDepartment().getDepartmentName());
		
		//Create the center of the scene
		VBox centerNode = new VBox(10);
		
		Button loadLocalButton = new Button("Load Local Copy");
		loadLocalButton.setOnAction(e -> 
		{
			client.readLocalPlanXML();
			new ViewBPScreen(client.getBusinessPlan(), client, application);
		});
		
		Button viewAllButton = new Button("View All Plans");
		viewAllButton.setOnAction(e -> 
		{
			new ViewAllScreen(client, application);
			application.notify(ViewAllScreen.viewAllScreen);
		});
		
		Button newButton = new Button("New Business Plan");
		newButton.setOnAction(e -> 
		{
			new NewBPPopupBox(client, application, false);
			NewBPPopupBox.newBPPopupBox.show();
		});
		
		centerNode.getChildren().addAll(loadLocalButton, viewAllButton, newButton);
		
		if(client.isAdmin())
		{
			Button addNewUserButton = new Button("Add New User");
			addNewUserButton.setOnAction(e ->
			{
				AddUserPopupBox.addUserPopupBox.show();
			});
			
			centerNode.getChildren().add(addNewUserButton);
		}
		
		if(client.isAdmin())
		{
			Button addNewDepartmentButton = new Button("Add New Department");
			addNewDepartmentButton.setOnAction(e ->
			{
				AddDepartmentPopupBox.addDepartmentPopupBox.show();
			});
			
			centerNode.getChildren().add(addNewDepartmentButton);
		}
		
		centerNode.setAlignment(Pos.CENTER);
		
		//Anchor elements to the correct position on the screen.
		AnchorPane.setTopAnchor(logoutButton, 20.0);
		AnchorPane.setRightAnchor(logoutButton, 20.0);

		AnchorPane.setTopAnchor(centerNode, 100.0);
		AnchorPane.setLeftAnchor(centerNode, 100.0);
		AnchorPane.setRightAnchor(centerNode, 100.0);
		AnchorPane.setBottomAnchor(centerNode, 100.0);	
		
		AnchorPane.setTopAnchor(departmentLabel, 20.0);
		AnchorPane.setLeftAnchor(departmentLabel, 20.0);
		
		rootNode.getChildren().addAll(logoutButton, centerNode, departmentLabel);
		
		homeScreen = new Scene(rootNode);
	}
	
	public Scene getScene()
	{
		return homeScreen;
	}
}
