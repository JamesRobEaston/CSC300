package viewAllBPView;

import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import applicationFiles.DepartmentConverter;

import java.io.IOException;
import java.util.ArrayList;

import applicationFiles.BPApplication;
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
import editOrClonePopup.EditOrCloneBPPopupBoxController;

public class ViewAllBPScreenController
{

	@FXML
	private Button backButton;

	@FXML
	private ChoiceBox<Department> departmentDropDownMenu;

	@FXML
	private TextField yearTextInput;

	@FXML
	private TextField idTextInput;

    @FXML
    private ScrollPane bpScrollPane;

    @FXML
    private FlowPane bpFlowPane;
    
    public Scene viewAllScreen;
	
	String[][] businessPlans;
	ArrayList<String[]> validPlans;
	Button resetButton;
	
	public static Department currDepartment;
	
	ClientProxy client;
	BPApplication application;
    
	public ViewAllBPScreenController() {}
	
	public ViewAllBPScreenController(ClientProxy client, BPApplication application)
	{
		this.client = client;
		this.application = application;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ViewAllBPScreenController.class.getResource("viewAllBPView/BusinessPlanScreen.fxml"));
		
		try
		{
			viewAllScreen = new Scene(loader.load());
		} catch (IOException e1)
		{
			e1.printStackTrace();
		}
		
		currDepartment = client.getDepartment();
		
		//Copy allPlans into a new list of plans that will be displayed
		setValidPlans(client, client.getDepartment());
		
		boolean isAdmin = client.isAdmin();
		
		if(isAdmin)
		{
			ArrayList<Department> departments = client.getAllDepartments();
			for(int i = 0; i < departments.size(); i++)
			{
				departmentDropDownMenu.getItems().add(departments.get(i));
			}
			departmentDropDownMenu.setConverter(new DepartmentConverter<Department>());
			
			departmentDropDownMenu.getSelectionModel().selectedIndexProperty().addListener((observableValue, oldIndex, newIndex) ->
			{
				currDepartment = departmentDropDownMenu.getItems().get(newIndex.intValue());
				setValidPlans(client, currDepartment);
				updateBPScrollPane(client, validPlans);
			});
			
			departmentDropDownMenu.setValue(ConcreteServer.adminDepartment);
		}
		else
		{
			departmentDropDownMenu.setVisible(false);
		}
		
		updateBPScrollPane(client, validPlans);
	}

	@FXML
	void goBack(ActionEvent event)
	{
		//TODO
		//application.notify(HomeScreen.homeScreen);
		resetButton.fire();
	}

	@FXML
	void newBP(ActionEvent event)
	{
		//TODO
		//new NewBPPopupBox(client, application, false);
		//NewBPPopupBox.newBPPopupBox.show();
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
		
		updateBPScrollPane(client, validPlans);
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
				}
				
			}
		}
		
		updateBPScrollPane(client, validPlans);
	}
	
	void setValidPlans(ClientProxy client, Department dept)
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
	
	void updateBPScrollPane(ClientProxy client, ArrayList<String[]> validPlans)
	{
		bpScrollPane.setContent(null);
		FlowPane rootNode = new FlowPane();
		for(String[] bp : validPlans)
		{	
			//Create the Button
			Button bpButton = new Button(bp[0]);
			bpButton.setOnAction(e ->
			{
				if(client.isAdmin())
				{
					client.retrieve(bp[0], currDepartment);
				}
				else
				{
					client.retrieve(bp[0]);
				}
				
				EditOrCloneBPPopupBoxController editOrClonePopupBox = new EditOrCloneBPPopupBoxController(model);
				.show(client.getLocalCopy());
			});

			StackPane buttonMargins = new StackPane(bpButton);
			StackPane.setMargin(bpButton, new Insets(10, 10, 10, 10));
			rootNode.getChildren().add(buttonMargins);
			bpScrollPane.setContent(rootNode);
		}
	}

}
