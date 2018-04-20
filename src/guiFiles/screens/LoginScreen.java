package guiFiles.screens;

import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;

import java.rmi.RemoteException;
import java.rmi.registry.*;

import clientServerPackage.*;
import guiFiles.BPApplication;

public class LoginScreen implements ScreenInterface
{
	//The actual Scene that is the login screen.
	public static Scene loginScreen;
	
	//The client created after logging in.
	public ClientProxy client;
	
	//Constructs the loginScreen
	public LoginScreen(BPApplication application)
	{
		AnchorPane rootNode = new AnchorPane();
		
		//Create the box in the top-left corner of the screen that allows the user to enter the server IP.
		VBox serverNode = new VBox(10);
		serverNode.setAlignment(Pos.CENTER);
		
		Label hostLabel = new Label("Server:");
		hostLabel.setAlignment(Pos.CENTER);
		TextField serverTextInput = new TextField("1099");
		//serverTextInput.setAlignment(Pos.CENTER);
		
		serverNode.getChildren().addAll(hostLabel, serverTextInput);
		
		//Create the Login area in the center of the screen.
		VBox loginNode = new VBox(10);
		loginNode.setAlignment(Pos.CENTER);
		
			//Create an HBox to store the user's username information
			HBox usernameNode = new HBox(10); 
			Label userLabel = new Label("Username:");
			TextField usernameInput = new TextField();
			usernameNode.getChildren().addAll(userLabel, usernameInput);
			
			//Create an HBox to store the user's password information
			HBox passwordNode = new HBox(13); 
			Label passwordLabel = new Label("Password:");
			PasswordField passwordInput = new PasswordField();
			passwordNode.getChildren().addAll(passwordLabel, passwordInput);
			
			//Create a VBox to store the previous two nodes
			VBox informationNode = new VBox(10);
			informationNode.getChildren().addAll(usernameNode, passwordNode);
			
			//Create an empty label to be used if the login fails
			Label invalidLoginLabel = new Label();
			
			//Create the Login button
			Button loginButton = new Button("Login");
			loginButton.setOnAction(e -> 
			{
				ServerInterface server = new ConcreteServer();
				
				boolean foundServer = true;
				int serverAddress = Integer.parseInt(serverTextInput.getText());
				try 
				{
					Registry registry = LocateRegistry.getRegistry(serverAddress);
					server = (ServerInterface) registry.lookup("Server");
				} 
				catch (Exception exception)
				{
					foundServer = false;
					invalidLoginLabel.setText("The server was not found.");
					exception.printStackTrace();
				}
				
				if(foundServer)
				{
					try
					{
						client = new ClientProxy(server);
						client.login(usernameInput.getText(), passwordInput.getText());
					}
					catch (Exception e1)
					{
						e1.printStackTrace();
					}
				
					//If the login failed, notify the user.
					if(client.getUserToken() == null)
					{
						invalidLoginLabel.setText("The username or password was not valid! Please try again.");
						invalidLoginLabel.setAlignment(Pos.CENTER);
						
					}
					//Otherwise, create all screens and show the home screen on the application.
					else
					{
						application.createAllStaticScreensAndPopupBoxes(client);
						application.notify(HomeScreen.homeScreen);
						usernameInput.setText("");
						passwordInput.setText("");
					}
				
				}
				
			});
			
		usernameNode.setAlignment(Pos.CENTER);
		passwordNode.setAlignment(Pos.CENTER);
		invalidLoginLabel.setAlignment(Pos.CENTER);
		loginButton.setAlignment(Pos.CENTER);
		loginNode.getChildren().addAll(informationNode, invalidLoginLabel, loginButton);
		
		//Anchor the nodes to their correct positions on the screen.
		rootNode.getChildren().addAll(serverNode, loginNode);
		
		AnchorPane.setTopAnchor(serverNode, 20.0);
		AnchorPane.setLeftAnchor(serverNode, 20.0);
		
		AnchorPane.setLeftAnchor(loginNode, 100.0);
		AnchorPane.setTopAnchor(loginNode, 100.0);
		AnchorPane.setRightAnchor(loginNode, 100.0);
		AnchorPane.setBottomAnchor(loginNode, 100.0);
		
		loginScreen = new Scene(rootNode);
	}
	
	public Scene getScene()
	{
		return loginScreen;
	}
}
