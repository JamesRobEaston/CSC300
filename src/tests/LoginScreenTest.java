package tests;

import static org.junit.Assert.assertEquals;

import java.rmi.registry.*;
import java.util.ArrayList;

import org.junit.gen5.api.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.loginController;

public class LoginScreenTest extends ApplicationTest
{
	
	loginController loginCont;
	ConcreteServer server;
	MockModel model;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{

		MockModel model = new MockModel(null, null, null);
		Registry registry = LocateRegistry.getRegistry(1099);
		server = (ConcreteServer) registry.lookup("Server");
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loginController.class.getResource("/login/LoginView.fxml"));
		try 
		{
			Scene loginScreen = new Scene(loader.load());
			loginCont = loader.getController();
			loginCont.setModel(model);
			primaryStage.setScene(loginScreen);
			primaryStage.show();
		}
		catch (Exception e) 
		{
			
			e.printStackTrace();
		}
		
	}
	
	//Helper method to enter a user's information
	public void fillUserInfo(String username, String password, String server)
	{
		clickOn("#usernameInput");
		WaitForAsyncUtils.waitForFxEvents();
		write(username);
		WaitForAsyncUtils.waitForFxEvents();
		
		clickOn("#passInput");
		WaitForAsyncUtils.waitForFxEvents();
		write(password);
		WaitForAsyncUtils.waitForFxEvents();
		
		clickOn("serverInput");
		WaitForAsyncUtils.waitForFxEvents();
		write(server);
		WaitForAsyncUtils.waitForFxEvents();
	}
	
	//Test logging a valid user into a valid server
	public void testLoginValidUser()
	{
		fillUserInfo("admin", "admin", "1099");
		clickOn("#loginButton");
		WaitForAsyncUtils.waitForFxEvents();
		
		String modelUserToken = model.getClient().getUserToken();
		
		ConcreteClient admin = null;
		ArrayList<ConcreteClient> users = server.getUsers();
		int index = 0;
		while((admin == null) && (index < users.size()))
		{
			ConcreteClient currUser = users.get(index);
			if(currUser.getUsername().equals("admin"))
			{
				admin = currUser;
			}
			index++;
		}
		
		String adminToken = admin.getUserToken();
		assertEquals(modelUserToken,adminToken);
		assertEquals(model.showHomeMethodCallCounter, 1);
	}
	
	//Test logging an invalid user into a valid server
	public void testLoginInvalidUser()
	{
		fillUserInfo("Bradshaw", "Bradshaw", "1099");
		clickOn("#loginButton");
		WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
	
	//Test logging a user with a valid username but the wrong password
	public void testLoginValidNameInvalidPass()
	{
		fillUserInfo("admin", "a", "1099");
		clickOn("#loginButton");
		WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
	
	//Test logging a user with an invalid name but a valid password
	public void testLoginInvalidNameValidPass()
	{
		fillUserInfo("Bradshaw", "admin", "1099");
		clickOn("#loginButton");
		WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
	
	//Test logging a valid user into an invalid server
	public void testValidLoginInvalidServer()
	{
		fillUserInfo("admin", "admin", "1");
		clickOn("#loginButton");
		assertEquals(model.getClient(), null);
	}
	
	//Test logging an invalid user into an invalid server
	public void testInvalidLoginInvalidServer()
	{
		fillUserInfo("Bradshaw", "Bradshaw", "1");
		clickOn("#loginButton");
		WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}

	@Test
	public void test()
	{
		testLoginValidUser();
		testLoginInvalidUser();
		testLoginValidNameInvalidPass();
		testLoginInvalidNameValidPass();
		testValidLoginInvalidServer();
		testInvalidLoginInvalidServer();
	}

}
