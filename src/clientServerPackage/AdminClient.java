package clientServerPackage;
import java.io.Serializable;

public class AdminClient extends ConcreteClient implements Serializable
{

	public AdminClient(String username, String password)
	{
		super(username, password);
		isAdmin = true;
	}
	
	public AdminClient()
	{
		super();
	}
	
	//Saves a BusinessPlan. Passes along that this user is a Admin so that
	//a plan only editable by Admins will be overwritten.
	@Override
	public void save(BP plan)
	{
		department.save(plan, true);
	}

	//Returns a new User or Admin object to be saved to the server's list
	//of Users.
	@Override
	public ConcreteClient addUser(String username, String password, boolean isAdmin)
	{
		ConcreteClient newUser;
		//If isAdmin is true, new user will be an Admin
		if(isAdmin)
		{
			newUser = new AdminClient(username, password);
		}
		//Otherwise, new user will be a User
		else
		{
			newUser = new ConcreteClient(username, password);
		}
		return newUser;
	}
	
}
