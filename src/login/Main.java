package login;


import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application 
{
	
	static Model model = new Model();
	static Stage stage;
    static AnchorPane login_view;
    Scene login_scene;
    static AnchorPane homePage_view;
    static Scene home_scene;
	
	@Override
	public void start(Stage primaryStage) throws Exception 
	{
		this.stage=primaryStage;
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("LoginView.fxml"));
		login_view= loader.load();
		loginController lcont=loader.getController();
		lcont.setModel(model);
		login_scene=new Scene(login_view);
		stage.setScene(login_scene);
		stage.show();

	}
	
	public static void showHomePage()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("login/HomePageView.fxml"));
		try 
		{
			homePage_view=loader.load();
			homePageController hcont = loader.getController();
			hcont.setModel(model);
		    home_scene=new Scene(login_view);
			stage.setScene(home_scene);
			stage.show();
			}
      catch (IOException e) 
		{
			
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) 
	{
		launch(args);

	}
}
