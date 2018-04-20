package model;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import addDepartmentPopup.AddDepartmentPopupBoxController;
import addUserPopup.AddNewUserPopupBoxController;
import applicationFiles.BPApplication;
import applicationFiles.DepartmentConverter;
import businessPlanClasses.Category;
import businessPlanClasses.Statement;
import businessPlanView.BusinessPlanScreenController;
import categoryPopupBox.CategoryPopupBoxController;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import clientServerPackage.ServerInterface;
import homePage.homePageController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import login.loginController;
import newBPPopup.NewBPPopupBoxController;
import saveBPPopup.SaveBPPopupBoxController;
import viewAllBPView.ViewAllBPScreenController;

public class Model 
{
	public ClientProxy client;
	BPApplication application;
	BP businessPlan;
	Department adminDepartment;
	public static Department currDepartment;
	
	public Model(ClientProxy client, BPApplication application, BP businessPlan)
	{
		this.client = client;
		this.application = application;
		this.businessPlan = businessPlan;
	}

	public boolean authenticate(TextField userName_input,PasswordField pass_input,TextField serv_input, Button login_but )
	{
		ServerInterface server = new ConcreteServer();
		
		userName_input.setText("admin");
		pass_input.setText("admin");
		serv_input.setText("1099");
		
		boolean foundServer = true;
		int serverAddress = Integer.parseInt(serv_input.getText());
		try 
		{
			Registry registry = LocateRegistry.getRegistry(serverAddress);
			server = (ServerInterface) registry.lookup("Server");
		} 
		catch (Exception exception)
		{
			foundServer = false;
			//invalidLoginLabel.setText("The server was not found.");
			exception.printStackTrace();
		}
		
		if(foundServer)
		{
			try
			{
				client = new ClientProxy(server);
				client.login(userName_input.getText(), pass_input.getText());
			}
			catch (Exception e1)
			{
				e1.printStackTrace();
			}
		
			//If the login failed, notify the user.
			if(client.getUserToken() == null)
			{
				return false;
				//invalidLoginLabel.setText("The username or password was not valid! Please try again.");
				//invalidLoginLabel.setAlignment(Pos.CENTER);
				
				
			}
			//Otherwise, create all screens and show the home screen on the application.
			else
			{
				//application.createAllStaticScreensAndPopupBoxes(client);
				return true;
			//	userName_input.setText("");
				//pass_input.setText("");
			}
		
		}
		return false;
		
	}

	public void notify(Scene scene)
	{
		application.notify(scene);
	}

	public boolean addDepartment(String departmentName)
	{
		return client.addDepartment(departmentName);
	}

	public ArrayList<Department> getAllDepartments()
	{
		return client.getAllDepartments();
	}

	public ClientProxy getClient()
	{
		return client;
	}

	public Department getDepartment()
	{
		return client.getDepartment();
	}

	public boolean isAdmin()
	{
		return client.isAdmin();
	}

	public BPApplication getApplication()
	{
		return application;
	}

	public void setApplication(BPApplication application)
	{
		this.application = application;
	}

	public BP getBusinessPlan()
	{
		return businessPlan;
	}

	public void setModelBusinessPlan(BP businessPlan)
	{
		this.businessPlan = businessPlan;
	}

	public Department getAdminDepartment()
	{
		return adminDepartment;
	}

	public void setAdminDepartment(Department adminDepartment)
	{
		this.adminDepartment = adminDepartment;
	}

	public void setClient(ClientProxy client)
	{
		this.client = client;
	}

	public void setLocalCopy(BP businessPlan2)
	{
		businessPlan = businessPlan2;
		client.setLocalCopy(businessPlan2);
	}
	
	public void setBusinessPlan(BP businessPlan2)
	{
		businessPlan = businessPlan2;
		client.setLocalCopy(businessPlan2);
	}

	public void saveLocalPlanXML()
	{
		client.saveLocalPlanXML();
	}

	public void submitPlan()
	{
		client.setLocalCopy(businessPlan);
		client.submitPlan();
	}

	public boolean addUser(String username, String password, String name, boolean b)
	{
		return client.addUser(username, password, name, b);
	}
	
	public void retrieve(String bpid)
	{
		client.retrieve(bpid);
		businessPlan = client.getLocalCopy();
	}
	
	public void showHome()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(homePageController.class.getResource("/homePage/HomePageView.fxml"));
		try
		{
			Scene homePage = new Scene(loader.load());
			homePageController cont = loader.getController();
			cont.setModel(this);
			notify(homePage);
		} catch (IOException e)
		{

			e.printStackTrace();
		}
	}
	
	public void showLogin()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(loginController.class.getResource("/login/LoginView.fxml"));
		try 
		{
			Scene loginScreen = new Scene(loader.load());
			loginController login = loader.getController();
			login.setModel(this);
			notify(loginScreen);
		}
		catch (Exception e) 
		{
			
			e.printStackTrace();
		}
	}
	
	public void showViewAllBPScreen()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewAllBPScreenController.class.getResource("/viewAllBPView/ViewAllBPScreen.fxml"));
		
		ViewAllBPScreenController cont = new ViewAllBPScreenController();
		
		try
		{
			Scene viewAllScreen = new Scene(loader.load());
			cont = loader.getController();
			cont.setModel(this);
			notify(viewAllScreen);
			
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		Department currDepartment = getDepartment();
		
		//Copy allPlans into a new list of plans that will be displayed
		cont.setValidPlans(getClient(), getDepartment());
		
		boolean isAdmin = isAdmin();
		final ChoiceBox<Department> departmentDropDownMenu = cont.departmentDropDownMenu;
		final ViewAllBPScreenController controller = cont;
		
		if(isAdmin)
		{
			ArrayList<Department> departments = getAllDepartments();
			for(int i = 0; i < departments.size(); i++)
			{
				departmentDropDownMenu.getItems().add(departments.get(i));
			}
			departmentDropDownMenu.setConverter(new DepartmentConverter<Department>());
			
			
			departmentDropDownMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) ->
			{
				Model.currDepartment = departmentDropDownMenu.getItems().get(newIndex.intValue());
				controller.setValidPlans(getClient(), Model.currDepartment);
				controller.updateBPScrollPane(getClient(), controller.validPlans);
			});
			
			cont.departmentDropDownMenu.setValue(ConcreteServer.adminDepartment);
		}
		else
		{
			cont.departmentDropDownMenu.setVisible(false);
		}
		
		cont.updateBPScrollPane(getClient(), cont.validPlans);
	}

	public void showBusinessPlanScreen(Statement statement)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(BusinessPlanScreenController.class.getResource("/businessPlanView/BusinessPlanScreen.fxml"));
		
		Scene viewBPScreen = new Scene(new AnchorPane());
		
		try
		{
			viewBPScreen = new Scene(loader.load());
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		BusinessPlanScreenController cont = loader.getController();
		cont.setModel(this);
		
		cont.currentNode = statement;
		
		cont.nameLabel.setText("Name: " + cont.currentNode.getId());
		cont.categoryLabel.setText("Category: " + statement.getType().getName());
		
		boolean hasParent = !(cont.currentNode.getParent() == null);
		boolean isEditable = businessPlan.isEditable();
		boolean isAdmin = isAdmin();
		
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
				showBusinessPlanScreen(childStatement);
			});
			cont.childrenNode.getChildren().remove(cont.createNewSubcategoryButton);
			cont.childrenNode.getChildren().add(chooseStatementButton);
			cont.childrenNode.getChildren().add(cont.createNewSubcategoryButton);
		}
		
		notify(viewBPScreen);
	}
	
	public void showBusinessPlanScreen()
	{
		showBusinessPlanScreen(businessPlan.getTree().getRoot());
	}
	
	Stage popupStage;
	
	public void closePopupBox()
	{
		popupStage.close();
	}
	
	public void showAddUserPopupBox()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AddNewUserPopupBoxController.class.getResource("/addUserPopup/AddUserPopupBox.fxml"));
		Scene newUserScene = new Scene(new AnchorPane());
		try
		{
			newUserScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		AddNewUserPopupBoxController cont = loader.getController();
		cont.setModel(this);

		ArrayList<Department> departments = getAllDepartments();
		cont.newDepartment.setConverter(new DepartmentConverter<Department>());

		for (Department department : departments)
		{
			if (!department.getName().equals("Admin"))
			{
				cont.newDepartment.getItems().add(department);
			} else
			{
				setAdminDepartment(department);
			}
		}

		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Add New User");
		popupStage.setResizable(false);
		popupStage.setScene(newUserScene);
		popupStage.show();
	}
	
	public void showAddDepartmentPopupBox()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AddDepartmentPopupBoxController.class.getResource("/addDepartmentPopup/AddDepartmentPopupBox.fxml"));
		Scene newDepartmentScene = new Scene(new AnchorPane());
		try
		{
			newDepartmentScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		AddDepartmentPopupBoxController cont = loader.getController();
		cont.setModel(this);
		
		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Add Department");
		popupStage.setResizable(false);
		popupStage.setScene(newDepartmentScene);
		popupStage.show();
	}
	
	public void showNewBPPopupBox(boolean isClone)
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(NewBPPopupBoxController.class.getResource("/newBPPopup/NewBPPopupBox.fxml"));
		Scene newBPScene = new Scene(new AnchorPane());
		try
		{
			newBPScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		NewBPPopupBoxController cont = loader.getController();
		cont.setIsClone(isClone);
		cont.setModel(this);
		
		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("New Business Plan");
		popupStage.setResizable(false);
		popupStage.setScene(newBPScene);
		popupStage.show();
	}
	
	public void showCategoryPopupBox()
	{	
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(CategoryPopupBoxController.class.getResource("/categoryPopupBox/CategoryPopupBox.fxml"));
		
		Scene newScene = new Scene(new AnchorPane());
		
		try
		{
			newScene = new Scene(loader.load());
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		CategoryPopupBoxController cont = loader.getController();
		cont.setModel(this);
		
		popupStage = new Stage();
		popupStage.setWidth(500.0);
		popupStage.setHeight(350.0);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Create Categories");
		popupStage.setResizable(false);
		popupStage.setScene(newScene);
		popupStage.show();
		
		cont.categoryInputs = new ArrayList<TextField>();
	}
	
	public void showSaveBPPopupBox()
	{
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
		
		SaveBPPopupBoxController cont = loader.getController();
		cont.setModel(this);
		
		popupStage = new Stage();
		popupStage.setWidth(500.0);
		popupStage.setHeight(350.0);
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("");
		popupStage.setResizable(false);
		popupStage.setScene(newScene);
		popupStage.show();		
				
	}

	public void loadLocalCopy()
	{
		client.readLocalPlanXML();
		businessPlan = client.getLocalCopy();
	}

	public void saveBPToDepartment(BP businessPlan2, String departmentName)
	{
		try
		{
			client.saveBPToDepartment(businessPlan2, departmentName);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

}
