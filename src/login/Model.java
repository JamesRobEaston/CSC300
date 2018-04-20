package login;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteServer;
import clientServerPackage.ServerInterface;
import guiFiles.screens.HomeScreen;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Model 
{
	public ClientProxy client;
	
	
	public boolean authenticate(TextField userName_input,PasswordField pass_input,TextField serv_input, Button login_but )
	{
		ServerInterface server = new ConcreteServer();
		Label invalidLoginLabel = new Label();
		
		boolean foundServer = true;
		int serverAddress = Integer.parseInt(serv_input.getText());
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
				client.login(userName_input.getText(), pass_input.getText());
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		
			//If the login failed, notify the user.
			if(client.getUserToken() == null)
			{
				return false;
				//invalidLoginLabel.setText("The username or password was not valid! Please try again.");
				//invalidLoginLabel.setAlignment(Pos.CENTER);
				
				
			}
			//Otherwise, create all screens and show the home screen on the application.
			else
			{
				//application.createAllStaticScreensAndPopupBoxes(client);
				//application.notify(HomeScreen.homeScreen);
				return true;
			//	userName_input.setText("");
				//pass_input.setText("");
			}
		
		}
		return false;
		
	}

}
