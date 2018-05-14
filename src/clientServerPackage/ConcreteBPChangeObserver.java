package clientServerPackage;

import java.rmi.RemoteException;

public class ConcreteBPChangeObserver implements BPChangeObserver
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6191742286635249112L;
	transient ClientProxy client;
	String userToken;

	@Override
	public void notifyBusinessPlanChanged() throws RemoteException
	{
		client.notifyBusinessPlanChanged();
	}

	@Override
	public String getUserToken() throws RemoteException
	{
		return userToken;
	}

	public ClientProxy getClient()
	{
		return client;
	}

	public void setClient(ClientProxy client)
	{
		this.client = client;
	}

	public void setUserToken(String userToken)
	{
		this.userToken = userToken;
	}

	@Override
	public void receiveText(String sender, String message) throws RemoteException
	{
		client.receiveText(sender, message);
	}

	
}
