package sprint5Tests;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;

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
import clientServerPackage.BP;
import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import clientServerPackage.ServerInterface;
import javafx.stage.Stage;
import model.Model;
import model.ModelInterface;

public class PartTwoTest extends ApplicationTest
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
		Department dept = new Department("CSC");
		server.getDepartments().add(dept);
		ConcreteClient user = new ConcreteClient("Tester", "test");
		user.setDepartment(dept);
		user.setAdmin(false);
		try
		{
			server.getUsers().add(user);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Log the user in
		clickOn("#usernameInput");
		write("Tester");
		clickOn("#passInput");
		write("test");
		clickOn("#loginButton");
		
		//Create a new BP
		clickOn("#newBP_but");
		sleep(100);
		clickOn("#planIDField");
		write("Test");
		clickOn("#planYearField");
		write("2018");
		clickOn("#createPlanButton");

		clickOn("#centreButton");
		clickOn("#submitButton");

		clickOn("#saveToServerButton");
		
		//Test making comments
		BusinessPlanScreenController cont = model.loader.getController();
		ObservableList<Node> screenElements = cont.commentsNode.getChildren();
		assertEquals(1, screenElements.size());
		
		clickOn("#commentsTab");
		clickOn("#addNewCommentButton");
		clickOn("#newCommentInput");
		
		write("New Comment 1");
		
		clickOn("#submitButton");
		
		clickOn("#addNewCommentButton");
		clickOn("#newCommentInput");
		
		write("New Comment 2");
		
		clickOn("#submitButton");
		
		//Check that there are 3 children
		assertEquals(3, screenElements.size());
		
		//Check that the two added children are labels
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals(Label.class, ((HBox) screenElements.get(1)).getChildren().get(0).getClass());
		
		//Check that the labels are correct
		assertEquals("New Comment 1", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(1)).getChildren().get(0)).getText());
		
		//Check that the Side-By-Side tab is the same
		clickOn("#SBSTab");
		cont = model.loader.getController();
		screenElements = cont.commentsNode1.getChildren();

		assertEquals(3, screenElements.size());
		
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals(Label.class, ((HBox) screenElements.get(1)).getChildren().get(0).getClass());
		
		assertEquals("New Comment 1", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(1)).getChildren().get(0)).getText());
		
		//Ensure that traversing the BP does not change the comments for a specific BP
		clickOn("#createNewSubcategoryButton");
		clickOn("#newChildIDInput");
		write("Test child");
		clickOn("Submit");
		
		clickOn("#commentsTab");
		cont = model.loader.getController();
		screenElements = cont.commentsNode.getChildren();
		assertEquals(1, screenElements.size());
		
		clickOn("#SBSTab");
		cont = model.loader.getController();
		screenElements = cont.commentsNode1.getChildren();

		assertEquals(1, screenElements.size());
		
		clickOn("#goUpALevelButton");
		clickOn("#commentsTab");
		
		//Check that nothing changed
		cont = model.loader.getController();
		screenElements = cont.commentsNode.getChildren();
		assertEquals(3, screenElements.size());
		
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals(Label.class, ((HBox) screenElements.get(1)).getChildren().get(0).getClass());
		
		assertEquals("New Comment 1", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(1)).getChildren().get(0)).getText());

		cont = model.loader.getController();
		screenElements = cont.commentsNode1.getChildren();
		assertEquals(3, screenElements.size());
		
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals(Label.class, ((HBox) screenElements.get(1)).getChildren().get(0).getClass());
		
		assertEquals("New Comment 1", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(1)).getChildren().get(0)).getText());
		
		//Test that saving works appropriately
		clickOn("#saveToServerButton");
		clickOn("#homeButton");
		clickOn("#viewAll_but");
		clickOn(lookup(".bpButton").queryAll().iterator().next());
		clickOn("#editButton");

		//Check that nothing changed
		clickOn("#commentsTab");
		cont = model.loader.getController();
		screenElements = cont.commentsNode.getChildren();
		assertEquals(3, screenElements.size());
		
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals(Label.class, ((HBox) screenElements.get(1)).getChildren().get(0).getClass());
		
		assertEquals("New Comment 1", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(1)).getChildren().get(0)).getText());

		cont = model.loader.getController();
		screenElements = cont.commentsNode1.getChildren();
		clickOn("#SBSTab");
		assertEquals(3, screenElements.size());
		
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals(Label.class, ((HBox) screenElements.get(1)).getChildren().get(0).getClass());
		
		assertEquals("New Comment 1", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(1)).getChildren().get(0)).getText());
		
		//Try deleting a comment
		screenElements = cont.commentsNode.getChildren();
		clickOn("#commentsTab");
		clickOn(lookup(".mainDeleteCommentButton").queryAll().iterator().next());
		
		assertEquals(2, screenElements.size());
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());

		screenElements = cont.commentsNode1.getChildren();
		clickOn("#SBSTab");

		assertEquals(2, screenElements.size());
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		
		//Try deleting comments on a non-editable plan from a different user
		ConcreteClient user2 = new ConcreteClient("Tester2", "test");
		user2.setDepartment(dept);
		user2.setAdmin(false);
		try
		{
			server.getUsers().add(user2);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Log out the old user and log in the new one
		clickOn("#saveToServerButton");
		clickOn("#homeButton");
		clickOn("#logout_but");
		
		clickOn("#usernameInput");
		write("Tester2");
		clickOn("#passInput");
		write("test");
		clickOn("#loginButton");

		clickOn("#viewAll_but");
		server.getDepartments().get(1).getPlans().get(0).setEditable(false);
		clickOn(lookup(".bpButton").queryAll().iterator().next());
		clickOn("#editButton");
		
		//Ensure that the comment is still visible
		assertEquals(2, screenElements.size());
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());

		screenElements = cont.commentsNode1.getChildren();
		clickOn("#SBSTab");

		assertEquals(2, screenElements.size());
		assertEquals(Label.class, ((HBox) screenElements.get(0)).getChildren().get(0).getClass());;
		assertEquals("New Comment 2", ((Label) ((HBox) screenElements.get(0)).getChildren().get(0)).getText());
		
		cont = model.loader.getController();
		screenElements = cont.commentsNode.getChildren();
		clickOn("#commentsTab");
		clickOn(lookup(".mainDeleteCommentButton").queryAll().iterator().next());
		
		assertEquals(1, screenElements.size());

		screenElements = cont.commentsNode1.getChildren();
		clickOn("#SBSTab");

		assertEquals(1, screenElements.size());
		
	}
}
