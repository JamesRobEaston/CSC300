package guiFiles.screens;

import clientServerPackage.*;
import businessPlanClasses.*;
import guiFiles.BPApplication;
import guiFiles.PopupBoxes.*;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.*;

import javafx.scene.paint.*;

import java.rmi.RemoteException;
import java.util.*;

public class ViewBPScreen
{
	Scene viewBPScreen;
	BP businessPlan;
	Statement currentNode;
	boolean needsToBeSaved;
	
	public ViewBPScreen(BP bp, ClientProxy client, BPApplication application)
	{
		businessPlan = bp;
		needsToBeSaved = false;
		application.notify(viewBPStatement(bp.getTree().getRoot(), client, application));
	}
	
	public Scene viewBPStatement(Statement statement, ClientProxy client, BPApplication application)
	{
		currentNode = statement;
		
		//Create the layout for the scene
		AnchorPane rootNode = new AnchorPane();
		
			//Create the top-left corner of the screen
			VBox nameAndCategoryBox = new VBox(10);
			
			if(statement.getParent() != null)
			{
				//Create the button to go up a level
				Button backButton = new Button("Go up a level");
				backButton.setOnAction(e ->
				{
					if(statement.getParent() != null)
					{
						application.notify(viewBPStatement(statement.getParent(), client, application));
					}
				});
				nameAndCategoryBox.getChildren().add(backButton);
			}
			
			//Create a VBox to display the statement's category and name.
			Label nameLabel = new Label("Name: " + statement.getId());
			Label categoryLabel = new Label("Category: " + statement.getType().getName());
			
			nameAndCategoryBox.getChildren().addAll(categoryLabel, nameLabel);
			
			//Create an HBox for the top-left corner of the screen
			HBox topLeftCorner = new HBox(20);
			topLeftCorner.getChildren().addAll(nameAndCategoryBox);
			
		
			
			
			//Create the top-right corner of the screen
			HBox topRightCorner = new HBox(10);
			
			if(statement.getParent() != null && businessPlan.isEditable())
			{
				Button deleteButton = new Button("Delete");
				deleteButton.setOnAction(e -> 
				{
					if(statement.getParent() != null)
					{
						int indexOfThis = -1;
						Statement parent = statement.getParent();
						for(int i = 0; i < parent.getChildren().size(); i++)
						{
							Statement child = parent.getChildren().get(i);
							if(child.getId().equals(statement.getId()))
							{
								indexOfThis = i;
								break;
							}
						}
						
						parent.getChildren().remove(indexOfThis);
	
						needsToBeSaved = true;
						application.notify(viewBPStatement(statement.getParent(), client, application));
					}
					else
					{
						client.delete(businessPlan);
					}
				});
				topRightCorner.getChildren().add(deleteButton);
			}
			
			Button homeButton = new Button("Home");
			homeButton.setOnAction(e ->
			{
				if(businessPlan.isEditable() && needsToBeSaved)
				{
					SaveBPPopupBox.show(businessPlan);
				}
				else
				{
					application.notify(HomeScreen.homeScreen);
				}
			});
			topRightCorner.getChildren().add(homeButton);

			Label invalidSaveLabel = new Label();
			
			if(businessPlan.isEditable() || client.isAdmin())
			{
				Button saveButton = new Button("Save");
				saveButton.setOnAction(e ->
				{
					client.setBusinessPlan(businessPlan);
					client.saveLocalPlanXML();
					needsToBeSaved = false;
				});
				
				
				Button saveToServerButton = new Button("Save to Server");
				saveToServerButton.setOnAction(e ->
				{
					client.setBusinessPlan(businessPlan);
					try
					{
						if(client.isAdmin())
						{
							client.saveBPToDepartment(businessPlan, ViewAllScreen.currDepartment.getDepartmentName());
						}
						else
						{
							client.submitPlan();
						}
						new ViewAllScreen(client, application);
						needsToBeSaved = false;
						invalidSaveLabel.setText("");
						
					} catch (Exception e1)
					{
						invalidSaveLabel.setText("Saving to the server failed.");
						e1.printStackTrace();
					}
				});
				
				topRightCorner.getChildren().addAll(saveButton, saveToServerButton);
			}
			
			if(client.isAdmin())
			{
				Label isEditableLabel = new Label("Editable");
				CheckBox isEditableCheckBox = new CheckBox();
				if(businessPlan.isEditable())
				{
					isEditableCheckBox.fire();
				}
				isEditableCheckBox.setOnAction(e ->
				{
					needsToBeSaved = true;
					businessPlan.setEditable(isEditableCheckBox.isSelected());
					new ViewBPScreen(businessPlan, client, application);
				});
				
				HBox editableNode = new HBox(10);
				editableNode.getChildren().addAll(isEditableLabel, isEditableCheckBox);
				
				topRightCorner.getChildren().add(editableNode);
			}
			else
			{
				Label isEditable = new Label();
				if(businessPlan.isEditable())
				{
					isEditable.setText("Is Editable");
				}
				else
				{
					isEditable.setText("Is Not Editable");
				}
				topRightCorner.getChildren().add(isEditable);
			}
			
			topRightCorner.getChildren().addAll(invalidSaveLabel);
			topRightCorner.setAlignment(Pos.CENTER_RIGHT);

			
			
			
			VBox centerNode = new VBox(10);
			
			//Create a scrollPane to display the data in the statement
			ScrollPane statementsNode = new ScrollPane();
			statementsNode.setPrefViewportWidth(300);
			statementsNode.setPrefViewportHeight(400);
			
			VBox statementsBox = new VBox(10);
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
						statementsBox.getChildren().remove(statementPane);
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
					
					statementsBox.getChildren().add(statementPane);
				}
			}
			
			//Add a button to add new data to this node
			if(businessPlan.isEditable())
			{
				Button newStatementButton = new Button("Add New Statement");
				newStatementButton.setOnAction(e ->
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
								statementsBox.getChildren().remove(statementPane);
								for(int i = 0; i < statement.getData().size(); i++)
								{
									String data = statement.getData().get(i);
									if(data.equals(newStatementInput.getText()))
									{
										statement.removeData(i);
										break;
									}
								}
							});
							
							statementPane.getChildren().add(deleteStatementButton);
						}
						
						statement.addData(newStatementInput.getText());
						statementsBox.getChildren().remove(newStatementInput);
						statementsBox.getChildren().remove(submitButton);
						statementsBox.getChildren().addAll(statementPane, newStatementButton);
					});
					statementsBox.getChildren().remove(newStatementButton);
					statementsBox.getChildren().addAll(newStatementInput, submitButton);
				});
				statementsBox.getChildren().add(newStatementButton);
			}
			
			statementsNode.setContent(statementsBox);
			
			centerNode.getChildren().add(statementsNode);
			
			//Display the statement's children
			businessPlan.getDesign().orderCategories();
			ArrayList<Category> categoryList = businessPlan.getDesign().getCategoryList();
			boolean isLowestCategory = statement.getType().getName().equals(categoryList.get(categoryList.size()-1).getName());
			
			if(!isLowestCategory)
			{
				String nextCategory = categoryList.get(categoryList.indexOf(statement.getType()) + 1).getName();
				Label nextCategoryLabel = new Label(nextCategory);
				
				ScrollPane childrenNode = new ScrollPane();
				childrenNode.setPrefViewportWidth(300);
				childrenNode.setPrefViewportHeight(400);
				
				TilePane children = new TilePane();
				for(Statement childStatement: statement.getChildren())
				{
					Button chooseStatementButton = new Button(childStatement.getId());
					chooseStatementButton.setOnAction(e ->
					{
						application.notify(viewBPStatement(childStatement, client, application));
					});
					children.getChildren().add(chooseStatementButton);
				}
				if(businessPlan.isEditable())
				{
					Button newChildButton = new Button("Create New");
					newChildButton.setOnAction(e ->
					{
						needsToBeSaved = true;
						TextField newChildIDInput = new TextField();
						newChildIDInput.setPromptText("Enter a new ID");
						Button submitButton = new Button("Submit");
						submitButton.setOnAction(e1 ->
						{
							ArrayList<Category> categories = businessPlan.getDesign().getCategoryList();
							Category newChildCategory = categories.get(categories.indexOf(statement.getType()) + 1);
							Statement newChild = new Statement(newChildCategory, statement, new ArrayList<String>(), newChildIDInput.getText());
							statement.addChild(newChild);
							application.notify(viewBPStatement(newChild, client, application));
						});
						children.getChildren().remove(newChildButton);
						children.getChildren().addAll(newChildIDInput, submitButton);
					});
					children.getChildren().add(newChildButton);
				}
				childrenNode.setContent(children);
				
				centerNode.getChildren().addAll(nextCategoryLabel, childrenNode);
			}
			
		//Anchor everything to the appropriate location
		AnchorPane.setTopAnchor(topLeftCorner, 20.0);
		AnchorPane.setLeftAnchor(topLeftCorner, 20.0);
		
		AnchorPane.setTopAnchor(topRightCorner, 20.0);
		AnchorPane.setRightAnchor(topRightCorner, 20.0);
		
		AnchorPane.setTopAnchor(centerNode, 100.0);
		AnchorPane.setLeftAnchor(centerNode, 100.0);
		AnchorPane.setRightAnchor(centerNode, 100.0);
		AnchorPane.setBottomAnchor(centerNode, 100.0);
		
		rootNode.getChildren().addAll(topLeftCorner, topRightCorner, centerNode);
			
		viewBPScreen = new Scene(rootNode);
		return viewBPScreen;
	}
	
	
}
