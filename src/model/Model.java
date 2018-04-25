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
import editOrClonePopup.EditOrClonePopupBoxController;
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

public class Model implements ModelInterface 
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

	/* (non-Javadoc)
	 * @see model.ModelInterface#authenticate(javafx.scene.control.TextField, javafx.scene.control.PasswordField, javafx.scene.control.TextField, javafx.scene.control.Button)
	 */
	@Override
	public boolean authenticate(TextField userName_input,PasswordField pass_input,TextField serv_input, Button login_but )
	{
		ServerInterface server = new ConcreteServer();
		
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

	/* (non-Javadoc)
	 * @see model.ModelInterface#notify(javafx.scene.Scene)
	 */
	@Override
	public void notify(Scene scene)
	{
		application.notify(scene);
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#addDepartment(java.lang.String)
	 */
	@Override
	public boolean addDepartment(String departmentName)
	{
		return client.addDepartment(departmentName);
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#getAllDepartments()
	 */
	@Override
	public ArrayList<Department> getAllDepartments()
	{
		return client.getAllDepartments();
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#getClient()
	 */
	@Override
	public ClientProxy getClient()
	{
		return client;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#getDepartment()
	 */
	@Override
	public Department getDepartment()
	{
		return client.getDepartment();
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#isAdmin()
	 */
	@Override
	public boolean isAdmin()
	{
		return client.isAdmin();
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#getApplication()
	 */
	@Override
	public BPApplication getApplication()
	{
		return application;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#setApplication(applicationFiles.BPApplication)
	 */
	@Override
	public void setApplication(BPApplication application)
	{
		this.application = application;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#getBusinessPlan()
	 */
	@Override
	public BP getBusinessPlan()
	{
		return businessPlan;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#setModelBusinessPlan(clientServerPackage.BP)
	 */
	@Override
	public void setModelBusinessPlan(BP businessPlan)
	{
		this.businessPlan = businessPlan;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#getAdminDepartment()
	 */
	@Override
	public Department getAdminDepartment()
	{
		return adminDepartment;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#setAdminDepartment(clientServerPackage.Department)
	 */
	@Override
	public void setAdminDepartment(Department adminDepartment)
	{
		this.adminDepartment = adminDepartment;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#setClient(clientServerPackage.ClientProxy)
	 */
	@Override
	public void setClient(ClientProxy client)
	{
		this.client = client;
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#setLocalCopy(clientServerPackage.BP)
	 */
	@Override
	public void setLocalCopy(BP businessPlan2)
	{
		businessPlan = businessPlan2;
		client.setLocalCopy(businessPlan2);
	}
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#setBusinessPlan(clientServerPackage.BP)
	 */
	@Override
	public void setBusinessPlan(BP businessPlan2)
	{
		businessPlan = businessPlan2;
		client.setLocalCopy(businessPlan2);
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#saveLocalPlanXML()
	 */
	@Override
	public void saveLocalPlanXML()
	{
		client.saveLocalPlanXML();
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#submitPlan()
	 */
	@Override
	public void submitPlan()
	{
		client.setLocalCopy(businessPlan);
		client.submitPlan();
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#addUser(java.lang.String, java.lang.String, java.lang.String, boolean)
	 */
	@Override
	public boolean addUser(String username, String password, String name, boolean b)
	{
		return client.addUser(username, password, name, b);
	}
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#retrieve(java.lang.String)
	 */
	@Override
	public void retrieve(String bpid)
	{
		client.retrieve(bpid);
		businessPlan = client.getLocalCopy();
	}
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showHome()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showLogin()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showViewAllBPScreen()
	 */
	@Override
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
		
		currDepartment = getDepartment();
		
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

	/* (non-Javadoc)
	 * @see model.ModelInterface#showBusinessPlanScreen(businessPlanClasses.Statement)
	 */
	@Override
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
		
		Statement curr = statement;
		while(curr != null)
		{
			cont.treeDisplay.getChildren().add(0, new Label(">" + curr.getId()));
			curr = curr.getParent();
		}
			
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showBusinessPlanScreen()
	 */
	@Override
	public void showBusinessPlanScreen()
	{
		showBusinessPlanScreen(businessPlan.getTree().getRoot());
	}
	
	Stage popupStage;
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#closePopupBox()
	 */
	@Override
	public void closePopupBox()
	{
		popupStage.close();
	}
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showAddUserPopupBox()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showAddDepartmentPopupBox()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showNewBPPopupBox(boolean)
	 */
	@Override
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
		
		if(!client.isAdmin())
		{
			cont.departmentChoiceBox.setVisible(false);
			cont.departmentChoiceBoxLabel.setVisible(false);
		}
		else
		{
			ArrayList<Department> departments = getAllDepartments();
			for(int i = 0; i < departments.size(); i++)
			{
				cont.departmentChoiceBox.getItems().add(departments.get(i));
			}
			cont.departmentChoiceBox.setConverter(new DepartmentConverter<Department>());
			
			cont.departmentChoiceBox.setValue(client.getDepartment());
			
			cont.department = client.getDepartment().getName();
		}
		
		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("New Business Plan");
		popupStage.setResizable(false);
		popupStage.setScene(newBPScene);
		popupStage.show();
	}
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showCategoryPopupBox()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showSaveBPPopupBox()
	 */
	@Override
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
	
	/* (non-Javadoc)
	 * @see model.ModelInterface#showEditOrClone()
	 */
	@Override
	public void showEditOrClone()
	{
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(EditOrClonePopupBoxController.class.getResource("/editOrClonePopup/EditOrClonePopupBox.fxml"));
		Scene newScene = new Scene(new AnchorPane());
		try
		{
			newScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		
		EditOrClonePopupBoxController cont = loader.getController();
		cont.setModel(this);

		popupStage = new Stage();
		popupStage.initModality(Modality.APPLICATION_MODAL);
		popupStage.setTitle("Edit or Clone Business Plan");
		popupStage.setResizable(false);
		popupStage.setScene(newScene);
		popupStage.show();
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#loadLocalCopy()
	 */
	@Override
	public void loadLocalCopy()
	{
		client.readLocalPlanXML();
		businessPlan = client.getLocalCopy();
		if(businessPlan != null)
		{
			ArrayList<Department> departments = client.getAllDepartments();
			for(int i = 0; i < departments.size(); i++)
			{
				if(businessPlan.getDepartment().equals(departments.get(i).getName()));
				{
					currDepartment = departments.get(i);
					break;
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see model.ModelInterface#saveBPToDepartment(clientServerPackage.BP, java.lang.String)
	 */
	@Override
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
