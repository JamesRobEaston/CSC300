package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.registry.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.rules.ExpectedException;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import clientServerPackage.ServerInterface;
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
		
		server = new ConcreteServer("admin", "admin");
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);
			
			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("Server", stub);
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		model = new MockModel(null, null, null);
		
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
	
	@BeforeEach
	public void resetModel()
	{
		model = new MockModel(null, null, null);
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
		
		
		clickOn("#serverInput");
		clickOn("#serverInput");
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
			users = model.getClient().getStub().getUsers();
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
		
		assertEquals(model.getClient().getUserToken(), null);
	}
	
	//Test logging a user with a valid username but the wrong password
	@Test
	public void testLoginValidNameInvalidPass()
	{
		fillUserInfo("admin", "a", "1099");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient().getUserToken(), null);
	}
	
	//Test logging a user with an invalid name but a valid password
	@Test
	public void testLoginInvalidNameValidPass()
	{
		fillUserInfo("Bradshaw", "admin", "1099");
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient().getUserToken(), null);
	}
	
	@Rule
	public final ExpectedException exception = ExpectedException.none();
	//Test logging a valid user into an invalid server
	@Test
	public void testValidLoginInvalidServer()
	{
		fillUserInfo("admin", "admin", "1");
		//exception.expect(ConnectException.class);
		clickOn("#loginButton");
		assertEquals(model.getClient(), null);
	}
	
	//Test logging an invalid user into an invalid server
	public void testInvalidLoginInvalidServer()
	{
		fillUserInfo("Bradshaw", "Bradshaw", "1");
		//exception.expect(ConnectException.class);
		clickOn("#loginButton");
		//WaitForAsyncUtils.waitForFxEvents();
		
		assertEquals(model.getClient(), null);
	}

}
