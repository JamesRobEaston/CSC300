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
import java.util.concurrent.TimeUnit;

public class ConcreteServer implements ServerInterface, Serializable
{

	public ArrayList<ConcreteClient> users;
	public ArrayList<Department> departments;
	public Random tokenGenerator;
	public static Department adminDepartment;
	
	//The default constructor, primarily used for XMLDeserialization
	public ConcreteServer()
	{
		users = new ArrayList<ConcreteClient>();
		departments = new ArrayList<Department>();
		tokenGenerator = new Random(System.currentTimeMillis());
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
		return user.getDepartment().retrieve(bpid);
	}

	// Saves the BusinessPlan within the department.
	@Override
	public void save(String userToken, BP plan) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		
		if (user == null)
		{
			return;
		}
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
	public void saveToAdminDepartment(String userToken, BP bp) throws RemoteException
	{
		ConcreteClient user = matchUser(userToken);
		if(user != null)
		{
			adminDepartment.save(bp, user.isAdmin());
		}
	}

	@Override
	public void saveBPToDepartment(String userToken, BP bp, String dept) throws RemoteException
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
				return department.retrieve(bpid);
			}
		}
		
		return null;
	}

}
