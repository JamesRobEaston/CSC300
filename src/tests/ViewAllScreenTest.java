package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import viewAllBPView.ViewAllBPScreenController;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import clientServerPackage.ServerInterface;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class ViewAllScreenTest extends ApplicationTest
{

	ViewAllBPScreenController cont;
	ConcreteServer server;
	MockModel model;
	ClientProxy adminProxy;

	@Override
	public void start(Stage stage)
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
		loader.setLocation(ViewAllBPScreenController.class.getResource("/viewAllBPView/ViewAllBPScreen.fxml"));
		try
		{
			Scene screen = new Scene(loader.load());
			cont = loader.getController();
			cont.setModel(model);
			stage.setScene(screen);
			stage.show();
		} catch (Exception e)
		{

			e.printStackTrace();
		}

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
		cont.setModel(model);
		ViewAllBPScreenController.currDepartment = adminProxy.getDepartment();
		cont.setValidPlans(adminProxy, ConcreteServer.adminDepartment);
	}
	
	@Test
	public void testGoBackButton()
	{
		reset();
		assertEquals(0, model.getShowHomeMethodCallCounter());
		clickOn("#backButton");
		assertEquals(1, model.getShowHomeMethodCallCounter());
	}

	@Test
	public void testNewBPButton()
	{
		reset();
		assertEquals(0, model.getShowNewBPPopupBoxMethodCallCounter());
		clickOn("#newBPButton");
		assertEquals(1, model.getShowNewBPPopupBoxMethodCallCounter());
	}

	@Test
	public void testResetBPsButton()
	{
		reset();
		Department currDepartment = ViewAllBPScreenController.currDepartment;
		clickOn("#resetButton");
		int numOfBPsInDept = currDepartment.getPlans().size();
		int numOfBPsDisplayed = cont.bpFlowPane.getChildren().size();
		assertEquals(numOfBPsInDept, numOfBPsDisplayed);
	}

	@Test
	public void testChoiceBox()
	{
		reset();
		
		//Add some test departments to the server
		try
		{
			server.addDepartment(adminProxy.getUserToken(), "Test1");
			server.addDepartment(adminProxy.getUserToken(), "Test2");
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		cont.updateDepartmentDropDownMenu();
		
		//Test to make sure the departments are there
		
		//Admin
		clickOn("#departmentDropDownMenu");
		
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		
		Department currDepartment = cont.departmentDropDownMenu.getSelectionModel().getSelectedItem();
		assertEquals("Admin", currDepartment.getName());
		
		//Test1
		clickOn("#departmentDropDownMenu");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		
		currDepartment = cont.departmentDropDownMenu.getSelectionModel().getSelectedItem();
		assertEquals("Test1", currDepartment.getName());
		
		//Test 2
		clickOn("#departmentDropDownMenu");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		
		currDepartment = cont.departmentDropDownMenu.getSelectionModel().getSelectedItem();
		assertEquals("Test2", currDepartment.getName());
		
		//Test 3
		try
		{
			server.addDepartment(adminProxy.getUserToken(), "Test3");
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		cont.updateDepartmentDropDownMenu();
		clickOn("#departmentDropDownMenu");
		type(KeyCode.DOWN);
		type(KeyCode.DOWN);
		type(KeyCode.DOWN);
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		
		currDepartment = cont.departmentDropDownMenu.getSelectionModel().getSelectedItem();
		assertEquals("Test3", currDepartment.getName());
	}

	@Test
	public void testChoosingBP()
	{
		reset();
		cont.updateDepartmentDropDownMenu();
		
		//Create a new BP and test that it is displayed
		String adminDepartmentName = adminProxy.getDepartment().getDepartmentName();
		BP bp = new BP("2018", "Test BP", adminDepartmentName);
		model.saveBPToDepartment(bp, adminDepartmentName);
		cont.setValidPlans(adminProxy, adminProxy.getDepartment());
	}

}
