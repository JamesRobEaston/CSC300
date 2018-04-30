package tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import addUserPopup.AddNewUserPopupBoxController;
import applicationFiles.DepartmentConverter;
import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteClient;
import javafx.application.Platform;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import clientServerPackage.ServerInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;

public class AddNewUserTest extends ApplicationTest
{

	AddNewUserPopupBoxController anuc;
	MockModel model;	
	Stage  popupStage;
	ConcreteClient admin;
	ConcreteServer server;
	ClientProxy adminProxy;
	
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
		}
		
		model = new MockModel(null, null, null);
		
		
		
			
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AddNewUserPopupBoxController.class.getResource("/addUserPopup/AddUserPopupBox.fxml"));
		Scene newUserScene = new Scene(new AnchorPane());
		
		
		try
		{
			newUserScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		anuc = loader.getController();
		anuc.setModel(model);
		reset();
		
		ConcreteClient admin = new ConcreteClient();
		try
		{
			admin = server.getUsers().get(0);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String adminProxyUserToken = server.genUserToken();
		admin.userToken = adminProxyUserToken;

		try
		{
			adminProxy = new ClientProxy((ServerInterface) LocateRegistry.getRegistry(1099).lookup("Server"));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		adminProxy.setUserToken(adminProxyUserToken);	
		model.setClient(adminProxy);
		

		ArrayList<Department> departments = model.getAllDepartments();
		anuc.newDepartment.setConverter(new DepartmentConverter<Department>());

		for (Department department : departments)
		{
			if (!department.getName().equals("Admin"))
			{
				anuc.newDepartment.getItems().add(department);
			} else
			{
				model.setAdminDepartment(department);
			}
		}

		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Add New User");
		popupStage.setResizable(false);
		popupStage.setScene(newUserScene);
		popupStage.show();
		
		
		
			
	
	}
	
	
	public void reset()
	{
		server = new ConcreteServer("admin", "admin");
		
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.getRegistry(1099);
			registry.rebind("Server", stub);
			

		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Create a ClientProxy to give to the model
		ConcreteClient admin = new ConcreteClient();
		try
		{
			admin = server.getUsers().get(0);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String adminProxyUserToken = server.genUserToken();
		admin.userToken = adminProxyUserToken;

		
		try
		{
			adminProxy = new ClientProxy((ServerInterface) LocateRegistry.getRegistry(1099).lookup("Server"));
		} catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		model = new MockModel(adminProxy, null, null);
		adminProxy.setUserToken(adminProxyUserToken);
		anuc.setModel(model);
		Model.currDepartment = adminProxy.getDepartment();	
		try
		{
			server.addDepartment(adminProxyUserToken, "Chemistry");
		} catch (RemoteException e)
		{
			fail("Did not add Department 'Chemistry' correctly.");
		}
		

	}
	
	public void close()
	{
		Platform.runLater(() ->
		{
			popupStage.close();
		});
		
		sleep(100);
	}	

	
	public void testCreate()
	{

        //check if addUser method is get called by clickng the "create" button
		
		assertEquals(model.addUserMethodCallCounter,0);
		clickOn("#createUserButton");
		assertEquals(model.addUserMethodCallCounter,1);
		model.addUserMethodCallCounter=0;
		//check if the new user is truly added to the server
		
		
		for (int i=0;i<server.users.size();i++)
		{
			assertEquals(server.users.get(server.users.size()-1).username,"haha");
			assertEquals(server.users.get(server.users.size()-1).password,"haha");
			assertEquals(server.users.get(server.users.size()-1).isAdmin,false);
		}
		//assertEquals(adminProxy .addUser("haha", "haha", "chemistry", false),true);
		
	}
	
	@Test
	public void testCancel()
	{
		reset();
		clickOn("#newUserCancel");
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
		close();
	}
	
	@Test
	public void testCreatingExistingUser() throws RemoteException
	{
		//Create a user
		reset();
		clickOn("#newUsername");
		write("NewUser");
		clickOn("#newPassword");
		write("NewPass");
		clickOn("#newDepartment");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createUserButton");
		
		//Test that the user was created
		ArrayList<ConcreteClient> users = server.getUsers();
		assertEquals(2, users.size());
		assertEquals(users.get(1).getUsername(), "NewUser");
		assertEquals(users.get(1).getPassword(), "NewPass");
		assertFalse(users.get(1).isAdmin());
		
		//Create a user with the same name
		doubleClickOn("#newUsername");
		write("NewUser");
		doubleClickOn("#newPassword");
		write("DifferentPass");
		clickOn("#newDepartment");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createUserButton");
		
		//Test to make sure the already existing user hasn't changed and no new users were added.
		users = server.getUsers();
		assertEquals(2, users.size());
		assertEquals(users.get(1).getUsername(), "NewUser");
		assertEquals(users.get(1).getPassword(), "NewPass");
		assertFalse(users.get(1).isAdmin());
		close();
	}
	
	
	
	@Test
	public void testAddNewUser() throws RemoteException
	{
		//create a user	
		reset();
		clickOn("#newUsername");
		write("haha");
		clickOn("#newPassword");
		write("haha");
		clickOn("#newDepartment");
	    type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		
		//testing
        testCreate();
        
        //clear the text fields
        doubleClickOn("#newUsername");
        type(KeyCode.DELETE);      
        doubleClickOn("#newPassword");
        type(KeyCode.DELETE);
        
        
        //create an admin	
        reset();
        model.setAdminDepartment(adminProxy.getDepartment());
    	clickOn("#newUsername");
		write("yoyo");
		clickOn("#newPassword");
		write("yoyo");
		clickOn("#newIsAdmin");

		//testing 
		assertEquals(model.addUserMethodCallCounter,0);
		clickOn("#createUserButton");
		assertEquals(model.addUserMethodCallCounter,1);
		for (int i=0;i<server.users.size();i++)
		{
			assertEquals(server.users.get(server.users.size()-1).username,"yoyo");
			assertEquals(server.users.get(server.users.size()-1).password,"yoyo");
			assertEquals(server.users.get(server.users.size()-1).isAdmin,true);
		}

		close();
	}
	
	

}
