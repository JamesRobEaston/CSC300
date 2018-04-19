package businessPlanView;

import businessPlanClasses.Statement;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import applicationFiles.BPApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BusinessPlanScreenController
{

	@FXML
	private Button deleteButton;

	@FXML
	private Button saveButton;

	@FXML
	private Button saveToServerButton;

	@FXML
	private Label isEditableLabel;

	@FXML
	private CheckBox isEditableCheckBox;

	@FXML
	private Button goUpALevelButton;

	@FXML
	private Label categoryLabel;

	@FXML
	private Label nameLabel;

	@FXML
	private VBox statementsPane;

	@FXML
	private Button addNewStatementButton;

	@FXML
	private Label subcategoryLabel;
	
	Scene viewBPScreen;
	BP businessPlan;
	Statement currentNode;
	boolean needsToBeSaved;
	BPApplication application;
	ClientProxy client;

	public BusinessPlanScreenController(BP bp, ClientProxy client, BPApplication application)
	{
		businessPlan = bp;
		needsToBeSaved = false;
		this.client = client;
		this.application = application;
		application.notify(viewBPStatement(bp.getTree().getRoot()));
	}
	
	public Scene viewBPStatement(Statement statement)
	{
		currentNode = statement;
		
		nameLabel.setText("Name: " + currentNode.getId());
		categoryLabel.setText("Category: " + statement.getType().getName());
	}
	
	@FXML
	void addNewStatement(ActionEvent event)
	{

	}

	@FXML
	void changeEditability(ActionEvent event)
	{

	}

	@FXML
	void createNewSubcategory(ActionEvent event)
	{

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

	}

	@FXML
	void saveToServer(ActionEvent event)
	{

	}

}
