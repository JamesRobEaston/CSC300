package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import clientServerPackage.ServerInterface;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddDepartmentPopupBoxTest extends ApplicationTest
{

	MockModel model;
	ConcreteServer server;
	ClientProxy adminProxy;
	AddDepartmentPopupBoxController cont;
	
	@Override
	public void start(Stage stage) throws Exception
	{
		//Load in scene and show the stage
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AddDepartmentPopupBoxController.class.getResource("AddDepartmentPopupBox.fxml"));
		try
		{
			Scene scene = new Scene(loader.load());
			stage.setTitle("Add Department");
			stage.setResizable(false);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		model = new MockModel(null, null, null);
		cont = loader.getController();
		cont.setModel(model);

		//Setup the registry and the server
		server = new ConcreteServer("admin", "admin");
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("Server", stub);

		} catch (Exception e)
		{
			//registry = LocateRegistry.getRegistry(1099);
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

		adminProxy.setUserToken(adminProxyUserToken);
		model.setClient(adminProxy);
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

		adminProxy.setUserToken(adminProxyUserToken);
		model = new MockModel(adminProxy, null, null);
		cont.setModel(model);
	}
	
	//Test to make sure new departments can be added
	@Test
	public void testAddingValidDepartment()
	{
		reset();
		
		//Check the initial state of the model and ensure it is valid
		ensureServerIsInInitialState();
		
		createDepartment("CSC");
		
		//Make sure the popupbox is closed and the department is added
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
		assertEquals(1, model.getAddDepartmentMethodCallCounter());
		try
		{
			assertEquals(2, server.getAllDepartments().size());
			assertEquals("Admin", server.getAllDepartments().get(0).getDepartmentName());
			assertEquals("CSC", server.getAllDepartments().get(1).getDepartmentName());
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		//Make sure a second, new department can be added
		createDepartment("Math");
		
		assertEquals(2, model.getClosePopupBoxMethodCallCounter());
		assertEquals(2, model.getAddDepartmentMethodCallCounter());
		try
		{
			assertEquals(3, server.getAllDepartments().size());
			assertEquals("Admin", server.getAllDepartments().get(0).getDepartmentName());
			assertEquals("CSC", server.getAllDepartments().get(1).getDepartmentName());
			assertEquals("Math", server.getAllDepartments().get(2).getDepartmentName());
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		//Make sure department names don't need to be just letters
		createDepartment("!@#$%^&*()123456789-_+=~`");
		
		assertEquals(3, model.getClosePopupBoxMethodCallCounter());
		assertEquals(3, model.getAddDepartmentMethodCallCounter());
		try
		{
			assertEquals(4, server.getAllDepartments().size());
			assertEquals("Admin", server.getAllDepartments().get(0).getDepartmentName());
			assertEquals("CSC", server.getAllDepartments().get(1).getDepartmentName());
			assertEquals("Math", server.getAllDepartments().get(2).getDepartmentName());
			assertEquals("!@#$%^&*()123456789-_+=~`", server.getAllDepartments().get(3).getDepartmentName());
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEnteringExistingDepartment()
	{
		reset();
		
		//Check the initial state of the model and ensure it is valid
		ensureServerIsInInitialState();
		
		//Add a new department
		createDepartment("CSC");
		
		//Create the same department and make sure a duplicate wasn't added
		createDepartment("CSC");
		
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
		assertEquals(2, model.getAddDepartmentMethodCallCounter());
		try
		{
			assertEquals(2, server.getAllDepartments().size());
			assertEquals("Admin", server.getAllDepartments().get(0).getDepartmentName());
			assertEquals("CSC", server.getAllDepartments().get(1).getDepartmentName());
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEnteringEmptyDepartment()
	{
		reset();
		
		//Check the initial state of the model and ensure it is valid
		ensureServerIsInInitialState();
		
		createDepartment("");
		
		//Ensure nothing has changed
		ensureServerIsInInitialState();

		createDepartment("                          ");
		
		//Ensure nothing has changed
		ensureServerIsInInitialState();
	}
	
	@Test
	public void testCancelButton()
	{
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		clickOn("#cancelButton");
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
	}
	
	//Helper method that enters text into the department name field and tries to create the department
	private void createDepartment(String text)
	{
		doubleClickOn("#deptNameField");
		write(text);
		clickOn("#createDepartmentButton");
	}
	
	
	private void ensureServerIsInInitialState()
	{
		//Check the initial state of the model and ensure it is valid
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		assertEquals(0, model.getAddDepartmentMethodCallCounter());
		try
		{
			assertEquals(1, server.getAllDepartments().size());
			assertEquals("Admin", server.getAllDepartments().get(0).getDepartmentName());
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

}
