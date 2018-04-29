package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

import applicationFiles.DepartmentConverter;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import newBPPopup.NewBPPopupBoxController;

public class NewBPPopupTest extends ApplicationTest
{
	
	boolean shouldBeClone;
	MockModel model;
	NewBPPopupBoxController cont;
	ConcreteServer server;
	ClientProxy client;
	Stage primaryStage;

	@Override
	public void start(Stage stage) throws Exception
	{
		primaryStage = stage;
		server = new ConcreteServer("admin", "admin");
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("Server", stub);

		} catch (Exception e)
		{
		}
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewBPPopupBoxController.class.getResource("/newBPPopup/NewBPPopupBox.fxml"));
		try
		{
			Scene newBPScene = new Scene(loader.load());
			
			primaryStage = new Stage();
			primaryStage.setTitle("New Business Plan");
			primaryStage.setResizable(false);
			primaryStage.setScene(newBPScene);
			primaryStage.show();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		cont = loader.getController();
		
		shouldBeClone = false;

	}
	
	private void reset()
	{
		//Reset the server and rebind it
		server = new ConcreteServer("admin", "admin");
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.getRegistry(1099);
			registry.rebind("Server", stub);

		} catch (Exception e)
		{
		}
		
		//Log the local ClientProxy into the admin account
		client = new ClientProxy();
		String adminUserToken = server.genUserToken();
		
		ConcreteClient admin;
		try
		{
			admin = server.getUsers().get(0);
			admin.userToken = adminUserToken;
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		ServerInterface stub = null;
		try 
		{
			Registry registry = LocateRegistry.getRegistry(1099);
			stub = (ServerInterface) registry.lookup("Server");
		} 
		catch (Exception e){}
		
		client.setUserToken(adminUserToken);
		client.setStub(stub);
		
		//Reset the model
		model = new MockModel(client, null, null);
		
		cont.setIsClone(shouldBeClone);
		cont.setModel(model);
		
		resetDepartmentChoiceBox();
		
		cont.department = client.getDepartment().getName();
	}
	
	private void resetDepartmentChoiceBox()
	{
		ArrayList<Department> departments = model.getAllDepartments();
		cont.departmentChoiceBox.getItems().clear();
		for(int i = 0; i < departments.size(); i++)
		{
			cont.departmentChoiceBox.getItems().add(departments.get(i));
		}
		cont.departmentChoiceBox.setConverter(new DepartmentConverter<Department>());
		
		cont.departmentChoiceBox.setValue(client.getDepartment());
	}
	
	private void closePrimaryStage()
	{
		Platform.runLater(() ->
		{
			primaryStage.close();
		});
		sleep(50);
	}
	
	private void assertInitState()
	{
		assertEquals(0, model.getClosePopupBoxMethodCallCounter());
		assertEquals(0, model.getShowCategoryPopupBoxMethodCallCounter());
		assertEquals(0, model.getShowBusinessPlanScreenMethodCallCounter());
	}
	
	@Test
	public void testCancel()
	{
		reset();
		assertInitState();
		clickOn("#closePopupButton");
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
		closePrimaryStage();
	}
	
	@Test
	public void testMakingAUniquePlan()
	{
		reset();
		assertInitState();
		
		doubleClickOn("#planIDField");
		write("UniquePlan");
		clickOn("#planYearField");
		write("2018");
		clickOn("#departmentChoiceBox");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createPlanButton");
		
		assertEquals(1, model.getClosePopupBoxMethodCallCounter());
		assertEquals(1, model.getShowCategoryPopupBoxMethodCallCounter());
		closePrimaryStage();
	}
	
	@Test
	public void testEmptyFields()
	{
		reset();
		assertInitState();
		
		clickOn("#createPlanButton");
		assertInitState();

		doubleClickOn("#planIDField");
		write("   ");
		doubleClickOn("#planYearField");
		write("   ");
		clickOn("#createPlanButton");
		assertInitState();

		doubleClickOn("#planIDField");
		write("NoYear");
		doubleClickOn("#planYearField");
		write("   ");
		clickOn("#createPlanButton");
		assertInitState();

		doubleClickOn("#planIDField");
		write("   ");
		doubleClickOn("#planYearField");
		write("NoID");
		clickOn("#createPlanButton");
		assertInitState();
		
		closePrimaryStage();
	}
	
	@Test
	public void testMakingAnAlreadyExistingPlan()
	{
		reset();
		assertInitState();
		
		BP bp = new BP("2018", "AlreadyExistingPlan", "Admin");
		try
		{
			server.saveToAdminDepartment(client.getUserToken(), bp);
		} catch (RemoteException e)
		{
			fail("OOF.");
		}
		
		doubleClickOn("#planIDField");
		write("AlreadyExistingPlan");
		doubleClickOn("#planYearField");
		write("2018");
		clickOn("#departmentChoiceBox");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createPlanButton");
		assertInitState();
		
		closePrimaryStage();
	}
	
	@Test
	public void testMakingTheSamePlanInTwoDepartments()
	{
		reset();
		assertInitState();
		
		try
		{
			server.addDepartment(client.getUserToken(), "CSC");
			server.addDepartment(client.getUserToken(), "Art");
		}
		catch(Exception e){}
		
		resetDepartmentChoiceBox();
		
		doubleClickOn("#planIDField");
		write("SamePlanDifferentDepartment");
		clickOn("#planYearField");
		write("2018");
		clickOn("#departmentChoiceBox");
		type(KeyCode.DOWN);
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createPlanButton");
		
		doubleClickOn("#planIDField");
		write("SamePlanDifferentDepartment");
		clickOn("#planYearField");
		write("2018");
		clickOn("#departmentChoiceBox");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createPlanButton");
		
		assertEquals(2, model.getClosePopupBoxMethodCallCounter());
		assertEquals(2, model.getShowCategoryPopupBoxMethodCallCounter());
		
		closePrimaryStage();
	}

}
