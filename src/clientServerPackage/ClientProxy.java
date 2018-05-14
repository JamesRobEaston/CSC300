package clientServerPackage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

import addUserPopup.AddNewUserPopupBoxController;
import businessPlanView.BusinessPlanScreenController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Model;
import model.ModelInterface;
import model.StaticModelAccessor;

public class ClientProxy implements ClientProxyInterface
{
	
	private static final long serialVersionUID = 5242747310111867431L;
	ServerInterface stub;
	String userToken;
	BP localCopy;
	transient Model model;
	ConcreteBPChangeObserver changeObserver;
	BPChangeObserver changeStub;

	public ClientProxy(ServerInterface server)
	{
		this.stub = server;
		changeObserver = new ConcreteBPChangeObserver();
		changeObserver.setClient(this);
		changeObserver.setUserToken(userToken);
		try
		{
			changeStub = (BPChangeObserver) UnicastRemoteObject.exportObject(changeObserver, 0);
		} catch (RemoteException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ClientProxy() { }

	//Sets the userToken to be equal to the userToken generated for whichever user has a matching username and password.
	//IMPORTANT: None of the following methods will work if userToken is not associated with a user on the Server.
	//			 Thus, this method must be called before anything can be done.
	//
	//As a side note, whenever the "current User" is referenced in comments, we mean the User with a matching userToken.
	public void login(String username, String password)
	{
		try
		{
			userToken = stub.authenticate(username, password);
			changeObserver.setUserToken(userToken);
		} catch (RemoteException e)
		{
			System.out.println("Login failed.");
		}
	}

	//Sets localCopy to be whatever BusinessPlan has an ID matching bpid.
	//If an external application controlling Client wants localCopy, they should call
	//getLocalCopy() after calling retrieve(), with an appropriate bpid().
	public void retrieve(String bpid) throws RemoteException
	{
		try
		{
			localCopy = stub.retrieve(userToken, bpid);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		subscribeToBP();
	}
	
	public void retrieve(String bpid, Department dept) throws RemoteException
	{
		
		try
		{
			localCopy = stub.retrieve(userToken, bpid, dept);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		subscribeToBP();
	}
	
	//Saves localCopy.
	//If localCopy has a unique ID, differing from every other BusinessPlan associated
	//with the current User's department, then this BusinessPlan is added to the Department's
	//list of BusinessPlans.
	//
	//IMPORTANT: If localCopy has a matching ID to any other BusinessPlan, then that other 
	//			 BusinessPlan will be overwritten. For this reason, ensure that your ID is correct.
	//
	//view() can be called to see all of the BusinessPlan IDs within a department.
	public void save() throws RemoteException
	{
		try
		{
			stub.save(userToken, localCopy, changeStub);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveToAdminDepartment() throws RemoteException
	{
		try
		{
			stub.saveToAdminDepartment(userToken, localCopy, changeStub);
			stub.save(userToken, localCopy, changeStub);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}

	//Adds a new user with the username, password, department, and admin privileges specified.
	//See Server's addUser() method for more information.
	public boolean addUser(String username, String password, String departmentName, boolean isAdmin)
	{
		try
		{
			return stub.addUser(userToken, username, password, departmentName, isAdmin);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	//Returns the ID, Name, and Year of each BusinessPlan associated with the current User's
	//department.
	//See Department's view() method for more information.
	
	public boolean addDepartment(String departmentName)
	{
		try
		{
			return stub.addDepartment(userToken, departmentName);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}
	
	public String[][] view()
	{
		try
		{
			String[][] plans = stub.view(userToken);
			System.out.println(plans.length);
			return plans;
		} catch (RemoteException e)
		{

			e.printStackTrace();
		}
		
		return null;
	}
	
	public String[][] view(Department dept)
	{
		try
		{
			return stub.view(userToken, dept);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public boolean isAdmin()
	{
		try
		{
			return stub.isAdmin(userToken);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return false;
	}

	//Basic getters and setters.
	public ServerInterface getStub()
	{
		return stub;
	}

	public void setStub(ServerInterface stub)
	{
		this.stub = stub;
	}

	public String getUserToken() throws RemoteException
	{
		return userToken;
	}

	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}

	public BP getLocalCopy()
	{
		return localCopy;
	}

	public void setLocalCopy(BP localCopy)
	{
		this.localCopy = localCopy;
	}
	
	public BP getBusinessPlan()
	{
		return localCopy;
	}

	public void setBusinessPlan(BP localCopy)
	{
		this.localCopy = localCopy;
	}
	
	public ArrayList<Department> getAllDepartments()
	{
		try
		{
			return  stub.getAllDepartments();
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	public void submitPlan() throws RemoteException
	{
		this.save();
	}
	
	public void readLocalPlanXML()
	//Gets the desired business plan from the XML file
	{
		String SERIALIZED_FILE_NAME = "BusinessPlan";
		
		XMLDecoder decoder = null;
		try
		{
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
		} catch (FileNotFoundException e)
		{
			System.out.println("ERROR: File " + SERIALIZED_FILE_NAME + " not found");
		}

		//Create the ConcreteServer from ConcreteServer.xml
		
		
		try 
		{
			localCopy = (BP) decoder.readObject();
		} 
		//Or instantiate a new ConcreteServer
		catch(Exception e)
		{
			localCopy = new BP();	
		}
		
		if(decoder != null)
		{
			decoder.close();
		}
	}
	
	//Saves current business plan to XML file
	public void saveLocalPlanXML()
	{
		if(localCopy != null)
		{
			XMLEncoder encoder = null;
			String fileName = "BusinessPlan";
			try
			{
				encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(fileName)));
				encoder.writeObject(localCopy);
				encoder.close();
			}
			catch(FileNotFoundException fileNotFound) {}
		}
		else
		{
			System.out.println("This client is not currently holding a business plan. Please choose one or create a new plan.");
		}
	}
	
	public void delete(BP bp)
	{
		try
		{
			stub.delete(userToken, bp);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public Department getDepartment()
	{
		try
		{
			return stub.getDepartment(userToken);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void saveBPToDepartment(BP bp, String dept) throws RemoteException
	{
		stub.saveBPToDepartment(userToken, bp, dept, changeStub);
	}

	@Override
	public void notifyBusinessPlanChanged() throws RemoteException
	{
		model.notifyBusinessPlanChanged();
		
		/*
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(AddNewUserPopupBoxController.class.getResource("/bpHasChangedPopup/BPHasChangedPopup.fxml"));
		Scene popupScene = new Scene(new AnchorPane());
		try
		{
			popupScene = new Scene(loader.load());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		BPHasChangedPopupController cont = loader.getController();
		Stage stage = new Stage();
		stage.setScene(popupScene);
		cont.setStage(stage);
		stage.show();
		*/
	}

	public String getToken()
	{
		return userToken;
	}

	public Model getModel()
	{
		return model;
	}

	public void setModel(Model model)
	{
		this.model = model;
	}
	
	public void subscribeToBP()
	{
		if(localCopy != null)
		{
			try
			{
				stub.subscribeToBP(localCopy, changeStub);
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void unsubscribeFromBP()
	{
		if(localCopy != null)
		{
			try
			{
				stub.unSubscribeFromBP(localCopy, changeStub);
			} catch (RemoteException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public BPChangeObserver exportObserver()
	{
		BPChangeObserver clientStub = changeObserver;
		try 
		{
			UnicastRemoteObject.unexportObject(changeObserver, true);
			clientStub = (BPChangeObserver) UnicastRemoteObject.exportObject(changeObserver, 0);
		}
		catch(RemoteException e)
		{
			try
			{
				clientStub = (BPChangeObserver) UnicastRemoteObject.exportObject(changeObserver, 0);
			} catch (RemoteException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		return clientStub;
	}
	
	public void sendMessage(String message)
	{
		try
		{
			stub.disperseText(userToken, localCopy, message, changeStub);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public void receiveText(String sender, String message)
	{
		model.receiveMessage(sender, message);
	}
}

