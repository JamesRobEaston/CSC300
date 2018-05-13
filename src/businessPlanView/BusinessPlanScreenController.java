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
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import model.Model;
import model.ModelInterface;

public class BusinessPlanScreenController
{

	@FXML
	public Button goUpALevelButton;

	@FXML
	public Label categoryLabel;

	@FXML
	public Label nameLabel;

	@FXML
	public VBox statementsNode;

	@FXML
	public Button addNewStatementButton;

	@FXML
	public Label subcategoryLabel;

	@FXML
	public Button createNewSubcategoryButton;

	@FXML
	public TilePane childrenNode;

	@FXML
	public VBox treeDisplay;

	@FXML
	public Button deleteButton;

	@FXML
	public ScrollPane childrenScrollPane;

	@FXML
	public Button homeButton;

	@FXML
	public Button saveButton;

	@FXML
	public Button saveToServerButton;

	@FXML
	public HBox isEditableHBox;

	@FXML
	public CheckBox isEditableCheckbox;

	@FXML
	public Label isEditableLabel;

	@FXML
	public Label invalidSaveLabel;

	@FXML
	public ImageView warningImage;

	@FXML
	private VBox commentsNode;

	@FXML
	private Button addNewCommentButton;

	@FXML
	private VBox statementsNode1;

	@FXML
	private Button addNewStatementButton1;

	@FXML
	private VBox commentsNode1;

	@FXML
	private Button addNewCommentButton1;

	Scene viewBPScreen;
	BP businessPlan;
	public Statement currentNode;
	public boolean needsToBeSaved;
	ModelInterface model;
	boolean showWarningImage;

	public BusinessPlanScreenController()
	{
	}

	public BusinessPlanScreenController(ModelInterface model)
	{
		this.model = model;
		businessPlan = model.getBusinessPlan();
		needsToBeSaved = false;
		viewBPScreen = viewBPStatement(businessPlan.getTree().getRoot());
	}

	public void setModel(ModelInterface model)
	{
		this.model = model;
		businessPlan = model.getBusinessPlan();
	}

	public Scene viewBPStatement(Statement statement)
	{
		currentNode = statement;

		nameLabel.setText("Name: " + currentNode.getId());
		categoryLabel.setText("Category: " + statement.getType().getName());

		boolean hasParent = !(currentNode.getParent() == null);
		boolean isEditable = businessPlan.isEditable();
		boolean isAdmin = model.isAdmin();

		// Determine whether the goUpALevel button is visible
		if (!hasParent)
		{
			goUpALevelButton.setVisible(false);
		} else
		{
			goUpALevelButton.setVisible(true);
		}

		// Determine if the delete button is visible

		if (hasParent && isEditable)
		{
			deleteButton.setVisible(true);
		} else
		{
			deleteButton.setVisible(false);
		}

		// Determine if the isEditable checkbox is visible
		if (isAdmin)
		{
			isEditableHBox.setVisible(true);
			isEditableLabel.setVisible(false);
		} else
		{
			isEditableHBox.setVisible(false);
			isEditableLabel.setVisible(true);
		}

		// Display whether or not the BP is editable
		if (businessPlan.isEditable())
		{
			isEditableCheckbox.fire();
			isEditableLabel.setText("Is Editable");
		} else
		{
			isEditableLabel.setText("Is Not Editable");
		}

		// Determine if the save buttons are visible
		if (isAdmin || isEditable)
		{
			saveButton.setVisible(true);
			saveToServerButton.setVisible(true);
		} else
		{
			saveButton.setVisible(false);
			saveToServerButton.setVisible(false);
		}

		// Populate statementsNode
		if (statement.getData() != null)
		{
			for (String dataStatement : statement.getData())
			{
				HBox statementPane = new HBox(20);
				Label statementLabel = new Label(dataStatement);

				Button deleteStatementButton = new Button("Delete");
				deleteStatementButton.setOnAction(e -> {
					needsToBeSaved = true;
					statementsNode.getChildren().remove(statementPane);
					for (int i = 0; i < statement.getData().size(); i++)
					{
						String data = statement.getData().get(i);
						if (data.equals(dataStatement))
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

		if (isEditable)
		{
			addNewStatementButton.setVisible(true);
		} else
		{
			addNewStatementButton.setVisible(false);
		}

		// Determine if the scroll pane that displays the child of this node should be
		// displayed
		businessPlan.getDesign().orderCategories();
		ArrayList<Category> categoryList = businessPlan.getDesign().getCategoryList();
		boolean isLowestCategory = statement.getType().getName()
				.equals(categoryList.get(categoryList.size() - 1).getName());

		if (isLowestCategory)
		{
			childrenScrollPane.setVisible(false);
			subcategoryLabel.setVisible(false);
		} else
		{
			childrenScrollPane.setVisible(true);
			subcategoryLabel.setVisible(true);

			String nextCategory = categoryList.get(categoryList.indexOf(statement.getType()) + 1).getName();
			subcategoryLabel.setText(nextCategory);
		}

		// Populate childrenNode
		for (Statement childStatement : statement.getChildren())
		{
			Button chooseStatementButton = new Button(childStatement.getId());
			chooseStatementButton.setOnAction(e -> {
				model.notify(viewBPStatement(childStatement));
			});
			childrenNode.getChildren().remove(createNewSubcategoryButton);
			childrenNode.getChildren().add(chooseStatementButton);
			childrenNode.getChildren().add(createNewSubcategoryButton);
		}

		return viewBPScreen;
	}

	void addNewComment(VBox currNode, Button currButton, VBox otherNode, Button otherButton)
	{
		TextField newCommentInput = new TextField();
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e1 -> {
			needsToBeSaved = true;
			HBox currCommentPane = new HBox(20);
			HBox otherCommentPane = new HBox(20);

			Label commentLabel = new Label(newCommentInput.getText());
			Label otherCommentLabel = new Label(newCommentInput.getText());
			
			currCommentPane.getChildren().addAll(commentLabel);
			otherCommentPane.getChildren().addAll(otherCommentLabel);

			if (businessPlan.isEditable())
			{
				Button deleteCommentButton = new Button("Delete");
				deleteCommentButton.setOnAction(e2 -> {
					needsToBeSaved = true;
					currNode.getChildren().remove(currCommentPane);
					otherNode.getChildren().remove(otherCommentPane);
					for (int i = 0; i < currentNode.getData().size(); i++)
					{
						String data = currentNode.getData().get(i);
						if (data.equals(newCommentInput.getText()))
						{
							currentNode.removeData(i);
							break;
						}
					}
				});
				
				Button otherDeleteCommentButton = new Button("Delete");
				otherDeleteCommentButton.setOnAction(e2 -> {
					needsToBeSaved = true;
					currNode.getChildren().remove(currCommentPane);
					otherNode.getChildren().remove(otherCommentPane);
					for (int i = 0; i < currentNode.getData().size(); i++)
					{
						String data = currentNode.getData().get(i);
						if (data.equals(newCommentInput.getText()))
						{
							currentNode.removeData(i);
							break;
						}
					}
				});

				currCommentPane.getChildren().add(deleteCommentButton);
				otherCommentPane.getChildren().add(otherDeleteCommentButton);
			}

			currentNode.addData(newCommentInput.getText());
			currNode.getChildren().remove(newCommentInput);
			currNode.getChildren().remove(submitButton);
			currNode.getChildren().add(currCommentPane);
			currNode.getChildren().add(currButton);
			otherNode.getChildren().remove(otherButton);
			otherNode.getChildren().add(otherCommentPane);
			otherNode.getChildren().add(otherButton);
		});

		currNode.getChildren().remove(currButton);
		currNode.getChildren().addAll(newCommentInput, submitButton);
	}

	@FXML
	void addNewCommentToMain(ActionEvent event)
	{
		addNewComment(commentsNode, addNewCommentButton, commentsNode1, addNewCommentButton1);
	}

	@FXML
	void addNewCommentToSBS(ActionEvent event)
	{
		addNewComment(commentsNode1, addNewCommentButton1, commentsNode, addNewCommentButton);
	}

	@FXML
	void addNewStatementToMain(ActionEvent event)
	{
		addNewStatement(statementsNode, addNewStatementButton, statementsNode1, addNewStatementButton1);
	}

	@FXML
	void addNewStatementToSBS(ActionEvent event)
	{
		addNewStatement(statementsNode1, addNewStatementButton1, statementsNode, addNewStatementButton);
	}

	void addNewStatement(VBox currNode, Button currButton, VBox otherNode, Button otherButton)
	{
		TextField newStatementInput = new TextField();
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e1 -> {
			needsToBeSaved = true;
			HBox currStatementPane = new HBox(20);
			HBox otherStatementPane = new HBox(20);

			Label statementLabel = new Label(newStatementInput.getText());
			Label otherStatementLabel = new Label(newStatementInput.getText());
			
			currStatementPane.getChildren().addAll(statementLabel);
			otherStatementPane.getChildren().addAll(otherStatementLabel);

			if (businessPlan.isEditable())
			{
				Button deleteStatementButton = new Button("Delete");
				deleteStatementButton.setOnAction(e2 -> {
					needsToBeSaved = true;
					currNode.getChildren().remove(currStatementPane);
					otherNode.getChildren().remove(otherStatementPane);
					for (int i = 0; i < currentNode.getData().size(); i++)
					{
						String data = currentNode.getData().get(i);
						if (data.equals(newStatementInput.getText()))
						{
							currentNode.removeData(i);
							break;
						}
					}
				});
				
				Button otherDeleteStatementButton = new Button("Delete");
				otherDeleteStatementButton.setOnAction(e2 -> {
					needsToBeSaved = true;
					currNode.getChildren().remove(currStatementPane);
					otherNode.getChildren().remove(otherStatementPane);
					for (int i = 0; i < currentNode.getData().size(); i++)
					{
						String data = currentNode.getData().get(i);
						if (data.equals(newStatementInput.getText()))
						{
							currentNode.removeData(i);
							break;
						}
					}
				});

				currStatementPane.getChildren().add(deleteStatementButton);
				otherStatementPane.getChildren().add(otherDeleteStatementButton);
			}

			currentNode.addData(newStatementInput.getText());
			currNode.getChildren().remove(newStatementInput);
			currNode.getChildren().remove(submitButton);
			currNode.getChildren().add(currStatementPane);
			currNode.getChildren().add(currButton);
			otherNode.getChildren().remove(otherButton);
			otherNode.getChildren().add(otherStatementPane);
			otherNode.getChildren().add(otherButton);
		});

		currNode.getChildren().remove(currButton);
		currNode.getChildren().addAll(newStatementInput, submitButton);
	}

	@FXML
	void changeEditability(ActionEvent event)
	{
		needsToBeSaved = true;
		businessPlan.setEditable(isEditableCheckbox.isSelected());
	}

	@FXML
	void createNewSubcategory(ActionEvent event)
	{
		needsToBeSaved = true;
		TextField newChildIDInput = new TextField();
		newChildIDInput.setPromptText("Enter a new ID");
		Button submitButton = new Button("Submit");
		submitButton.setOnAction(e1 -> {
			ArrayList<Category> categories = model.getBusinessPlan().getDesign().getCategoryList();
			Category newChildCategory = categories.get(categories.indexOf(currentNode.getType()) + 1);
			Statement newChild = new Statement(newChildCategory, currentNode, new ArrayList<String>(),
												new ArrayList<String>(), newChildIDInput.getText());
			currentNode.addChild(newChild);
			model.showBusinessPlanScreen(newChild);
		});
		childrenNode.getChildren().remove(createNewSubcategoryButton);
		childrenNode.getChildren().addAll(newChildIDInput, submitButton);
	}

	@FXML
	void deleteNode(ActionEvent event)
	{
		if (currentNode.getParent() != null)
		{
			int indexOfThis = -1;
			Statement parent = currentNode.getParent();
			for (int i = 0; i < parent.getChildren().size(); i++)
			{
				Statement child = parent.getChildren().get(i);
				if (child.getId().equals(currentNode.getId()))
				{
					indexOfThis = i;
					break;
				}
			}

			parent.getChildren().remove(indexOfThis);

			needsToBeSaved = true;
			model.showBusinessPlanScreen(currentNode.getParent());
		}
	}

	@FXML
	void goHome(ActionEvent event)
	{
		if (businessPlan.isEditable() && needsToBeSaved)
		{
			model.showSaveBPPopupBox(false);
		} else
		{
			model.showHome();
		}
	}

	@FXML
	void goUpALevel(ActionEvent event)
	{
		if (currentNode.getParent() != null)
		{
			model.showBusinessPlanScreen(currentNode.getParent());
		}
	}

	@FXML
	void saveLocally(ActionEvent event)
	{
		model.setBusinessPlan(businessPlan);
		model.saveLocalPlanXML();
		needsToBeSaved = false;
	}

	@FXML
	void saveToServer(ActionEvent event)
	{
		model.setBusinessPlan(businessPlan);
		try
		{
			if (model.isAdmin())
			{
				model.saveBPToDepartment(model.getBusinessPlan(), Model.currDepartment.getDepartmentName());
			} else
			{
				model.submitPlan();
			}
			needsToBeSaved = false;
			invalidSaveLabel.setText("");
			setWarningImageVisibility(false);

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

	// Shows the warning label
	public void setWarningImageVisibility(boolean visible)
	{
		showWarningImage = visible;
		warningImage.setVisible(visible);
	}

	// Method to display the warningImage if it needs to be
	public void showWarningImageIfAppropriate()
	{
		warningImage.setVisible(showWarningImage);
	}
}
