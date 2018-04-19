package applicationFiles;

import clientServerPackage.*;
import javafx.application.*;
import javafx.stage.*;
import javafx.scene.*;

public class BPApplication extends Application
{
	//The stage that will act as the primary window in the application.
	Stage primaryStage;
	
	@Override
	public void start(Stage stage)
	{
		primaryStage = stage;
		primaryStage.setTitle("Business Plans Galore!");
		primaryStage.setHeight(700);
		primaryStage.setWidth(700);
		
		//LoginScreen loginScreen = new LoginScreen(this);
		
		//setScene(loginScreen.getScene());
		primaryStage.show();
	}
	
	//Displays the scene given.
	public void setScene(Scene scene)
	{
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	//Observer pattern. The application is notified by scenes whenever the scene needs to change and, whenever
	//it is notified, it uses the push method to receive the new scene and display it.
	public void notify(Scene scene)
	{
		this.setScene(scene);
	}
	
	//Creates all of the classes with a static scene or window.
	//This method is put here to encapsulate the creation of these objects.
	public void createAllStaticScreensAndPopupBoxes(ClientProxy client)
	{
		/*new HomeScreen(client, this);
		new AddUserPopupBox(client, this);
		new AddDepartmentPopupBox(client, this);
		new EditOrCloneBPPopupBox(client, this);
		new SaveBPPopupBox(client, this);
		new ViewAllScreen(client, this);
		new categoriesPopupBox(client, this);
		*/
	}
	
	public void launch()
	{
		super.launch();
	}
}
