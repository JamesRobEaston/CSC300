package viewAllBPView;

import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import applicationFiles.DepartmentConverter;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import applicationFiles.BPApplication;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import model.Model;
import model.ModelInterface;
import editOrClonePopup.EditOrClonePopupBoxController;
import homePage.homePageController;

public class ViewAllBPScreenController
{

	@FXML
	private Button backButton;

	@FXML
	public ChoiceBox<Department> departmentDropDownMenu;

	@FXML
	private TextField yearTextInput;

	@FXML
	private TextField idTextInput;

    @FXML
    private ScrollPane bpScrollPane;

    @FXML
    public FlowPane bpFlowPane;
    
    public Scene viewAllScreen;
	
	String[][] businessPlans;
	public ArrayList<String[]> validPlans;
	Button resetButton;
	
	public static Department currDepartment;

	ModelInterface model;
    
	public ViewAllBPScreenController() {}
	
	public ViewAllBPScreenController(ModelInterface model)
	{

		this.model = model;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewAllBPScreenController.class.getResource("viewAllBPView/ViewAllBPScreen.fxml"));
		
		try
		{
			viewAllScreen = new Scene(loader.load());
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		currDepartment = model.getDepartment();
		
		//Copy allPlans into a new list of plans that will be displayed
		setValidPlans(model.getClient(), model.getDepartment());
		
		boolean isAdmin = model.isAdmin();
		
		if(isAdmin)
		{
			ArrayList<Department> departments = model.getAllDepartments();
			for(int i = 0; i < departments.size(); i++)
			{
				departmentDropDownMenu.getItems().add(departments.get(i));
			}
			departmentDropDownMenu.setConverter(new DepartmentConverter<Department>());
			
			departmentDropDownMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) ->
			{
				currDepartment = departmentDropDownMenu.getItems().get(newIndex.intValue());
				setValidPlans(model.getClient(), currDepartment);
				updateBPScrollPane(model.getClient(), validPlans);
			});
			
			departmentDropDownMenu.setValue(ConcreteServer.adminDepartment);
		}
		else
		{
			departmentDropDownMenu.setVisible(false);
		}
		
		updateBPScrollPane(model.getClient(), validPlans);
	}
	
	public void setModel(ModelInterface model)
	{
		this.model = model;
	}

	@FXML
	void goBack(ActionEvent event)
	{
		model.showHome();
	}

	@FXML
	void newBP(ActionEvent event)
	{
		model.showNewBPPopupBox(false);
	}

	@FXML
	void resetBPs(ActionEvent event)
	{
		validPlans.clear();
		
		for(String[] bp : businessPlans)
		{
			validPlans.add(bp);
		}
		
		yearTextInput.setText("");
		idTextInput.setText("");
		
		updateBPScrollPane(model.getClient(), validPlans);
	}

	@FXML
	void searchThroughBPs(ActionEvent event)
	{
		//Search by year
		String year = yearTextInput.getText();
		if(year != "");
		{
			String[] bp;
			for(int i = 0; i < validPlans.size(); i++)
			{
				bp = validPlans.get(i);
				if(!bp[2].contains(year))
				{
					validPlans.remove(bp);
					i--;
				}
				
			}
		}
		
		//Search by name
		String name = idTextInput.getText();
		if(name != "");
		{
			String[] bp;
			for(int i = 0; i < validPlans.size(); i++)
			{
				bp = validPlans.get(i);
				if(!bp[0].contains(name))
				{
					validPlans.remove(bp);
					i--;
				}
				
			}
		}
		
		updateBPScrollPane(model.getClient(), validPlans);
	}
	
	public void setValidPlans(ClientProxy client, Department dept)
	{
		validPlans = new ArrayList<String[]>();
		String[][] bps = new String[0][0];
		
		if(!client.isAdmin())
		{
			//Get all of the Business Plans
			bps = client.view();
		}
		else
		{
			bps = client.view(dept);
			if(bps == null)
			{
				bps = new String[0][0];
			}
		}

		for(String[] bp : bps)
		{
			validPlans.add(bp);
		}
		
		businessPlans = bps;
		
	}
	
	public void updateBPScrollPane(ClientProxy client, ArrayList<String[]> validPlans)
	{
		FlowPane rootNode = new FlowPane();
		for(String[] bp : validPlans)
		{	
			//Create the Button
			Button bpButton = new Button(bp[0]);
			bpButton.setOnAction(e ->
			{
				if(client.isAdmin())
				{
					try
					{
						client.retrieve(bp[0], Model.currDepartment);
					} catch (RemoteException e1)
					{
						e1.printStackTrace();
					}
				}
				else
				{
					try
					{
						client.retrieve(bp[0]);
					} catch (RemoteException e1)
					{
						e1.printStackTrace();
					}
				}
				model.setBusinessPlan(client.getLocalCopy());
				model.showEditOrClone();
			});

			bpButton.getStyleClass().add("bpButton");
			StackPane buttonMargins = new StackPane(bpButton);
			StackPane.setMargin(bpButton, new Insets(10, 10, 10, 10));
			rootNode.getChildren().add(buttonMargins);
		}
		
		if(Platform.isFxApplicationThread())
		{
			bpScrollPane.setContent(rootNode);
		}
		else
		{
			Platform.runLater(() ->
			{
				bpScrollPane.setContent(rootNode);
			});
		}
	}
	
	public void updateBPScrollPane(ClientProxy client)
	{
		updateBPScrollPane(client, validPlans);
	}

	public void updateDepartmentDropDownMenu()
	{
		ArrayList<Department> departments = model.getAllDepartments();
		if(Platform.isFxApplicationThread())
		{
			departmentDropDownMenu.getItems().clear();
			for(int i = 0; i < departments.size(); i++)
			{
				departmentDropDownMenu.getItems().add(departments.get(i));
			}
			departmentDropDownMenu.setConverter(new DepartmentConverter<Department>());
			
			departmentDropDownMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) ->
			{
				currDepartment = departmentDropDownMenu.getItems().get(newIndex.intValue());
				Model.currDepartment = currDepartment;
				setValidPlans(model.getClient(), currDepartment);
				updateBPScrollPane(model.getClient(), validPlans);
			});
		}
		else
		{
			Platform.runLater(() ->
			{
				departmentDropDownMenu.getItems().clear();
				for(int i = 0; i < departments.size(); i++)
				{
					departmentDropDownMenu.getItems().add(departments.get(i));
				}
				departmentDropDownMenu.setConverter(new DepartmentConverter<Department>());
				
				departmentDropDownMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) ->
				{
					currDepartment = departmentDropDownMenu.getItems().get(newIndex.intValue());
					setValidPlans(model.getClient(), currDepartment);
					updateBPScrollPane(model.getClient(), validPlans);
				});
			});
		}
	}
}
