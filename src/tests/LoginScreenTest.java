package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.util.ArrayList;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import clientServerPackage.ConcreteClient;
import clientServerPackage.ServerInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import login.loginController;

public class LoginScreenTest extends ApplicationTest
{
	
	loginController loginCont;
	ServerInterface server;
	MockModel model;
	
	@Override
	public void start(Stage primaryStage) throws Exception
	{

		MockModel model = new MockModel(null, null, null);
		Registry registry = LocateRegistry.getRegistry(1099);
		server = (ServerInterface) registry.lookup("Server");
		
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
		//WaitForAsyncUtils.waitForFxEvents();
		write(username);
		//WaitForAsyncUtils.waitForFxEvents();
		
		clickOn("#passInput");
		//WaitForAsyncUtils.waitForFxEvents();
		write(password);
		//WaitForAsyncUtils.waitForFxEvents();
		
		clickOn("serverInput");
		//WaitForAsyncUtils.waitForFxEvents();
		write(server);
		//WaitForAsyncUtils.waitForFxEvents();
	}
	
	//Test logging a valid user into a valid server
	@Test
	public void testLoginValidUser()
	{
		fillUserInfo("admin", "admin", "1099");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		String modelUserToken = model.getClient().getUserToken();
		
		ConcreteClient admin = null;
		ArrayList<ConcreteClient> users = new ArrayList<ConcreteClient>();
		try
		{
			users = server.getUsers();
		} catch (Exception e)
		{
			fail("this is bad");
		}
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
	@Test
	public void testLoginInvalidUser()
	{
		fillUserInfo("Bradshaw", "Bradshaw", "1099");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
	
	//Test logging a user with a valid username but the wrong password
	@Test
	public void testLoginValidNameInvalidPass()
	{
		fillUserInfo("admin", "a", "1099");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
	
	//Test logging a user with an invalid name but a valid password
	@Test
	public void testLoginInvalidNameValidPass()
	{
		fillUserInfo("Bradshaw", "admin", "1099");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
	
	//Test logging a valid user into an invalid server
	@Test
	public void testValidLoginInvalidServer()
	{
		fillUserInfo("admin", "admin", "1");
		clickOn("#loginButton");
		assertEquals(model.getClient(), null);
	}
	
	//Test logging an invalid user into an invalid server
	@Test
	public void testInvalidLoginInvalidServer()
	{
		fillUserInfo("Bradshaw", "Bradshaw", "1");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}
/*
	@Test
	public void testAllTests()
	{
		testLoginValidUser();
		testLoginInvalidUser();
		testLoginValidNameInvalidPass();
		testLoginInvalidNameValidPass();
		testValidLoginInvalidServer();
		testInvalidLoginInvalidServer();
	}
*/
}
