package applicationFiles;

import java.io.IOException;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import clientServerPackage.*;
import homePage.homePageController;
import javafx.application.*;
import javafx.fxml.FXMLLoader;
import javafx.stage.*;
import login.loginController;
import model.Model;
import model.ModelInterface;
import javafx.scene.*;

public class BPApplication extends Application
{
	//The stage that will act as the primary window in the application.
	public Stage primaryStage;
	
	@Override
	public void start(Stage stage)
	{
		primaryStage = stage;
		primaryStage.setTitle("Business Plans Galore!");
		primaryStage.setHeight(700);
		primaryStage.setWidth(700);
		
		//LoginScreen loginScreen = new LoginScreen(this);
		
		ModelInterface model = new Model(null, this, null);
		model.showLogin();
		
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
	
	public static void main(String[] args)
	{

		launch();
		
	}
	
	public void launchApp()
	{
		launch();
	}
	
	public void closeApp()
	{
		primaryStage.close();
	}
}
