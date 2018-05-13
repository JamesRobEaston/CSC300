package clientServerPackage;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Hashtable;

public class ConcreteServer implements ServerInterface, Serializable
{

	private static final long serialVersionUID = 3409865608827353664L;
	public ArrayList<ConcreteClient> users;
	public ArrayList<Department> departments;
	public Random tokenGenerator;
	public static Department adminDepartment;
	public Hashtable<String, ArrayList<BPChangeObserver>> activeBusinessPlans;
	
	//The default constructor, primarily used for XMLDeserialization
	public ConcreteServer()
	{
		users = new ArrayList<ConcreteClient>();
		departments = new ArrayList<Department>();
		tokenGenerator = new Random(System.currentTimeMillis());
		activeBusinessPlans = new Hashtable<String, ArrayList<BPChangeObserver>>();
	}
	
	//The primary constructor for the server, which adds an initial default admin to the server.
	public ConcreteServer(String adminUsername, String adminPassword)
	{
		users = new ArrayList<ConcreteClient>();
		//Give the Server an initial Admin which can be used to add other users.
		ConcreteClient initAdmin = (new AdminClient(adminUsername, adminPassword));
		adminDepartment = new Department("Admin");
		initAdmin.setDepartment(adminDepartment);
		users.add(initAdmin);
		departments = new ArrayList<Department>();
		departments.add(adminDepartment);
		tokenGenerator = new Random(System.currentTimeMillis());
		activeBusinessPlans = new Hashtable<String, ArrayList<BPChangeObserver>>();
	}

	//This method generates a unique user token that is used to identify users, which is returned.
	public String genUserToken()
	{
		String token = "";
		for (int i = 0; i < 16; i++)
		{
			token = token + tokenGenerator.nextInt(10);
		}
		return token;
	}

	public ArrayList<ConcreteClient> getUsers() throws RemoteException
	{
		return users;
	}

	public void setUsers(ArrayList<ConcreteClient> users)
	{
		this.users = users;
	}

	public Random getTokenGenerator()
	{
		return tokenGenerator;
	}

	public void setTokenGenerator(Random tokenGenerator)
	{
		this.tokenGenerator = tokenGenerator;
	}

	public ArrayList<Department> getDepartments()
	{
		return departments;
	}

	public void setDepartments(ArrayList<Department> departments)
	{
		this.departments = departments;
	}
	
	public ArrayList<Department> getAllDepartments() throws RemoteException
	{
		return departments;
	}

	// Helper method that returns the user that matches the userToken, or
	// returns null if no such user exists.
	public ConcreteClient matchUser(String userToken)
	{
		Iterator<ConcreteClient> userIterator = this.users.iterator();
		ConcreteClient user;
		//For every user in users
		while (userIterator.hasNext())
		{
			user = userIterator.next();
			//Return this user if it's userToken matches the given userToken
			if (user.getUserToken() != null && user.getUserToken().equals(userToken))
			{
				return user;
			}
		}
		//Return null if no match was found
		return null;
	}

	//Checks that the user exists in the server's list of users and that it's password
	//matches the given password before generating and returning a userToken
	@Override
	public String authenticate(String username, String password) throws RemoteException
	{
		Iterator<ConcreteClient> userIterator = this.users.iterator();
		ConcreteClient user;
		//For every user in users
		while (userIterator.hasNext())
		{
			user = userIterator.next();
			//If this user's username matches the given username
			if (user.getUsername().equals(username))
			{
				//If this user's password matches the given password
				if (user.getPassword().equals(password))
				{
					//Generate a unique userToken
					String userToken = "";
					boolean duplicate = true;
					while (duplicate)
					{
						userToken = genUserToken();
						if (matchUser(userToken) == null)
						{
							duplicate = false;
						}
					//Give the userToken to the user and return it
					}
					user.setUserToken(userToken);
					return userToken;
				}
			}
		}
		//If no username/password match was found, return null.
		return null;
	}

	// Returns the id, name, and year of all BusinessPlans within a
	// user's department.
	@Override
	public String[][] view(String userToken) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if (user == null)
		{
			return null;
		}
		return user.getDepartment().view();
	}

	// Returns the BusinessPlan which matches the id given.
	@Override
	public BP retrieve(String userToken, String bpid) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if (user == null)
		{
			return null;
		}
		BP bp = user.getDepartment().retrieve(bpid);
		return bp;
	}

	// Saves the BusinessPlan within the department.
	@Override
	public void save(String userToken, BP plan, BPChangeObserver stub) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		
		if (user == null)
		{
			return;
		}

		notifyAllUsersBPChanged(plan, stub);
		user.save(plan);
		
	}

	// Adds a new ConcreteClient to the server, only will work if this method was
	// called by an Admin. 
	// IMPORTANT: If departmentName is not already registered, then a new Department will be created.
	//		   	  We leave it to the Application calling addUser to handle typos and ensure departmentName is accurate.
	@Override
	public boolean addUser(String userToken, String username, String password, String departmentName, boolean isAdmin) throws RemoteException
	{
		// Check to ensure that username isn't already taken.
		Iterator<ConcreteClient> userIterator = this.users.iterator();
		ConcreteClient currUser;
		//For each user in users
		while (userIterator.hasNext())
		{
			currUser = userIterator.next();
			//Return before adding new user if username matches a previous user's username
			if (currUser.getUsername().equals(username))
			{
				return false;
			}
		}
		// Get the user trying to add a new user, have them create the new ConcreteClient object.
		ConcreteClient user = matchUser(userToken);
		if (user == null) {return false;}
		ConcreteClient newUser = user.addUser(username, password, isAdmin);
		// Set the user's department and add the ConcreteClient if one was actually returned.
		if (newUser != null)
		{
			// Give the ConcreteClient their department, creating a new one if no match was found
			Iterator<Department> departmentIterator = this.departments.iterator();
			Department department = null;
			boolean found = false;
			
			// Loop through each known department to see if the requested department already exists
			while ((!found)&&(departmentIterator.hasNext()))
			{
				department = departmentIterator.next();
				if(department.getDepartmentName().equals(departmentName))
				{
					found = true;
				}
			}
			//Create a new department if the ConcreteClient's requested department does not exist.
			if(!found)
			{
				department = new Department(departmentName);
				departments.add(department);
			}
			newUser.setDepartment(department);
			users.add(newUser);
			return true;
		}
		
		return false;
	}
	
	public boolean addDepartment(String userToken, String departmentName) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		
		if(user instanceof AdminClient)
		{
			boolean valid = true;
			Iterator<Department> iterator = departments.iterator();
			Department department;
			while(valid && iterator.hasNext())
			{
				department = iterator.next();
				if(department.getName().equals(departmentName))
				{
					valid = false;
				}
			}
			if(valid)
			{
				departments.add(new Department(departmentName));
				return true;
			}
		}
		
		return false;
	}
	
	//Called by an independent thread, which is created when the Server is constructed.
	//Writes this Server object to Server.xml.
	public void saveAll()
	{
		// Serialize Server and save to file.
		XMLEncoder encoder = null;
		try
		{
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream("Server.xml")));
		}
		catch(FileNotFoundException fileNotFound) {}
		
		encoder.writeObject(this);
		encoder.close();
	}
	
	public boolean isAdmin(String userToken) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if(user == null)
		{
			return false;
		}
		return user.isAdmin();
	}
	
	public void delete(String userToken, BP bp)
	{
		ConcreteClient user = matchUser(userToken);
		user.delete(bp);
	}
	
	public Department getDepartment(String userToken)
	{
		return matchUser(userToken).getDepartment();
	}

	@Override
	public void saveToAdminDepartment(String userToken, BP bp, BPChangeObserver stub) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if(user != null)
		{
			notifyAllUsersBPChanged(bp, stub);
			
			adminDepartment.save(bp, user.isAdmin());
		}
	}

	@Override
	public void saveBPToDepartment(String userToken, BP bp, String dept, BPChangeObserver stub) throws RemoteException
	{
		Iterator<Department> departmentIterator = this.departments.iterator();
		Department department = null;
		boolean found = false;
		
		// Loop through each known department to see if the requested department already exists
		while ((!found)&&(departmentIterator.hasNext()))
		{
			department = departmentIterator.next();
			if(department.getDepartmentName().equals(dept))
			{
				found = true;
			}
		}
		//Create a new department if the ConcreteClient's requested department does not exist.
		if(found)
		{
			notifyAllUsersBPChanged(bp, stub);
			
			ConcreteClient user = matchUser(userToken);
			department.save(bp, user.isAdmin());
		}
		
	}

	@Override
	public String[][] view(String userToken, Department dept) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if(user.isAdmin())
		{
			return dept.view();
		}
		return null;
	}

	@Override
	public BP retrieve(String userToken, String bpid, Department dept) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if(user.isAdmin())
		{
			String departmentName = dept.getDepartmentName();
			Iterator<Department> iterator = departments.iterator();
			boolean foundDept = false;
			Department department = new Department();
			
			while(!foundDept && iterator.hasNext())
			{
				department = iterator.next();
				if(department.getDepartmentName().equals(departmentName))
				{
					foundDept = true;
				}
			}
			
			if(foundDept)
			{
				BP bp = department.retrieve(bpid); 
				return bp;
			}
		}
		
		return null;
	}
	
	//Helper method to add a user to the list of clients viewing the specified BP
	public void subscribeToBP(BP bp, BPChangeObserver stub) throws RemoteException
	{
		String bpid = bp.getID();
		ArrayList<BPChangeObserver> clients = activeBusinessPlans.get(bpid);
		if(clients == null)
		{
			clients = new ArrayList<BPChangeObserver>();
			activeBusinessPlans.put(bpid, clients);
		}
		clients.add(stub);
	}
	
	//Helper method to notify all users if a BP has changed
	private void notifyAllUsersBPChanged(BP bp, BPChangeObserver stub) throws RemoteException
	{
		ArrayList<BPChangeObserver> clients = activeBusinessPlans.get(bp.getID());
		if(clients != null)
		{
			for(int i = 0; i < clients.size(); i++)
			{
				BPChangeObserver observer = clients.get(i);
				System.out.println(observer.getUserToken());
				System.out.println(stub.getUserToken());
				if(observer.getUserToken().equals(stub.getUserToken()))
				{}
				else
				{
					try
					{
						observer.notifyBusinessPlanChanged();
					}
					catch(RemoteException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		else
		{
			clients = new ArrayList<BPChangeObserver>();
			clients.add(stub);
			activeBusinessPlans.put(bp.getID(), clients);
		}
	}

	//If a user stops viewing a BP, remove them from activeBusinessPlans
	@Override
	public void unSubscribeFromBP(BP bp, BPChangeObserver stub) throws RemoteException
	{
		//Remove the user from the list of clients viewing the plan
		ArrayList<BPChangeObserver> clients = activeBusinessPlans.get(bp.getID());
		for(int i = 0; i < clients.size(); i++)
		{
			BPChangeObserver client = clients.get(i);
			if(client.getUserToken() == stub.getUserToken())
			{
				clients.remove(i);
			}
		}
		
		//If there are no more clients viewing the plan, remove the plan from the list of active plans
		if(clients.size() == 0)
		{
			activeBusinessPlans.remove(bp.getID());
		}
		
	}
	
	

}
