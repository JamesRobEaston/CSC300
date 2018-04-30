package tests;

import org.testfx.framework.junit.ApplicationTest;

import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import clientServerPackage.ServerInterface;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import homePage.homePageController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Model;
import saveBPPopup.SaveBPPopupBoxController;
import viewAllBPView.ViewAllBPScreenController;

public class SaveBPTest extends ApplicationTest
{

	SaveBPPopupBoxController spb;
	MockModel model;
	Stage popupStage;
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
		loader.setLocation(SaveBPPopupBoxController.class.getResource("/saveBPPopup/SaveBPPopupBox.fxml"));
		
		Scene newScene = new Scene(new AnchorPane());
		
		try
		{
			newScene = new Scene(loader.load());
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		spb = loader.getController();
		spb.setModel(model);
		
		popupStage = new Stage();
		popupStage.setWidth(500.0);
		popupStage.setHeight(350.0);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("");
		popupStage.setResizable(false);
		popupStage.setScene(newScene);
		popupStage.show();		
				
		
		
		
		
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
		spb.setModel(model);

	}
	

	
	public void testCancel()
	{
		reset();
		assertEquals(model.closePopupBoxMethodCallCounter, 0);
		clickOn("#cancel_but");
		assertEquals(model.closePopupBoxMethodCallCounter, 1);
		
		
	}
	
	public void testDiscard()
	{
		reset();
		assertEquals(model.showHomeMethodCallCounter, 0);
		clickOn("#discard_but");
		assertEquals(model.showHomeMethodCallCounter, 1);
		
		
	}
	
	public void testSave()
	{
		reset();
		assertEquals(model.saveLocalPlanXMLMethodCallCounter, 0);
		clickOn("#save_but");
		assertEquals(model.saveLocalPlanXMLMethodCallCounter, 1);
		
	}

	
	@Test
	public void testValidLoginInvalidServer()
	{
	
		testSave();
		testCancel();
		testDiscard();	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
