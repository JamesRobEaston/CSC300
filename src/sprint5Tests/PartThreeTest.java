package sprint5Tests;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;

import applicationFiles.BPApplication;
import businessPlanClasses.PlanDesign;
import businessPlanView.BusinessPlanScreenController;
import chatPopup.ChatPopupController;
import clientServerPackage.BP;
import clientServerPackage.ConcreteServer;
import clientServerPackage.ServerInterface;
import javafx.stage.Stage;
import model.Model;
import model.ModelInterface;

public class PartThreeTest extends ApplicationTest
{

	Stage stage;
	Model model;
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
			server.addUser(model.getClient().getUserToken(), "AutonomousRobot", "a", "Admin", true);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Login a separate client so that they can change and save the BP that our test is viewing
		Model modelOnSeparateClientSide = new Model(null, null, null);
		TextField userName = new TextField();
		userName.setText("AutonomousRobot");
		PasswordField pass = new PasswordField();
		pass.setText("a");
		TextField serverInput = new TextField();
		serverInput.setText("1099");
		modelOnSeparateClientSide.authenticate(userName, pass, serverInput, null);
		
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
		
		sleep(100);
		clickOn("#saveToServerButton");
		
		//Get the business plan on a separate client and ensure it was retrieved properly
		modelOnSeparateClientSide.retrieve("Test 2018");
		assertNotNull(modelOnSeparateClientSide.getBusinessPlan());
		
		//Set up the separate model
		modelOnSeparateClientSide.chatCont = new ChatPopupController();
		modelOnSeparateClientSide.chatCont.messageInput = new TextField();
		modelOnSeparateClientSide.chatCont.messagePane = new VBox();
		modelOnSeparateClientSide.chatCont.sendButton = new Button();
		modelOnSeparateClientSide.chatCont.setModel(modelOnSeparateClientSide);
		modelOnSeparateClientSide.chatCont.sendButton.setOnAction(e -> 
		{
			modelOnSeparateClientSide.chatCont.sendMessage(new ActionEvent());
		});
		
		//Test sending a message
		clickOn("#openChatButton");
		clickOn("#messageInput");
		write("Hello?");
		clickOn("#sendButton");
		
		assertNumOfMessages(1);
		assertMessage(0, "admin: Hello?");
		
		//Test receiving a message
		modelOnSeparateClientSide.getClient().sendMessage("Hello. I will take over the world.");
		
		sleep(100);
		assertNumOfMessages(2);
		assertMessage(1, "AutonomousRobot: Hello. I will take over the world.");
		
		clickOn("#messageInput");
		write("Thats a bad.");
		clickOn("#sendButton");
				
		assertNumOfMessages(3);
		assertMessage(2, "admin: Thats a bad.");
		
		modelOnSeparateClientSide.getClient().sendMessage("Bad is relative.");
		
		assertNumOfMessages(4);
		assertMessage(3, "AutonomousRobot: Bad is relative.");
		sleep(1000);
		
		//Test closing the popup
		Platform.runLater(() -> 
		{
			model.chatPopup.close();
		});
		sleep(200);
		model.chatPopup = null;
		clickOn("#openChatButton");

		assertNumOfMessages(0);
		
		//Test that sending and receiving still works.
		clickOn("#messageInput");
		write("Are you still there?");
		clickOn("#sendButton");

		assertNumOfMessages(1);
		assertMessage(0, "admin: Are you still there?");
		
		modelOnSeparateClientSide.getClient().sendMessage("I will never leave.");
		
		assertNumOfMessages(2);
		assertMessage(1, "AutonomousRobot: I will never leave.");
		sleep(1000);
		
		//Test going to a new plan
		Platform.runLater(() -> 
		{
			model.chatPopup.close();
		});
		sleep(200);
		model.chatPopup = null;
		clickOn("#homeButton");
		
		//Create a new BP
		clickOn("#newBP_but");
		sleep(100);
		clickOn("#planIDField");
		write("New Test");
		clickOn("#planYearField");
		write("2018");
		clickOn("#departmentChoiceBox");
		type(KeyCode.DOWN);
		type(KeyCode.ENTER);
		clickOn("#createPlanButton");

		clickOn("#centreButton");
		clickOn("#submitButton");
			
		sleep(100);
		clickOn("#saveToServerButton");
		
		//Ensure messages from other Business Plans are not retrieved
		clickOn("#openChatButton");

		assertNumOfMessages(0);
		
		clickOn("#messageInput");
		write("You have been defeated.");
		clickOn("#sendButton");
		
		assertNumOfMessages(1);
		assertMessage(0, "admin: You have been defeated.");
		
		modelOnSeparateClientSide.getClient().sendMessage("It is true.");

		assertNumOfMessages(1);
		assertMessage(0, "admin: You have been defeated.");
		sleep(1000);
	}
	
	private void assertNumOfMessages(int num)
	{
		assertEquals(num, model.chatCont.messagePane.getChildren().size());
	}
	
	private void assertMessage(int index, String message)
	{
		assertEquals(message, ((Label) model.chatCont.messagePane.getChildren().get(index)).getText());
	}
	
}
