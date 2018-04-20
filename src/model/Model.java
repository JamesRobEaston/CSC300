package model;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

import applicationFiles.BPApplication;
import clientServerPackage.BP;
import clientServerPackage.ClientProxy;
import clientServerPackage.ConcreteServer;
import clientServerPackage.Department;
import clientServerPackage.ServerInterface;
import homePage.homePageController;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class Model 
{
	public ClientProxy client;
	BPApplication application;
	BP businessPlan;
	Department adminDepartment;
	
	public Model(ClientProxy client, BPApplication application, BP businessPlan)
	{
		this.client = client;
		this.application = application;
		this.businessPlan = businessPlan;
	}

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
				homePageController homePage = new homePageController();
				application.notify(homePage.getScene());
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
		client.setLocalCopy(businessPlan2);
	}
	
	public void setBusinessPlan(BP businessPlan2)
	{
		client.setLocalCopy(businessPlan2);
	}

	public void saveLocalPlanXML()
	{
		client.saveLocalPlanXML();
	}

	

}
