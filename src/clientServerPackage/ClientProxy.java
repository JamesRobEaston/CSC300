package clientServerPackage;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;

public class ClientProxy
{
	ServerInterface stub;
	String userToken;
	BP localCopy;

	public ClientProxy(ServerInterface server)
	{
		this.stub = server;
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
		} catch (RemoteException e)
		{
			System.out.println("Login failed.");
		}
	}

	//Sets localCopy to be whatever BusinessPlan has an ID matching bpid.
	//If an external application controlling Client wants localCopy, they should call
	//getLocalCopy() after calling retrieve(), with an appropriate bpid().
	public void retrieve(String bpid)
	{
		try
		{
			localCopy = stub.retrieve(userToken, bpid);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public void retrieve(String bpid, Department dept)
	{
		try
		{
			localCopy = stub.retrieve(userToken, bpid, dept);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
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
	public void save()
	{
		try
		{
			stub.save(userToken, localCopy);
		} catch (RemoteException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveToAdminDepartment()
	{
		try
		{
			stub.saveToAdminDepartment(userToken, localCopy);
			stub.save(userToken, localCopy);
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

	public String getUserToken()
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
	
	public void submitPlan()
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
		stub.saveBPToDepartment(userToken, bp, dept);
	}

}

