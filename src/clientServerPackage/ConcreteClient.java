package clientServerPackage;
import java.io.Serializable;
import java.rmi.RemoteException;

public class ConcreteClient implements Serializable
{
	private static final long serialVersionUID = 5532140250240995190L;
	public String username;
	public String password;
	public Department department;
	public String userToken;
	public boolean isAdmin;
	
	public ConcreteClient(String username, String password)
	{
		super();
		this.username = username;
		this.password = password;
		isAdmin = false;
	}
	
	public ConcreteClient() { }

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Department getDepartment()
	{
		return department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}

	public String getUserToken()
	{
		return userToken;
	}

	void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}

	public boolean isAdmin()
	{
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin)
	{
		this.isAdmin = isAdmin;
	}

	//Saves a BusinessPlan. Passes along that this user is a User so that
	//a plan only editable by Admins will not be overwritten.
	public void save(BP plan)
	{
		department.save(plan, false);
	}

	//Returns null to signify that the user was only a User and did not
	//have permissions to create a new User.
	public ConcreteClient addUser(String username, String password, boolean isAdmin)
	{
		return null;
	}
	
	public void delete(BP bp)
	{
		department.delete(bp);
	}
}
