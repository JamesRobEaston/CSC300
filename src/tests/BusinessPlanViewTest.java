package tests;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.junit.*;
import org.testfx.framework.junit.ApplicationTest;

import businessPlanClasses.Category;
import businessPlanClasses.PlanDesign;
import businessPlanClasses.Statement;
import businessPlanView.BusinessPlanScreenController;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteClient;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import clientServerPackage.ServerInterface;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import model.Model;

public class BusinessPlanViewTest extends ApplicationTest
{

	BusinessPlanScreenController cont;
	ConcreteServer server;
	MockModel model;
	ClientProxy adminProxy;
	BP businessPlan;
	Statement rootStatement;
	Stage stage;
	Scene screen;

	@Override
	public void start(Stage stage)
	{
		this.stage = stage;
		server = new ConcreteServer("admin", "admin");
		try
		{
			ServerInterface stub = (ServerInterface) UnicastRemoteObject.exportObject(server, 0);

			Registry registry = LocateRegistry.createRegistry(1099);
			registry.bind("Server", stub);

		} catch (Exception e)
		{
		}

		businessPlan = new BP("test","Head Element","test");
		PlanDesign design = businessPlan.getDesign();
		design.addCategory("1st Element", 1, 0, 10000000);
		rootStatement = businessPlan.getTree().getRoot();
		
		model = new MockModel(new ClientProxy(), null, businessPlan);
		model.client.setStub(server);
		model.client.login("admin", "admin");
		
		showBusinessPlanScreen(rootStatement,stage);
	}
	
	public void showBusinessPlanScreen(Statement statement,Stage stage)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BusinessPlanScreenController.class.getResource("/businessPlanView/BusinessPlanScreen.fxml"));
		
		try
		{
			screen = new Scene(loader.load());
			cont = loader.getController();
			cont.setModel(model);
			stage.setScene(screen);
			stage.show();
		} catch (Exception e)
		{

			e.printStackTrace();
		}
		
		BusinessPlanScreenController cont = loader.getController();
		cont.setModel(model);
		
		cont.currentNode = statement;
		
		cont.nameLabel.setText("Name: " + cont.currentNode.getId());
		cont.categoryLabel.setText("Category: " + statement.getType().getName());
		
		Statement curr = statement;
		while(curr != null)
		{
			cont.treeDisplay.getChildren().add(0, new Label(">" + curr.getId()));
			curr = curr.getParent();
		}
			
		boolean hasParent = !(cont.currentNode.getParent() == null);
		boolean isEditable = businessPlan.isEditable();
		boolean isAdmin = model.client.isAdmin();
		
		//Determine whether the goUpALevel button is visible
		if(!hasParent)
		{
			cont.goUpALevelButton.setVisible(false);
		}
		else
		{
			cont.goUpALevelButton.setVisible(true);
		}
		
		//Determine if the delete button is visible
		
		if(hasParent && isEditable)
		{
			cont.deleteButton.setVisible(true);
		}
		else
		{
			cont.deleteButton.setVisible(false);
		}
		
		//Determine if the isEditable checkbox is visible
		if(isAdmin)
		{
			cont.isEditableHBox.setVisible(true);
			cont.isEditableLabel.setVisible(false);
		}
		else
		{
			cont.isEditableHBox.setVisible(false);
			cont.isEditableLabel.setVisible(true);
		}
		
		//Display whether or not the BP is editable
		if(businessPlan.isEditable())
		{
			cont.isEditableCheckbox.fire();
			cont.isEditableLabel.setText("Is Editable");
		}
		else 
		{
			cont.isEditableLabel.setText("Is Not Editable");
		}
		
		//Determine if the save buttons are visible
		if(isAdmin || isEditable)
		{
			cont.saveButton.setVisible(true);
			cont.saveToServerButton.setVisible(true);
		}
		else
		{
			cont.saveButton.setVisible(false);
			cont.saveToServerButton.setVisible(false);
		}
		
		//Populate statementsNode
		if(statement.getData()!= null)
		{
			for(String dataStatement : statement.getData())
			{
				HBox statementPane = new HBox(20);
				Label statementLabel = new Label(dataStatement);
				
				Button deleteStatementButton = new Button("Delete");
				deleteStatementButton.setOnAction(e ->
				{
					cont.needsToBeSaved = true;
					cont.statementsNode.getChildren().remove(statementPane);
					for(int i = 0; i < statement.getData().size(); i++)
					{
						String data = statement.getData().get(i);
						if(data.equals(dataStatement))
						{
							statement.removeData(i);
							break;
						}
					}
				});
				
				statementPane.getChildren().addAll(statementLabel, deleteStatementButton);
				
				cont.statementsNode.getChildren().remove(cont.addNewStatementButton);
				cont.statementsNode.getChildren().add(statementPane);
				cont.statementsNode.getChildren().add(cont.addNewStatementButton);
			}
		}
		
		if(isEditable)
		{
			cont.addNewStatementButton.setVisible(true);
		}
		else
		{
			cont.addNewStatementButton.setVisible(false);
		}
		
		//Determine if the scroll pane that displays the child of this node should be displayed
		businessPlan.getDesign().orderCategories();
		ArrayList<Category> categoryList = businessPlan.getDesign().getCategoryList();
		boolean isLowestCategory = statement.getType().getName().equals(categoryList.get(categoryList.size()-1).getName());
		
		if(isLowestCategory)
		{
			cont.childrenScrollPane.setVisible(false);
			cont.subcategoryLabel.setVisible(false);
		}
		else
		{
			cont.childrenScrollPane.setVisible(true);
			cont.subcategoryLabel.setVisible(true);

			String nextCategory = categoryList.get(categoryList.indexOf(statement.getType()) + 1).getName();
			cont.subcategoryLabel = new Label(nextCategory);
		}
		
		//Populate childrenNode
		for(Statement childStatement: statement.getChildren())
		{
			Button chooseStatementButton = new Button(childStatement.getId());
			chooseStatementButton.setOnAction(e ->
			{
				showBusinessPlanScreen(childStatement,stage);
			});
			cont.childrenNode.getChildren().remove(cont.createNewSubcategoryButton);
			cont.childrenNode.getChildren().add(chooseStatementButton);
			cont.childrenNode.getChildren().add(cont.createNewSubcategoryButton);
		}
	}
	
	public void update(Statement statement,Stage stage)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BusinessPlanScreenController.class.getResource("/businessPlanView/BusinessPlanScreen.fxml"));
		
		Scene viewBPScreen = new Scene(new AnchorPane());
		
		try
		{
			screen = new Scene(loader.load());
			cont = loader.getController();
			cont.setModel(model);
		} catch (Exception e)
		{

			e.printStackTrace();
		}
		
		BusinessPlanScreenController cont = loader.getController();
		cont.setModel(model);
		
		cont.currentNode = statement;
		
		cont.nameLabel.setText("Name: " + cont.currentNode.getId());
		cont.categoryLabel.setText("Category: " + statement.getType().getName());
		
		Statement curr = statement;
		while(curr != null)
		{
			cont.treeDisplay.getChildren().add(0, new Label(">" + curr.getId()));
			curr = curr.getParent();
		}
			
		boolean hasParent = !(cont.currentNode.getParent() == null);
		boolean isEditable = businessPlan.isEditable();
		boolean isAdmin = model.client.isAdmin();
		
		//Determine whether the goUpALevel button is visible
		if(!hasParent)
		{
			cont.goUpALevelButton.setVisible(false);
		}
		else
		{
			cont.goUpALevelButton.setVisible(true);
		}
		
		//Determine if the delete button is visible
		
		if(hasParent && isEditable)
		{
			cont.deleteButton.setVisible(true);
		}
		else
		{
			cont.deleteButton.setVisible(false);
		}
		
		//Determine if the isEditable checkbox is visible
		if(isAdmin)
		{
			cont.isEditableHBox.setVisible(true);
			cont.isEditableLabel.setVisible(false);
		}
		else
		{
			cont.isEditableHBox.setVisible(false);
			cont.isEditableLabel.setVisible(true);
		}
		
		//Display whether or not the BP is editable
		if(businessPlan.isEditable())
		{
			cont.isEditableCheckbox.fire();
			cont.isEditableLabel.setText("Is Editable");
		}
		else 
		{
			cont.isEditableLabel.setText("Is Not Editable");
		}
		
		//Determine if the save buttons are visible
		if(isAdmin || isEditable)
		{
			cont.saveButton.setVisible(true);
			cont.saveToServerButton.setVisible(true);
		}
		else
		{
			cont.saveButton.setVisible(false);
			cont.saveToServerButton.setVisible(false);
		}
		
		//Populate statementsNode
		if(statement.getData()!= null)
		{
			for(String dataStatement : statement.getData())
			{
				HBox statementPane = new HBox(20);
				Label statementLabel = new Label(dataStatement);
				
				Button deleteStatementButton = new Button("Delete");
				deleteStatementButton.setOnAction(e ->
				{
					cont.needsToBeSaved = true;
					cont.statementsNode.getChildren().remove(statementPane);
					for(int i = 0; i < statement.getData().size(); i++)
					{
						String data = statement.getData().get(i);
						if(data.equals(dataStatement))
						{
							statement.removeData(i);
							break;
						}
					}
				});
				
				statementPane.getChildren().addAll(statementLabel, deleteStatementButton);
				
				cont.statementsNode.getChildren().remove(cont.addNewStatementButton);
				cont.statementsNode.getChildren().add(statementPane);
				cont.statementsNode.getChildren().add(cont.addNewStatementButton);
			}
		}
		
		if(isEditable)
		{
			cont.addNewStatementButton.setVisible(true);
		}
		else
		{
			cont.addNewStatementButton.setVisible(false);
		}
		
		//Determine if the scroll pane that displays the child of this node should be displayed
		businessPlan.getDesign().orderCategories();
		ArrayList<Category> categoryList = businessPlan.getDesign().getCategoryList();
		boolean isLowestCategory = statement.getType().getName().equals(categoryList.get(categoryList.size()-1).getName());
		
		if(isLowestCategory)
		{
			cont.childrenScrollPane.setVisible(false);
			cont.subcategoryLabel.setVisible(false);
		}
		else
		{
			cont.childrenScrollPane.setVisible(true);
			cont.subcategoryLabel.setVisible(true);

			String nextCategory = categoryList.get(categoryList.indexOf(statement.getType()) + 1).getName();
			cont.subcategoryLabel = new Label(nextCategory);
		}
		
		//Populate childrenNode
		for(Statement childStatement: statement.getChildren())
		{
			Button chooseStatementButton = new Button(childStatement.getId());
			chooseStatementButton.setOnAction(e ->
			{
				showBusinessPlanScreen(childStatement,stage);
			});
			cont.childrenNode.getChildren().remove(cont.createNewSubcategoryButton);
			cont.childrenNode.getChildren().add(chooseStatementButton);
			cont.childrenNode.getChildren().add(cont.createNewSubcategoryButton);
		}
		//this.notify();
	}
	
	@Test
	public void testHomeButtonNotSaved()
	{
		assertEquals(0, model.getShowSaveBPPopupBoxMethodCallCounter());
		clickOn("#homeButton");
		assertEquals(1, model.getShowSaveBPPopupBoxMethodCallCounter());
	}
	
	@Test
	public void testSaveAndHomeButton()
	{
		assertEquals(0, model.getSetBusinessPlanMethodCallCounter());
		assertEquals(0, model.getSaveLocalPlanXMLMethodCallCounter());
		clickOn("#saveButton");
		assertEquals(1, model.getSetBusinessPlanMethodCallCounter());
		assertEquals(1, model.getSaveLocalPlanXMLMethodCallCounter());
		assertEquals(0, model.getShowHomeMethodCallCounter());
		clickOn("#homeButton");
		assertEquals(1, model.getShowHomeMethodCallCounter());
	}
	
	@Test
	public void testIsEditableAndHomeButton()
	{
		assertEquals(true,model.getBusinessPlan().isEditable());
		clickOn("#isEditableCheckbox");
		assertEquals(false,model.getBusinessPlan().isEditable());
		assertEquals(0, model.getShowHomeMethodCallCounter());
		clickOn("#homeButton");
		assertEquals(1, model.getShowHomeMethodCallCounter());
	}
	
	@Test
	public void testServerSaveButton()
	{
		assertEquals(0, model.getSetBusinessPlanMethodCallCounter());
		assertEquals(0, model.getIsAdminMethodCallCounter());
		clickOn("#saveToServerButton");
		assertEquals(1, model.getSetBusinessPlanMethodCallCounter());
		assertEquals(1, model.getIsAdminMethodCallCounter());
	}
	
	@Test
	public void testGeneral()
	{
		clickOn("#addNewStatementButton");
		write("Test Statement");
		clickOn("Submit");
		// TODO see that the element actually exists
		clickOn("Delete");
		// TODO see that element no longer exists
		clickOn("#createNewSubcategoryButton");
		push(KeyCode.TAB);
		write("Test 1st Element");
		clickOn("Submit");
		assertEquals(1, model.getShowBusinessPlanScreenMethodCallCounter());
		clickOn("Submit");
		assertEquals(2, model.getShowBusinessPlanScreenMethodCallCounter());
		
		
		
	}
}
