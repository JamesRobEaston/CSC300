package businessPlanView;

import businessPlanClasses.Category;
import businessPlanClasses.Statement;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;

import java.io.IOException;
import java.util.ArrayList;

import applicationFiles.BPApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BusinessPlanScreenController
{

	@FXML
    private Button goUpALevelButton;

    @FXML
    private Label categoryLabel;

    @FXML
    private Label nameLabel;

    @FXML
    private VBox statementsNode;

    @FXML
    private Button addNewStatementButton;

    @FXML
    private Label subcategoryLabel;

    @FXML
    private Button createNewSubcategoryButton;

    @FXML
    private TilePane childrenNode;
    
    @FXML
    private Button deleteButton;
    
    @FXML
    private ScrollPane childrenScrollPane;

    @FXML
    private Button homeButton;

    @FXML
    private Button saveButton;

    @FXML
    private Button saveToServerButton;

    @FXML
    private HBox isEditableHBox;

    @FXML
    private CheckBox isEditableCheckbox;

    @FXML
    private Label isEditableLabel;

    @FXML
    private Label invalidSaveLabel;
	
	Scene viewBPScreen;
	BP businessPlan;
	Statement currentNode;
	boolean needsToBeSaved;
	BPApplication application;
	ClientProxy client;

	public BusinessPlanScreenController() {}
	
	public BusinessPlanScreenController(BP bp, ClientProxy client, BPApplication application)
	{
		businessPlan = bp;
		needsToBeSaved = false;
		this.client = client;
		this.application = application;
		viewBPScreen = viewBPStatement(bp.getTree().getRoot());
	}
	
	public Scene viewBPStatement(Statement statement)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BusinessPlanScreenController.class.getResource("businessPlanView/BusinessPlanScreen.fxml"));
		
		try
		{
			viewBPScreen = new Scene(loader.load());
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		currentNode = statement;
		
		nameLabel.setText("Name: " + currentNode.getId());
		categoryLabel.setText("Category: " + statement.getType().getName());
		
		boolean hasParent = !(currentNode.getParent() == null);
		boolean isEditable = businessPlan.isEditable();
		boolean isAdmin = client.isAdmin();
		
		//Determine whether the goUpALevel button is visible
		if(!hasParent)
		{
			goUpALevelButton.setVisible(false);
		}
		else
		{
			goUpALevelButton.setVisible(true);
		}
		
		//Determine if the delete button is visible
		
		if(hasParent && isEditable)
		{
			deleteButton.setVisible(true);
		}
		else
		{
			deleteButton.setVisible(false);
		}
		
		//Determine if the isEditable checkbox is visible
		if(isAdmin)
		{
			isEditableHBox.setVisible(true);
			isEditableLabel.setVisible(false);
		}
		else
		{
			isEditableHBox.setVisible(false);
			isEditableLabel.setVisible(true);
		}
		
		//Display whether or not the BP is editable
		if(businessPlan.isEditable())
		{
			isEditableCheckbox.fire();
			isEditableLabel.setText("Is Editable");
		}
		else 
		{
			isEditableLabel.setText("Is Not Editable");
		}
		
		//Determine if the save buttons are visible
		if(isAdmin || isEditable)
		{
			saveButton.setVisible(true);
			saveToServerButton.setVisible(true);
		}
		else
		{
			saveButton.setVisible(false);
			saveToServerButton.setVisible(false);
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
					needsToBeSaved = true;
					statementsNode.getChildren().remove(statementPane);
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
				
				statementsNode.getChildren().remove(addNewStatementButton);
				statementsNode.getChildren().add(statementPane);
				statementsNode.getChildren().add(addNewStatementButton);
			}
		}
		
		if(isEditable)
		{
			addNewStatementButton.setVisible(true);
		}
		else
		{
			addNewStatementButton.setVisible(false);
		}
		
		//Determine if the scroll pane that displays the child of this node should be displayed
		businessPlan.getDesign().orderCategories();
		ArrayList<Category> categoryList = businessPlan.getDesign().getCategoryList();
		boolean isLowestCategory = statement.getType().getName().equals(categoryList.get(categoryList.size()-1).getName());
		
		if(isLowestCategory)
		{
			childrenScrollPane.setVisible(false);
			subcategoryLabel.setVisible(false);
		}
		else
		{
			childrenScrollPane.setVisible(true);
			subcategoryLabel.setVisible(true);

			String nextCategory = categoryList.get(categoryList.indexOf(statement.getType()) + 1).getName();
			subcategoryLabel = new Label(nextCategory);
		}
		
		//Populate childrenNode
		for(Statement childStatement: statement.getChildren())
		{
			Button chooseStatementButton = new Button(childStatement.getId());
			chooseStatementButton.setOnAction(e ->
			{
				application.notify(viewBPStatement(childStatement));
			});
			childrenNode.getChildren().remove(createNewSubcategoryButton);
			childrenNode.getChildren().add(chooseStatementButton);
			childrenNode.getChildren().add(createNewSubcategoryButton);
		}
		
		return viewBPScreen;
	}
	
	@FXML
	void addNewStatement(ActionEvent event)
	{
		TextField newStatementInput = new TextField();
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e1 ->
		{
			needsToBeSaved = true;
			HBox statementPane = new HBox(20);
			
			Label statementLabel = new Label(newStatementInput.getText());
			
			statementPane.getChildren().addAll(statementLabel);

			if(businessPlan.isEditable())
			{
				Button deleteStatementButton = new Button("Delete");
				deleteStatementButton.setOnAction(e2 ->
				{
					needsToBeSaved = true;
					statementsNode.getChildren().remove(statementPane);
					for(int i = 0; i < currentNode.getData().size(); i++)
					{
						String data = currentNode.getData().get(i);
						if(data.equals(newStatementInput.getText()))
						{
							currentNode.removeData(i);
							break;
						}
					}
				});
				
				statementPane.getChildren().add(deleteStatementButton);
			}
			
			currentNode.addData(newStatementInput.getText());
			statementsNode.getChildren().remove(newStatementInput);
			statementsNode.getChildren().remove(submitButton);
			statementsNode.getChildren().addAll(statementPane, addNewStatementButton);
		});
		
		statementsNode.getChildren().remove(addNewStatementButton);
		statementsNode.getChildren().addAll(newStatementInput, submitButton);
	}

	@FXML
	void changeEditability(ActionEvent event)
	{
		needsToBeSaved = true;
		businessPlan.setEditable(isEditableCheckbox.isSelected());
		new BusinessPlanScreenController(businessPlan, client, application);
	}

	@FXML
	void createNewSubcategory(ActionEvent event)
	{
		needsToBeSaved = true;
		TextField newChildIDInput = new TextField();
		newChildIDInput.setPromptText("Enter a new ID");
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e1 ->
		{
			ArrayList<Category> categories = businessPlan.getDesign().getCategoryList();
			Category newChildCategory = categories.get(categories.indexOf(currentNode.getType()) + 1);
			Statement newChild = new Statement(newChildCategory, currentNode, new ArrayList<String>(), newChildIDInput.getText());
			currentNode.addChild(newChild);
			application.notify(viewBPStatement(newChild));
		});
		childrenNode.getChildren().remove(createNewSubcategoryButton);
		childrenNode.getChildren().addAll(newChildIDInput, submitButton);
	}

	@FXML
	void deleteNode(ActionEvent event)
	{
		if(currentNode.getParent() != null)
		{
			int indexOfThis = -1;
			Statement parent = currentNode.getParent();
			for(int i = 0; i < parent.getChildren().size(); i++)
			{
				Statement child = parent.getChildren().get(i);
				if(child.getId().equals(currentNode.getId()))
				{
					indexOfThis = i;
					break;
				}
			}
			
			parent.getChildren().remove(indexOfThis);

			needsToBeSaved = true;
			application.notify(viewBPStatement(currentNode.getParent()));
		}
		else
		{
			client.delete(businessPlan);
		}
	}

	@FXML
	void goHome(ActionEvent event)
	{
		if(businessPlan.isEditable() && needsToBeSaved)
		{
			//TODO: Open up a popupbox to prompt the user to save
			//SaveBPPopupBox.show(businessPlan);
		}
		else
		{
			//TODO: Show Home Screen
			//application.notify(HomeScreen.homeScreen);
		}
	}

	@FXML
	void goUpALevel(ActionEvent event)
	{
		if(currentNode.getParent() != null)
		{
			application.notify(viewBPStatement(currentNode.getParent()));
		}
	}

	@FXML
	void saveLocally(ActionEvent event)
	{
		client.setBusinessPlan(businessPlan);
		client.saveLocalPlanXML();
		needsToBeSaved = false;
	}

	@FXML
	void saveToServer(ActionEvent event)
	{
		client.setBusinessPlan(businessPlan);
		try
		{
			if(client.isAdmin())
			{
				//TODO
				//client.saveBPToDepartment(businessPlan, ViewAllScreen.currDepartment.getDepartmentName());
			}
			else
			{
				client.submitPlan();
			}
			//TODO
			//new ViewAllScreen(client, application);
			needsToBeSaved = false;
			invalidSaveLabel.setText("");
			
		} catch (Exception e1)
		{
			invalidSaveLabel.setText("Saving to the server failed.");
			e1.printStackTrace();
		}
	}
	
	public Scene getScene()
	{
		return viewBPScreen;
	}
}
