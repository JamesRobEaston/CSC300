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

	public BusinessPlanScreenController(BP bp, ClientProxy client, BPApplication application)
	{
		businessPlan = bp;
		needsToBeSaved = false;
		application.notify(viewBPStatement(bp.getTree().getRoot(), client, application));
	}
	
	public Scene viewBPStatement(Statement statement, ClientProxy client, BPApplication application)
	{
		currentNode = statement;
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

	}

	@FXML
	void goHome(ActionEvent event)
	{

	}

	@FXML
	void goUpALevel(ActionEvent event)
	{

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
