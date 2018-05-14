package sprint5Tests;

import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

import applicationFiles.BPApplication;
import businessPlanClasses.PlanDesign;
import clientServerPackage.BP;
import clientServerPackage.ConcreteServer;
import clientServerPackage.ServerInterface;
import javafx.stage.Stage;
import model.Model;
import model.ModelInterface;

public class PartOneTest extends ApplicationTest
{

	Stage stage;
	ModelInterface model;
	ConcreteServer server;
	
	@Override
	public void start(Stage primaryStage)
	{
		this.stage = primaryStage;
		server = new ConcreteServer("admin", "admin");
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("Server", stub);

		} catch (Exception e)
		{
		}
		
		BPApplication application = new BPApplication();
		application.primaryStage = stage;
		model = new Model(null, application, null);
		model.showLogin();
	}
	
	@Test
	public void test()
	{
		//Log the user in
		clickOn("#usernameInput");
		write("admin");
		clickOn("#passInput");
		write("admin");
		clickOn("#loginButton");
		
		try
		{
			server.addUser(model.getClient().getUserToken(), "a", "a", "Admin", true);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Login a separate client so that they can change and save the BP that our test is viewing
		Model modelOnSeparateClientSide = new Model(null, null, null);
		TextField userName = new TextField();
		userName.setText("a");
		PasswordField pass = new PasswordField();
		pass.setText("a");
		TextField server = new TextField();
		server.setText("1099");
		modelOnSeparateClientSide.authenticate(userName, pass, server, null);
		
		//Create a new BP
		clickOn("#newBP_but");
		sleep(100);
		clickOn("#planIDField");
		write("Test");
		clickOn("#planYearField");
		write("2018");
		clickOn("#departmentChoiceBox");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createPlanButton");

		clickOn("#centreButton");
		clickOn("#submitButton");
		
		//Get the business plan on a separate client and ensure it was retrieved properly
		modelOnSeparateClientSide.retrieve("Test 2018");
		assertNotNull(modelOnSeparateClientSide.getBusinessPlan());
		
		sleep(1000);
		try
		{
			modelOnSeparateClientSide.client.saveToAdminDepartment();
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		sleep(1000);
	}
}
