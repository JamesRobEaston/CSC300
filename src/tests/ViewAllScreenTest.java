package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Button;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import businessPlanClasses.Category;
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
import model.Model;

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
		Model.currDepartment = adminProxy.getDepartment();
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
		BP bp = new BP("2018", "TestBP", adminDepartmentName);
		bp.setCategoryList();
		model.saveBPToDepartment(bp, adminDepartmentName);
		
		//Make sure the BP was actually saved
		assertEquals(1, ConcreteServer.adminDepartment.getPlans().size());
		
		//Update the display so that it shows the BP
		cont.setValidPlans(adminProxy, adminProxy.getDepartment());
		cont.updateBPScrollPane(adminProxy);
		
		sleep(100);
		clickOn(lookup(".bpButton").queryAll().iterator().next());
		
		//Make sure the editOrClonePopupBox is called and the correct business plan was given to the model
		assertEquals(1, model.getShowEditOrCloneMethodCallCounter());
		assertNotNull(model.getBusinessPlan());
		assertEquals("TestBP 2018", model.getBusinessPlan().getID());
		
	}
	
	@Test
	public void testSearchingBPs()
	{
		reset();
		cont.updateDepartmentDropDownMenu();
		
		//Create a new BP and test that it is displayed
		String adminDepartmentName = adminProxy.getDepartment().getDepartmentName();
		BP bp1 = new BP("2018", "TestBP", adminDepartmentName);
		BP bp2 = new BP("3018", "TestBP2", adminDepartmentName);
		BP bp3 = new BP("5", "Centre", adminDepartmentName);
		BP bp4 = new BP("65", "VMOSA", adminDepartmentName);
		
		bp1.setCategoryList();
		bp2.setCategoryList();
		bp3.setCategoryList();
		bp4.setCategoryList();
		
		model.saveBPToDepartment(bp1, adminDepartmentName);
		model.saveBPToDepartment(bp2, adminDepartmentName);
		model.saveBPToDepartment(bp3, adminDepartmentName);
		model.saveBPToDepartment(bp4, adminDepartmentName);
		
		//Make sure the BP was actually saved
		assertEquals(4, ConcreteServer.adminDepartment.getPlans().size());
		
		//Update the display so that it shows the BP
		cont.setValidPlans(adminProxy, adminProxy.getDepartment());
		cont.updateBPScrollPane(adminProxy);
		
		//Search by year
		doubleClickOn("#yearTextInput");
		write("18");		
		clickOn("#searchButton");
		int numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(2, numOfBPs);
		
		clickOn("#resetButton");
		
		doubleClickOn("#yearTextInput");
		write("5");
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(2, numOfBPs);
		
		doubleClickOn("#yearTextInput");
		write("65");
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(1, numOfBPs);
		
		clickOn("#resetButton");
		
		doubleClickOn("#yearTextInput");
		write("9");		
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(0, numOfBPs);
		
		clickOn("#resetButton");
		
		//Check that an empty search does not change the number of BPs displayed
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(4, numOfBPs);
		
		//Search by ID
		doubleClickOn("#idTextInput");
		write("Centre");		
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(1, numOfBPs);
		
		clickOn("#resetButton");
		
		doubleClickOn("#idTextInput");
		write("VMOSA");		
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(1, numOfBPs);
		
		clickOn("#resetButton");
		
		doubleClickOn("#idTextInput");
		write("TestBP");		
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(2, numOfBPs);
		
		doubleClickOn("#idTextInput");
		write("TestBP2");		
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(1, numOfBPs);

		clickOn("#resetButton");
		
		//Search by name and year
		doubleClickOn("#yearTextInput");
		write("18");		
		doubleClickOn("#idTextInput");
		write("TestBP");
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(2, numOfBPs);

		clickOn("#resetButton");
		
		doubleClickOn("#yearTextInput");
		write("18");		
		doubleClickOn("#idTextInput");
		write("TestBP2");
		clickOn("#searchButton");
		numOfBPs = lookup(".bpButton").queryAll().size();
		assertEquals(1, numOfBPs);
	}

}
